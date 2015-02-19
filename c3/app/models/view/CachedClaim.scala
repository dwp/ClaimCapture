package models.view

import app.ConfigProperties._
import java.util.UUID._

import scala.language.implicitConversions
import scala.reflect.ClassTag
import play.api.Play.current
import play.api.mvc._
import play.api.data.Form
import play.api.cache.Cache
import play.api.{Logger, Play}
import play.api.mvc.Results._
import play.api.http.HeaderNames._
import models.domain._
import controllers.routes
import play.api.i18n.Lang
import scala.concurrent.ExecutionContext
import models.domain.Claim
import models.view.CachedClaim.ClaimResult
import monitoring.Histograms
import scala.util.Try
import net.sf.ehcache.CacheManager
import ExecutionContext.Implicits.global

object CachedClaim {
  val missingRefererConfig = "Referer not set in config"
  val key = "claim"
  type ClaimResult = (Claim, Result)
  // Versioning
  val C3VERSION = "C3Version"
  val C3VERSION_VALUE = "2.11"
}

/**
 * The Unique ID use to identified a claim/circs in the cache is a UUID, stored in the claim/circs and set in the request session (cookie).
 */
trait CachedClaim {

  type IterationID = String

  val cacheKey = CachedClaim.key

  // Common pages
  val startPage: String = getProperty("claim.start.page", "/allowance/benefits")
  val timeoutPage = routes.ClaimEnding.timeout()
  val errorPageCookie = routes.ClaimEnding.errorCookie()
  val errorPage = routes.ClaimEnding.error()  
  val errorPageBrowserBackButton = routes.ClaimEnding.errorBrowserBackbutton()

  private val defaultLang = "en"

  // CSRF Cookie management
  protected val csrfCookieName = getProperty("csrf.cookie.name", "csrf")
  protected val csrfSecure = getProperty("csrf.cookie.secure", getProperty("session.secure",default = false))

  // Expiration value
  private val  expiration = getProperty("cache.expiry", 3600)

  private lazy val redirectEnforced = getProperty("enforceRedirect", default = true)


  implicit def formFiller[Q <: QuestionGroup](form: Form[Q])(implicit classTag: ClassTag[Q]) = new {
    def fill(qi: QuestionGroup.Identifier)(implicit claim: Claim): Form[Q] = claim.questionGroup(qi) match {
      case Some(q: Q) => form.fill(q)
      case _ => form
    }
  }

  implicit def defaultResultToLeft(result: Result):Left[play.api.mvc.Result,(models.domain.Claim, play.api.mvc.Result)] = Left(result)

  implicit def claimAndResultToRight(claimingResult: ClaimResult):Right[play.api.mvc.Result,(models.domain.Claim, play.api.mvc.Result)] = Right(claimingResult)

  protected def newInstance(newuuid:String = randomUUID.toString): Claim = new Claim(cacheKey, uuid = newuuid) with FullClaim

  def copyInstance(claim: Claim): Claim = new Claim(claim.key, claim.sections, claim.created, claim.lang, claim.uuid, claim.transactionId, claim.previouslySavedClaim)(claim.navigation) with FullClaim

  private def keyAndExpiration(r: Request[AnyContent]): (String, Int) = {
    r.session.get(cacheKey).getOrElse("") -> expiration
  }

  private def refererAndHost(r: Request[AnyContent]): (String, String) = {
    r.headers.get("Referer").getOrElse("No Referer in header") -> r.headers.get("Host").getOrElse("No Host in header")
  }

  /**
   * Tries to get the claim of change of circs from the cache.
   * @param request the http request that has the session with uuid of claim which is the key used by cache.
   * @return None if could not find claim/CoCs. Some(claim) is could find it.
   */
  def fromCache(request: Request[AnyContent]): Option[Claim] = {
    val key = keyAndExpiration(request)._1
    if (key.isEmpty) {
      // Log an error if session empty or with no cacheKey entry so we know it is not a cache but a cookie issue.
      Logger.error(s"Did not receive Session information for a $cacheKey for ${request.method} url path ${request.path} and agent ${request.headers.get("User-Agent").getOrElse("Unknown agent")}. Probably a cookie issue: ${request.cookies.filterNot( _.name.startsWith("_"))}.")
      None
    } else Cache.getAs[Claim](key)
  }

