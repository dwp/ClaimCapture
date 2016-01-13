package models.view

import java.util.UUID._

import app.ConfigProperties._
import gov.dwp.exceptions.DwpRuntimeException
import models.domain.{Claim, QuestionGroup}
import models.view.ClaimHandling.ClaimResult
import models.view.cache.EncryptedCacheHandling
import play.api.cache.Cache
import play.api.data.Form
import play.api.http.HeaderNames._
import play.api.http.HttpVerbs
import play.api.i18n.Lang
import play.api.http.HeaderNames._
import play.api.mvc.Results._
import play.api.mvc._
import play.api.{Logger, Play}
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.implicitConversions

import scala.reflect.ClassTag

object ClaimHandling {
  type ClaimResult = (Claim, Result)
  // Versioning
  val C3VERSION = "C3Version"
  val C3VERSION_VALUE = "3.2"
  val applicationFinished = "application-finished"

}

trait ClaimHandling extends RequestHandling with EncryptedCacheHandling {
  protected def claimNotValid(claim: Claim): Boolean
  protected def newInstance(newuuid: String = randomUUID.toString): Claim
  protected def copyInstance(claim: Claim): Claim
  protected def backButtonPage : Call


  //============================================================================================================
  //         NEW CLAIM
  //============================================================================================================

  /**
   * Called when starting a new claim. Overwrites CSRF token and Version in case user had old cookies.
   */
  def newClaim(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult]): Action[AnyContent] = Action {
    request => {
      implicit val r = request

      recordMeasurements()
      val key = keyFrom(request)

      if (request.getQueryString("changing").getOrElse("false") == "false") {
        // Delete any old data to avoid somebody getting access to session left by somebody else
        if (!key.isEmpty) Cache.remove(key)
        // Start with new claim
        val claim = newInstance()
        Logger.info(s"New ${claim.key} ${claim.uuid}.")
        // Cookies need to be changed BEFORE session, session is within cookies.
        def tofilter(theCookie: Cookie): Boolean = {
          theCookie.name == ClaimHandling.C3VERSION || theCookie.name == getProperty("session.cookieName", "PLAY_SESSION")
        }

        // This workaround shit has been put in place in order to clear up the PLAY_LANG cookie at the startup of the app.
        // It's this ugly because the play framework has deemed us outcasts on the cookie managing for requests.
        // Since the discarding only takes place after the rendering of the first page, that wasn't of much use for that page
        // So we have to rely on dark arts in order to modify the cookies before render time so the framework doesn't read the lingering language from here

        val newHeaders = request.headers.get(COOKIE) match {
          case Some(cookieHeader) => request.headers.remove(COOKIE).add(COOKIE->Cookies.mergeCookieHeader(cookieHeader,Seq(Cookie("PLAY_LANG",""))))
          case _ => request.headers
        }

        implicit val newRequest = Request(request.copy(headers=newHeaders),request.body)

        // Added C3Version for full Zero downtime
        withHeaders(action(claim, newRequest, bestLang)(f))
          .withCookies(newRequest.cookies.toSeq.filterNot(tofilter) :+ Cookie(ClaimHandling.C3VERSION, ClaimHandling.C3VERSION_VALUE): _*)
          .withSession(claim.key -> claim.uuid)
          .discardingCookies(DiscardingCookie(ClaimHandling.applicationFinished),DiscardingCookie("PLAY_LANG"))
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
     Resume Claim. If we succesfully resume the claim, the original claim will be loaded into the cache.
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
      enforceAlreadyFinishedRedirection(request,
        originCheck(
          fromCache(request) match {
            case Some(claim) if isNotValidClaim(claim) && !Play.isTest =>
              Logger.error(s"$cacheKey - cache: ${keyFrom(request)} lost the claim date and claimant details")
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
    // We log just to be able to invstigate issues with claims/circs and see path taken by customer. Do not need to log all GETs and POSTs in production (INFO).
    if (request.method == HttpVerbs.GET) Logger.info(s"claimingWithClaim - ${claim.key} ${claim.uuid} - GET url ${request.path}")
    else Logger.debug(s"claimingWithClaim - ${claim.key} ${claim.uuid} - ${request.method} url ${request.path}")
    implicit val r = request
    val key = keyFrom(request)
    if (key != claim.uuid) Logger.error(s"claimingWithClaim - Claim uuid ${claim.uuid} does not match cache key $key.")
    action(copyInstance(claim), request, getLang(claim))(f).withSession(claim.key -> claim.uuid)
  }

  private def claimingWithoutClaim(f: (Claim) => (Request[AnyContent]) => (Lang) => Either[Result, (Claim, Result)], request: Request[AnyContent]): Result = {
    if (Play.isTest) {
      implicit val r = request
      val claim = newInstance()
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
          Logger.info(s"ending - ${claim.key} ${claim.uuid} - ${request.method} url ${request.path}")
          // reaching end of process - thank you page so we delete claim for security reasons and free memory
          removeFromCache(claim.uuid)
          removeSaveForLaterFromCache(claim.uuid)
          originCheck(f(claim)(request)(getLang(claim))).discardingCookies(DiscardingCookie(csrfCookieName, secure = csrfSecure, domain = theDomain),
            DiscardingCookie(ClaimHandling.C3VERSION),DiscardingCookie("PLAY_LANG")).withNewSession.withCookies(Cookie(ClaimHandling.applicationFinished, "true"))
        case _ =>
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
      case Cookie(applicationFinished, "true", _, _, _, _, _) =>
        Logger.info("User already completed claim. Redirection to back button page.")
        true
      case _ => false
    }) Redirect(backButtonPage)
    else otherwise

  protected def action(claim: Claim, request: Request[AnyContent], lang: Lang)(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult]): Result = {
    val key = keyFrom(request)
    if (!key.isEmpty && key != claim.uuid) Logger.warn(s"action - Claim uuid ${claim.uuid} does not match cache key $key. Can happen if action new claim and user reuses session. Will disregard session key and use uuid.")

    //val newRequest = createNewRequest(request) //??? //Create new request modifiying the current cookie to add whatever setLang does
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
          case (play.api.http.Status.SEE_OTHER, Some(claim)) if claim.navigation.beenInPreview => Redirect(controllers.preview.routes.Preview.present())
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
          case (play.api.http.Status.SEE_OTHER, Some(claim)) if claim.navigation.beenInPreview && t(getParams(claim),claim.checkYAnswers.previouslySavedClaim->claim) => Redirect(controllers.preview.routes.Preview.present())
          case _ => result
        }
      }
    }
  }

  def getLang(claim: Claim)(implicit request: Request[AnyContent]): Lang = claim.lang.getOrElse(bestLang(request))

  private def bestLang(implicit request: Request[AnyContent]) = {
    val implementedLangs = getProperty("play.i18n.langs", defaultLang)
    val listOfPossibleLangs = request.acceptLanguages.flatMap(aL => implementedLangs.split(",").toList.filter(iL => iL == aL.code))

    if (listOfPossibleLangs.size > 0)
      Lang(listOfPossibleLangs.head)
    else
      Lang(defaultLang)
  }

  def createParamsMap(parameters: Map[String, Seq[String]]) = {
    parameters.map { case (k, v) => k -> v.mkString }
  }
}
