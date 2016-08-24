package models.view

import java.util.UUID._

import app.ConfigProperties._
import gov.dwp.exceptions.DwpRuntimeException
import models.domain.{Claim, ClaimDate, QuestionGroup, YourDetails}
import models.view.ClaimHandling.ClaimResult
import models.view.cache.EncryptedCacheHandling
import play.api.cache.Cache
import play.api.data.Form
import play.api.i18n.Lang
import play.api.http.HeaderNames._
import play.api.mvc.Results._
import play.api.mvc._
import play.api.{Logger, Play}
import play.api.Play.current
import utils.helpers.OriginTagHelper

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions
import scala.reflect.ClassTag
import scala.util.{Success, Try}

object ClaimHandling {
  type ClaimResult = (Claim, Result)
  // Versioning
  def C3NAME = s"${getStringProperty("application.name")}"
  def C3VERSION = s"${C3NAME.toUpperCase}Version"
  def C3VERSION_VALUE = getStringProperty("application.version").takeWhile(_ != '-')
  def C3VERSION_SECSTOLIVE = getIntProperty("application.seconds.to.live")
  val applicationFinished = "application-finished"
  val GA_COOKIE="_ga"
}

trait ClaimHandling extends RequestHandling with EncryptedCacheHandling {
  protected def claimNotValid(claim: Claim): Boolean
  protected def newInstance(newuuid: String = randomUUID.toString): Claim
  protected def copyInstance(claim: Claim): Claim
  protected def backButtonPage : Call

  def googleAnalyticsAgentId(request: Request[AnyContent])={
    request.cookies.get(ClaimHandling.GA_COOKIE) match{
      case Some(cookie) => cookie.value
      case _ => "N/A"
    }
  }

  //============================================================================================================
  //         NEW CLAIM
  //============================================================================================================