  /**
   * Called when starting a new claim. Overwrites CSRF token and Version in case user had old cookies.
   */
  def newClaim(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult]): Action[AnyContent] = Action {
    request => {
      implicit val r = request

      recordMeasurements()

      if (request.getQueryString("changing").getOrElse("false") == "false") {
        // Delete any old data to avoid somebody getting access to session left by somebody else
        val key = keyAndExpiration(request)._1
        if (!key.isEmpty) Cache.remove(key)
        // Start with new claim
        val claim = newInstance()
        Logger.info(s"New ${claim.key} ${claim.uuid} cached.")
        // Cookies need to be changed BEFORE session, session is within cookies.
        def tofilter(theCookie: Cookie): Boolean = { theCookie.name == CachedClaim.C3VERSION || theCookie.name == getProperty("session.cookieName","PLAY_SESSION")}
        // Added C3Version for full Zero downtime
        withHeaders(action(claim, r, bestLang)(f))
          .withCookies(r.cookies.toSeq.filterNot(tofilter) :+ Cookie(CachedClaim.C3VERSION, CachedClaim.C3VERSION_VALUE): _*)
          .withSession(claim.key -> claim.uuid)
          .discardingCookies(DiscardingCookie("application-finished"))
      } else {
        val key = request.session.get(cacheKey).getOrElse(throw new RuntimeException("I expected a key in the session!"))
        Logger.info(s"Changing $cacheKey - $key")
        val claim = Cache.getAs[Claim](key).getOrElse(throw new RuntimeException("I expected a claim in the cache!"))
        val result = originCheck(action(claim, r, claim.lang.getOrElse(bestLang))(f))
        result
      }
    }
  }

  implicit def actionWrapper(action:Action[AnyContent]) = new {

    def withPreview():Action[AnyContent] = Action.async(action.parser){ request =>
      Logger.debug("actionWrapper")
      action(request).map { result =>
        result.header.status -> fromCache(request) match {
          case (play.api.http.Status.SEE_OTHER,Some(claim)) if claim.navigation.beenInPreview => Redirect(controllers.preview.routes.Preview.present())
          case _ => result
        }
      }
    }

    // If the curried function returns true, this action will be redirected to preview if we have been there previously
    // The data feeded to the curried function is the current submitted value of the claim, and the previously saved claim the moment we visited preview page.
    def withPreviewConditionally[T <: QuestionGroup](t:((Option[T],T)) => Boolean)(implicit classTag:ClassTag[T]):Action[AnyContent] = Action.async(action.parser){ request =>

      def getParams[E <: T](claim:Claim)(implicit classTag:ClassTag[E]):(Option[E],E) = {
        claim.previouslySavedClaim.map(_.questionGroup(classTag.runtimeClass).getOrElse(None)).asInstanceOf[Option[E]] -> claim.questionGroup(classTag.runtimeClass).get.asInstanceOf[E]
      }

      action(request).map{ result =>
        result.header.status -> fromCache(request) match {
          case (play.api.http.Status.SEE_OTHER,Some(claim)) if claim.navigation.beenInPreview && t(getParams(claim))=> Redirect(controllers.preview.routes.Preview.present())
          case _ => result
        }
      }
    }
  }


  def claiming(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult]): Action[AnyContent] = Action {
    implicit request => {
      val (referer, _) = refererAndHost(request)

      enforceAlreadyFinishedRedirection(request,
        originCheck(
          fromCache(request) match {

            case Some(claim) =>  claimingWithClaim(f, request, claim)

            case None => claimingWithoutClaim(f, request)
          }
        )
      )

    }
  }

  /**
   * Here we are displaying an error page at the start of the application (after the first page) if the cookies are disabled when the user
   * is applying for a claim
   * @param f
   * @return
   */
  def claimingWithCookie(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult]): Action[AnyContent] = Action {
    implicit request => {
      enforceAlreadyFinishedRedirection(request,
        originCheck(
          fromCache(request) match {
  
            case Some(claim) =>  claimingWithClaim(f, request, claim)
  
            case None if Play.isTest => claimingWithoutClaim(f, request)
  
            case None  => Redirect(errorPageCookie)
          })
      )
    }
  }

 protected def claimingWithCondition(isNotValid: Claim => Boolean, noClaimInCache: =>Result)  (f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult])(implicit request: Request[AnyContent]) = {
   originCheck(
     fromCache(request) match {
       case Some(claim) if !Play.isTest && isNotValid(claim) =>
         Logger.error(s"$cacheKey - cache: ${keyAndExpiration(request)._1} lost the claim date and claimant details")
         Redirect(errorPageBrowserBackButton)

       case Some(claim) =>  claimingWithClaim(f, request, claim)

       case None if Play.isTest => claimingWithoutClaim(f, request)

       case None =>  noClaimInCache
     })

 }

  /**
   * Here we are checking the mandatory fields for Circs : full name and nino on the about you page and
   * for claim : firstName, lastName and nino of the claimant. This check is done to ensure that the
   * mandatory fields are always present when we submit the claim or circs. An error page is displayed
   * if the mandatory fields are missing. We found the mandatory fields missing when the user uses the
   * browser's back and forward buttons instead of the ones provided by the application.
   * @param f
   * @return
   */
  def claimingWithCheck(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult]) = claimingWithDataCheck(ifMandatoryFieldsMissing)(f)

  protected def claimingWithDataCheck(isNotValid: Claim => Boolean)  (f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult]): Action[AnyContent] = Action {
    implicit request =>
      claimingWithCondition(isNotValid,claimingWithoutClaim(f,request))(f)
  }
  private def ifMandatoryFieldsMissing (claim:Claim):Boolean = {
    (claim.questionGroup[ClaimDate] match {
      case Some(null) => true
      case None => true
      case _ => false

    }) ||
    (claim.questionGroup[YourDetails] match {
      case None => true
      case Some(YourDetails(_,firstName,_,surname,nino,_)) if firstName.isEmpty || surname.isEmpty || nino.nino.isEmpty => true
      case _ => false
    })
  }

  private def claimingWithClaim(f: (Claim) => (Request[AnyContent]) => (Lang) => Either[Result, (Claim, Result)], request: Request[AnyContent], claim: Claim): Result = {
    Logger.debug(s"claimingWithClaim - ${claim.key} ${claim.uuid}")
    implicit val r = request
    val key = keyAndExpiration(request)._1
    if (key != claim.uuid) Logger.error(s"claimingWithClaim - Claim uuid ${claim.uuid} does not match cache key $key.")
    val lang = claim.lang.getOrElse(bestLang)
    action(copyInstance(claim), request, lang)(f).withSession(claim.key -> claim.uuid)
  }

  private def claimingWithoutClaim(f: (Claim) => (Request[AnyContent]) => (Lang) => Either[Result, (Claim, Result)], request: Request[AnyContent]): Result = {
    if (Play.isTest) {
      implicit val r = request
      val claim = newInstance()
      Cache.set(claim.uuid, claim, expiration) // place an empty claim in the cache to satisfy tests
      // Because a test can start at any point of the process we have to be sure the claim uuid is in the session.
      action(claim, request, bestLang)(f).withSession(claim.key -> claim.uuid)
    } else {
      val uuid = keyAndExpiration(request)._1
      Logger.debug(s"claimingWithoutClaim - uuid $uuid")
      if (uuid.isEmpty) {
        Redirect(errorPage)
      } else {
        Logger.warn(s"$cacheKey - $uuid timeout")
        Redirect(timeoutPage)
      }
    }
  }



  def ending(f: Claim => Request[AnyContent] => Lang => Result): Action[AnyContent] = Action {
    request => {
      implicit val r = request
      val theDomain = Session.domain

      fromCache(request) match {
        case Some(claim) =>
          val lang = claim.lang.getOrElse(bestLang)
          // reaching end of process - thank you page so we delete claim for security reasons and free memory
          Cache.remove(claim.uuid)
          originCheck(f(claim)(request)(lang)).discardingCookies(DiscardingCookie(csrfCookieName, secure = csrfSecure, domain=theDomain),DiscardingCookie(CachedClaim.C3VERSION)).withNewSession.withCookies(Cookie("application-finished","true"))
        case _ => {
          enforceAlreadyFinishedRedirection(request,
            originCheck(f(Claim())(request)(bestLang)).discardingCookies(DiscardingCookie(csrfCookieName, secure = csrfSecure, domain=theDomain), DiscardingCookie(CachedClaim.C3VERSION)).withNewSession.withCookies(Cookie("application-finished","true"))
          )
        }
      }
    }
  }

  def endingOnError(f: Claim => Request[AnyContent] => Lang => Result): Action[AnyContent] = Action {
    request => {
      implicit val r = request
      originCheck(f(Claim())(request)(bestLang))
    }
  }

  def claimingWithCheckInIteration(f: (IterationID) => Claim => Request[AnyContent] => Lang => Either[Result, ClaimResult]) = Action.async {
    request =>
      claimingWithCheck(f(request.body.asFormUrlEncoded.getOrElse(Map("" -> Seq(""))).getOrElse("iterationID", Seq("Missing IterationID at request"))(0)))(request)
  }

  private def action(claim: Claim, request: Request[AnyContent], lang: Lang)(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult]): Result = {
    val (key, expiration) = keyAndExpiration(request)
    if (!key.isEmpty && key != claim.uuid) Logger.warn(s"action - Claim uuid ${claim.uuid} does not match cache key $key. Can happen if action new claim and user reuses session. Will disregard session key and use uuid.")

    f(claim)(request)(lang) match {
      case Left(r: Result) => r
      case Right((c: Claim, r: Result)) =>
        Cache.set(claim.uuid, c, expiration)
        r
    }
  }

  private def enforceAlreadyFinishedRedirection(request:Request[AnyContent],otherwise: =>Result):Result = {

    if (request.cookies.exists{
      case Cookie("application-finished","true",_,_,_,_,_) => println("Found cookie! Redirecting");true
      case _ => false
    }) Redirect(controllers.routes.Application.backButtonPage)
    else otherwise

  }

  private def originCheck(action: => Result)(implicit request: Request[AnyContent]) = {
    val (referer, host) = refererAndHost(request)
    val sameHostCheck = referer.contains(host)

    Logger.info(s"Redirect $redirectEnforced. Same host $sameHostCheck")
    if (sameHostCheck) {
      withHeaders(action)
    } else {
      if (redirectEnforced) {
        Logger.warn(s"HTTP Referrer : $referer. Conf Referrer : $startPage. HTTP Host : $host")
        MovedPermanently(startPage)
      } else {
        withHeaders(action)
      }
    }
  }

  private def withHeaders(result: Result): Result = {
    result.withHeaders(CACHE_CONTROL -> "must-revalidate,no-cache,no-store","X-Frame-Options" -> "SAMEORIGIN")
  }

  private def bestLang()(implicit request: Request[AnyContent]) = {
    val implementedLangs = getProperty("application.langs", defaultLang)
    val listOfPossibleLangs = request.acceptLanguages.flatMap(aL => implementedLangs.split(",").toList.filter(iL => iL == aL.code))

    if (listOfPossibleLangs.size > 0)
      Lang(listOfPossibleLangs.head)
    else
      Lang(defaultLang)
  }

  private def recordMeasurements() = {
    Histograms.recordCacheSize(Try(CacheManager.getInstance().getCache("play").getKeysWithExpiryCheck.size()).getOrElse(0))
  }
}