  /**
   * Called when starting a new claim. Overwrites CSRF token and Version in case user had old cookies.
   */
  def newClaim(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult]): Action[AnyContent] = Action {
    request => {
      implicit val r = request
      val key = keyFrom(request)

      recordMeasurements()

      if (request.getQueryString("changing").getOrElse("false") == "false") {
        // Delete any old data to avoid somebody getting access to session left by somebody else
        if (!key.isEmpty) Cache.remove(key)
        // Start with new claim
        val claim = newInstance()
        renameThread(claim.uuid)
        Logger.info(s"New ${claim.key} ${claim.uuid} with google-analytics:${googleAnalyticsAgentId(r)}.")


        implicit val newRequest = createNewRequest(request)

        // Added C3Version for full Zero downtime
        Logger.info(s"New C3Version cookie for ${claim.uuid} value:${ClaimHandling.C3VERSION_VALUE} expiresecs:${ClaimHandling.C3VERSION_SECSTOLIVE}")
        withHeaders(action(claim, newRequest, bestLang)(f))
          .withCookies(newRequest.cookies.toSeq.filterNot(toFilter) :+ Cookie(ClaimHandling.C3VERSION, ClaimHandling.C3VERSION_VALUE, Some(ClaimHandling.C3VERSION_SECSTOLIVE)): _*)
          .withSession(claim.key -> claim.uuid)
          .discardingCookies(DiscardingCookie(ClaimHandling.applicationFinished))
      } else {

        implicit val r = request
        Logger.debug("New claim with changing true.")
        if (key.isEmpty) Redirect(errorPageCookie)
        else {
          Logger.info(s"Changing $cacheKey - $key")
          val claim = fromCache(request).getOrElse(throw new DwpRuntimeException("I expected a claim in the cache since we are only changing request, e.g. chainging language.!"))
          originCheck(action(claim, request, getLang(claim))(f))
        }
      }
    }
  }

  /*
     Resume Claim. If we successfully resume the claim, the original claim will be loaded into the cache.
     We check response and pickup the new claim and set in session in action()
   */
  def resumeClaim(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult]): Action[AnyContent] = Action {
    request => {
      implicit val r = request
      recordMeasurements()

      val claim = newInstance()
      withHeaders(action(claim, r, bestLang)(f))
    }
  }

  def createNewRequest(request: Request[AnyContent]) = {
    // This workaround shit has been put in place in order to clear up the PLAY_LANG cookie at the startup of the app.
    // It's this ugly because the play framework has deemed us outcasts on the cookie managing for requests.
    // Since the discarding only takes place after the rendering of the first page, that wasn't of much use for that page
    // So we have to rely on dark arts in order to modify the cookies before render time so the framework doesn't read the lingering language from here
    val lang = if (OriginTagHelper.isOriginGB())request.getQueryString("lang").getOrElse("") else "";
    val newHeaders = request.headers.get(COOKIE) match {
      case Some(cookieHeader) => request.headers.remove(COOKIE).add(COOKIE->Cookies.mergeCookieHeader(cookieHeader,Seq(Cookie("PLAY_LANG",lang))))
      case _ => request.headers.remove(COOKIE).add(COOKIE->Cookies.mergeCookieHeader("", Seq(Cookie("PLAY_LANG",lang))))
    }
    Request(request.copy(headers=newHeaders),request.body)
  }

  /*
    If we have a claim, whether valid or not then we keep it since we might go back to it later.
    If we dont have a claim then create a new one.
 */
  def optionalClaim(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult]): Action[AnyContent] = Action {
    request => {
      implicit val r = request
      fromCache(request) match {
        case Some(claim) => {
          Logger.info("ClaimHandling optionalClaim for existing claim:" + claim.uuid)
          withHeaders(action(claim, r, bestLang)(f))
        }
        case _ => {
          implicit val newRequest = createNewRequest(request)

          val claim = newInstance()
          Logger.info("ClaimHandling optionalClaim created new claim:" + claim.uuid)
          withHeaders(action(claim, newRequest, bestLang)(f))
            .withCookies(newRequest.cookies.toSeq.filterNot(toFilter) :+ Cookie(ClaimHandling.C3VERSION, ClaimHandling.C3VERSION_VALUE, Some(ClaimHandling.C3VERSION_SECSTOLIVE)): _*)
            .withSession(claim.key -> claim.uuid)
            .discardingCookies(DiscardingCookie(ClaimHandling.applicationFinished))
        }
      }
    }
  }

  // Cookies need to be changed BEFORE session, session is within cookies.
  def toFilter(theCookie: Cookie): Boolean = {
    theCookie.name == ClaimHandling.C3VERSION || theCookie.name == getStringProperty("play.http.session.cookieName")
  }

  //============================================================================================================
  //         GOING THROUGH CLAIM
  //============================================================================================================

  /**
   * Here we are checking the mandatory fields for Circs : full name and nino on the about you page and
   * for claim : firstName, lastName and nino of the claimant. This check is done to ensure that the
   * mandatory fields are always present when we submit the claim or circs. An error page is displayed
   * if the mandatory fields are missing. We found the mandatory fields missing when the user uses the
   * browser's back and forward buttons instead of the ones provided by the application.
   */
  def claimingWithCheck(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult],checkCookie:Boolean = false) = claimingWithConditions(f, cookieCheck = checkCookie, isNotValidClaim = claimNotValid)

  def claiming(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult],checkCookie:Boolean = false): Action[AnyContent] = claimingWithConditions(f, cookieCheck = checkCookie)

  private def claimingWithConditions(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult], cookieCheck: Boolean, isNotValidClaim: Claim => Boolean = noClaimValidation): Action[AnyContent] = Action {
    implicit request =>
      renameThread(request)
      enforceAlreadyFinishedRedirection(request,
        originCheck(
          fromCache(request) match {
            case Some(claim) if isNotValidClaim(claim) && !Play.isTest =>
              Logger.error(s"$cacheKey - cache: ${keyFrom(request)} lost the claim date and claimant details")
              Logger.info(s"path:${request.path} - claim uuid ${claim.uuid} not valid claim.questionGroup[ClaimDate].isDefined:${claim.questionGroup[ClaimDate].isDefined} claim.questionGroup[YourDetails].isDefined:${claim.questionGroup[YourDetails].isDefined}")
              Redirect(errorPageBrowserBackButton)

            case Some(claim) => claimingWithClaim(f, request, claim)

            case None if cookieCheck && !Play.isTest => Redirect(errorPageCookie)

            case None => claimingWithoutClaim(f, request)
          }
        )
      )

  }

  private def noClaimValidation(claim: Claim) = false

  private def claimingWithClaim(f: (Claim) => (Request[AnyContent]) => (Lang) => Either[Result, (Claim, Result)], request: Request[AnyContent], claim: Claim): Result = {
    val key = keyFrom(request)
    Logger.info(s"claimingWithClaim - ${claim.key} ${claim.uuid} - ${request.method} url ${request.path}")
    implicit val r = request

    if (key != claim.uuid) Logger.error(s"claimingWithClaim - Claim uuid ${claim.uuid} does not match cache key $key.")
    action(copyInstance(claim), request, getLang(claim))(f).withSession(claim.key -> claim.uuid)
  }

  private def claimingWithoutClaim(f: (Claim) => (Request[AnyContent]) => (Lang) => Either[Result, (Claim, Result)], request: Request[AnyContent]): Result = {
    if (Play.isTest) {
      implicit val r = request
      val claim = newInstance()
      renameThread(claim.uuid)
      saveInCache(claim) // place an empty claim in the cache to satisfy tests
      // Because a test can start at any point of the process we have to be sure the claim uuid is in the session.
      action(claim, request, bestLang)(f).withSession(claim.key -> claim.uuid)
    } else {
      val uuid = keyFrom(request)
      Logger.info(s"claimingWithoutClaim - uuid $uuid - ${request.method} url ${request.path}")
      if (uuid.isEmpty) {
        Redirect(errorPage)
      } else {
        Logger.warn(s"$cacheKey - $uuid timeout")
        Redirect(timeoutPage)
      }
    }
  }

  //============================================================================================================
  //         END OF CLAIM
  //============================================================================================================

  def ending(f: Claim => Request[AnyContent] => Lang => Result): Action[AnyContent] = Action {
    request => {
      implicit val r = request
      val theDomain = Session.domain

      fromCache(request, required = false) match {
        case Some(claim) =>
          Logger.info(s"ending claim - ${claim.key} ${claim.uuid} - ${request.method} url ${request.path}")
          // reaching end of process - thank you page so we delete claim for security reasons and free memory
          removeFromCache(claim.uuid)
          if (getBooleanProperty("saveForLaterSaveEnabled") || getBooleanProperty("saveForLaterResumeEnabled")) removeSaveForLaterFromCache(claim.uuid)
          originCheck(f(claim)(request)(getLang(claim))).discardingCookies(DiscardingCookie(csrfCookieName, secure = csrfSecure, domain = theDomain),
            DiscardingCookie(ClaimHandling.C3VERSION),DiscardingCookie("PLAY_LANG")).withNewSession.withCookies(Cookie(ClaimHandling.applicationFinished, "true"))
        case _ =>
          Logger.info(s"ending no claim - ${request.method} url ${request.path}")
          enforceAlreadyFinishedRedirection(request,
            originCheck(f(Claim(cacheKey))(request)(bestLang)).discardingCookies(DiscardingCookie(csrfCookieName, secure = csrfSecure, domain = theDomain),
              DiscardingCookie(ClaimHandling.C3VERSION),DiscardingCookie("PLAY_LANG")).withNewSession.withCookies(Cookie(ClaimHandling.applicationFinished, "true"))
          )
      }
    }
  }

  def endingOnError(f: Claim => Request[AnyContent] => Lang => Result): Action[AnyContent] = Action {
    request => {
      implicit val r = request
      originCheck(f(Claim(cacheKey))(request)(bestLang))
    }
  }

  //============================================================================================================
  //         UTILITY FUNCTIONS
  //============================================================================================================

  private def enforceAlreadyFinishedRedirection(request: Request[AnyContent], otherwise: => Result): Result =
    if (request.cookies.exists {
      case Cookie(ClaimHandling.applicationFinished, "true", _, _, _, _, _) =>
        Logger.info("User already completed claim. Redirection to back button page.")
        request.cookies.foreach(c => Logger.info(s"path:${request.path} cookie name:${c.name} cookie value:${c.value}"))
        true
      case _ => false
    }) Redirect(backButtonPage)
    else otherwise

  protected def action(claim: Claim, request: Request[AnyContent], lang: Lang)(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult]): Result = {
    val key = keyFrom(request)
    if (!key.isEmpty && key != claim.uuid) Logger.warn(s"action - Claim uuid ${claim.uuid} does not match cache key $key. Can happen if action new claim and user reuses session. Will disregard session key and use uuid.")

    f(claim)(request)(lang) match {
      case Left(r: Result) => r
      case Right((c: Claim, r: Result)) => {
        request.uri match {
          case "/save" => {
            saveInCache(c)
            r
          }
          case "/resume" if( !claim.uuid.equals(c.uuid)) =>{
              // If we have resumed and the claim has been authenticated and successully switched, we need to set the cookie
              r.withSession(claim.key -> c.uuid)
          }
          case _ => {
            saveInCache(c.update(saveForLaterPageData = Map[String,String]()))
            r
          }
        }
      }
    }
  }

  implicit def formFiller[Q <: QuestionGroup](form: Form[Q])(implicit classTag: ClassTag[Q]) = new {
    def fill(qi: QuestionGroup.Identifier)(implicit claim: Claim): Form[Q] = {
      (claim.saveForLaterCurrentPageData.isEmpty, claim.questionGroup(qi)) match {
        case (false, _) => form.copy[Q](data = claim.saveForLaterCurrentPageData)
        case (_, Some(q: Q)) => form.fill(q)
        case _ => form
      }
    }
  }

  implicit def defaultResultToLeft(result: Result): Left[play.api.mvc.Result, (models.domain.Claim, play.api.mvc.Result)] = Left(result)

  implicit def claimAndResultToRight(claimingResult: ClaimResult): Right[play.api.mvc.Result, (models.domain.Claim, play.api.mvc.Result)] = Right(claimingResult)

  implicit def actionWrapper(action: Action[AnyContent]) = new {

    def withPreview(): Action[AnyContent] = Action.async(action.parser) { request =>
      Logger.debug("actionWrapper")
      action(request).map { result =>
        result.header.status -> fromCache(request) match {
          case (play.api.http.Status.SEE_OTHER, Some(claim)) if claim.navigation.beenInPreview => Redirect(controllers.preview.routes.Preview.present().url + getReturnToSummaryValue(claim))
          case _ => result
        }
      }
    }

    // If the curried function returns true, this action will be redirected to preview if we have been there previously
    // The data feeded to the curried function is the current submitted value of the claim, and the previously saved claim the moment we visited preview page.
    def withPreviewConditionally[T <: QuestionGroup](t: ((Option[T], T),(Option[Claim],Claim)) => Boolean)(implicit classTag: ClassTag[T]): Action[AnyContent] = Action.async(action.parser) { request =>

      def getParams[E <: T](claim: Claim)(implicit classTag: ClassTag[E]): (Option[E], E) = {
        claim.checkYAnswers.previouslySavedClaim.map(_.questionGroup(classTag.runtimeClass).getOrElse(None)).asInstanceOf[Option[E]] -> claim.questionGroup(classTag.runtimeClass).get.asInstanceOf[E]
      }

      action(request).map { result =>
        result.header.status -> fromCache(request) match {
          case (play.api.http.Status.SEE_OTHER, Some(claim)) if claim.navigation.beenInPreview && t(getParams(claim),claim.checkYAnswers.previouslySavedClaim->claim) => Redirect(controllers.preview.routes.Preview.present().url + getReturnToSummaryValue(claim))
          case _ => result
        }
      }
    }
  }

  def getReturnToSummaryValue(claim : Claim) : String = {
    claim.checkYAnswers.returnToSummaryAnchor == "" match {
      case true => ""
      case false => "#" + claim.checkYAnswers.returnToSummaryAnchor
    }
  }

  def getLang(claim: Claim)(implicit request: Request[AnyContent]): Lang = claim.lang.getOrElse(bestLang(request))

  private def bestLang(implicit request: Request[AnyContent]) = {
    val implementedLangs = getListProperty("play.i18n.langs")
    val listOfPossibleLangs = request.acceptLanguages.flatMap(aL => implementedLangs.split(",").toList.filter(iL => iL == aL.code))

    if (listOfPossibleLangs.size > 0)
      Lang(listOfPossibleLangs.head)
    else
      Lang(defaultLang)
  }
  def getListProperty(property:String) = Try(Play.current.configuration.getString(property).getOrElse("lang-error")) match { case Success(s) => s case _ => "lang-error"}


  def createParamsMap(parameters: Map[String, Seq[String]]) = {
    parameters.map { case (k, v) => k -> v.mkString }
  }
}
