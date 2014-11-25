package models.view

import app.ConfigProperties._
import java.util.UUID._
import utils.csrf.DwpCSRF

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
import ExecutionContext.Implicits.global
import models.view.CachedClaim.ClaimResult
import monitoring.Histograms
import scala.util.Try
import net.sf.ehcache.CacheManager
import ExecutionContext.Implicits.global

object CachedClaim {
  val missingRefererConfig = "Referer not set in config"
  val key = "claim"
  type ClaimResult = (Claim, Result)
}

/**
 * The Unique ID use to identified a claim/circs in the cache is a UUID, stored in the claim/circs and set in the request session (cookie).
 */
trait CachedClaim {

  type JobID = String

  val cacheKey = CachedClaim.key

  val startPage: String = getProperty("claim.start.page", "/allowance/benefits")

  val timeoutPage = routes.ClaimEnding.timeout()

  val errorPage = routes.ClaimEnding.error()

  val defaultLang = "en"

  implicit def formFiller[Q <: QuestionGroup](form: Form[Q])(implicit classTag: ClassTag[Q]) = new {
    def fill(qi: QuestionGroup.Identifier)(implicit claim: Claim): Form[Q] = claim.questionGroup(qi) match {
      case Some(q: Q) => form.fill(q)
      case _ => form
    }
  }

  implicit def defaultResultToLeft(result: Result) = Left(result)

  implicit def claimAndResultToRight(claimingResult: ClaimResult) = Right(claimingResult)

  def newInstance(newuuid:String = randomUUID.toString): Claim = new Claim(cacheKey, uuid = newuuid) with FullClaim

  def copyInstance(claim: Claim): Claim = new Claim(claim.key, claim.sections, claim.created, claim.lang, claim.uuid, claim.transactionId, claim.previouslySavedClaim)(claim.navigation) with FullClaim

  private def keyAndExpiration(r: Request[AnyContent]): (String, Int) = {
    r.session.get(cacheKey).getOrElse("") -> getProperty("cache.expiry", 3600)  //.getOrElse(randomUUID.toString)
  }

  private def refererAndHost(r: Request[AnyContent]): (String, String) = {
    r.headers.get("Referer").getOrElse("No Referer in header") -> r.headers.get("Host").getOrElse("No Host in header")
  }

  def fromCache(request: Request[AnyContent]): Option[Claim] = {
    val (key, _) = keyAndExpiration(request)
    if (key.isEmpty) {
      // Log an error if session empty or with no cacheKey entry so we know it is not a cache but a cookie issue.
      Logger.error(s"Did not receive Session information for ${cacheKey} for url path ${request.path}. Probably a cookie issue: ${request.cookies.filterNot( _.name.startsWith("_"))}.")
      None
    } else Cache.getAs[Claim](key)
  }

  def recordMeasurements() = {
    Histograms.recordCacheSize(Try(CacheManager.getInstance().getCache("play").getKeysWithExpiryCheck.size()).getOrElse(0))
  }


  protected val C3VERSION = "C3Version"
  protected val C3VERSION_VALUE = "0.52"

  /**
   * Called when starting a new claim. Overwrites CSRF token and Version in case user had old cookies.
   */
  def newClaim(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult]): Action[AnyContent] = Action {
    request => {
      implicit val r = request

      recordMeasurements()

      if (request.getQueryString("changing").getOrElse("false") == "false") {
        // Delete any old data to avoid somebody getting access to session left by somebody else
        val (key, _) = keyAndExpiration(request)
        if (!key.isEmpty) Cache.remove(key)
        // Start with new claim
        val claim = newInstance()
        val result = withHeaders(action(claim, r, bestLang)(f))
        Logger.info(s"New ${claim.key} ${claim.uuid} cached.")
        // Cookies need to be changed BEFORE session, session is within cookies.
        def tofilter(theCookie: Cookie): Boolean = { theCookie.name == C3VERSION || theCookie.name == getProperty("session.cookieName","PLAY_SESSION")}
        // Added C3Version for full Zero downtime
        result.withCookies(r.cookies.toSeq.filterNot(tofilter) :+ Cookie(C3VERSION, C3VERSION_VALUE): _*).withSession((claim.key -> claim.uuid))
      }
      else {
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

      action(request).map{ result =>
        result.header.status -> fromCache(request) match {
          case (play.api.http.Status.SEE_OTHER,Some(claim)) if claim.navigation.beenInPreview => Redirect(controllers.preview.routes.Preview.present)
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
          case (play.api.http.Status.SEE_OTHER,Some(claim)) if claim.navigation.beenInPreview && t(getParams(claim))=> Redirect(controllers.preview.routes.Preview.present)
          case _ => result
        }
      }
    }


  }


  def claiming(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult]): Action[AnyContent] = Action {
    request => {
      implicit val r = request
      originCheck(
        fromCache(request) match {
          case Some(claim) =>
            Logger.debug(s"claiming - ${claim.key} ${claim.uuid}")
            val lang = claim.lang.getOrElse(bestLang)
            action(copyInstance(claim), request, lang)(f)

          case None =>
            if (Play.isTest) {
              Logger.debug(s"claiming - None and test")
              val (_, expiration) = keyAndExpiration(request)
              val claim = newInstance()
              Cache.set(claim.uuid, claim, expiration) // place an empty claim in the cache to satisfy tests
              // Because a test can start at any point of the process we have to be sure the claim uuid is in the session.
              action(claim, request, bestLang)(f).withSession(claim.key -> claim.uuid)
            } else {
              Logger.warn(s"Cache $cacheKey - ${keyAndExpiration(request)._1} timeout")
              Redirect(timeoutPage)
            }
        })
    }
  }

  def claimingWithCheck(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult]): Action[AnyContent] = Action {
    request => {
      implicit val r = request
      originCheck(
        fromCache(request) match {
          case Some(claim) if !Play.isTest && (
            !claim.questionGroup[ClaimDate].isDefined ||
              claim.questionGroup[ClaimDate].isDefined &&
                claim.questionGroup[ClaimDate].get.dateOfClaim == null) =>
            Logger.error(s"$cacheKey - cache: ${keyAndExpiration(request)._1} lost the claim date")
            Redirect(timeoutPage)
          case Some(claim) =>
            Logger.debug(s"claimingWithCheck - ${claim.key} ${claim.uuid}")
            val key = keyAndExpiration(request)._1
            if (key != claim.uuid) Logger.error(s"claimingWithCheck - Claim uuid ${claim.uuid} does not match cache key $key.")
            val lang = claim.lang.getOrElse(bestLang)
            if (Play.isTest) {
              // Because a test can start at any point of the process we have to be sure the claim uuid is in the session.
              action (copyInstance (claim), request, lang)(f).withSession(claim.key -> claim.uuid)
            } else {
              // We do not need to add claim uuid in session since done by first step of process (newClaim).
              action (copyInstance (claim), request, lang)(f)
            }
          case None =>
            if (Play.isTest) {
              Logger.debug(s"claimingWithCheck - None and test")
              val (_, expiration) = keyAndExpiration(request)
              val claim = newInstance()
              Cache.set(claim.uuid, claim, expiration) // place an empty claim in the cache to satisfy tests
              action(claim, request, bestLang)(f).withSession(claim.key -> claim.uuid)
            } else {
              Logger.warn(s"$cacheKey - ${keyAndExpiration(request)._1} timeout")
              Redirect(timeoutPage)
            }
        })
    }
  }

  def ending(f: Claim => Request[AnyContent] => Lang => Result): Action[AnyContent] = Action {
    request => {
      implicit val r = request
      implicit val cl = new Claim()
      val csrfCookieName = getProperty("csrf.cookie.name","csrf")
      val csrfSecure = getProperty("csrf.cookie.secure",getProperty("session.secure",false))
      val theDomain = Play.current.configuration.getString("session.domain")
      fromCache(request) match {
        case Some(claim) =>
          val lang = claim.lang.getOrElse(bestLang)
          originCheck(f(claim)(request)(lang)).discardingCookies(DiscardingCookie(csrfCookieName, secure = csrfSecure, domain=theDomain),DiscardingCookie(C3VERSION)).withNewSession
        case _ => originCheck(f(cl)(request)(bestLang)).discardingCookies(DiscardingCookie(csrfCookieName, secure = csrfSecure, domain=theDomain), DiscardingCookie(C3VERSION)).withNewSession
      }

    }
  }

  def claimingInJob(f: (JobID) => Claim => Request[AnyContent] => Lang => Either[Result, ClaimResult]) = Action.async {
    request =>
      claiming(f(request.body.asFormUrlEncoded.getOrElse(Map("" -> Seq(""))).get("jobID").getOrElse(Seq("Missing JobID at request"))(0)))(request)
  }

  def claimingWithCheckInJob(f: (JobID) => Claim => Request[AnyContent] => Lang => Either[Result, ClaimResult]) = Action.async {
    request =>
      claimingWithCheck(f(request.body.asFormUrlEncoded.getOrElse(Map("" -> Seq(""))).get("jobID").getOrElse(Seq("Missing JobID at request"))(0)))(request)
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

  private def sameHostCheck()(implicit request: Request[AnyContent]) = {
    val (referer, host) = refererAndHost(request)
    referer.contains(host)
  }

  private def originCheck(action: => Result)(implicit request: Request[AnyContent]) = {
    val (referer, host) = refererAndHost(request)

    Logger.info(s"Redirect $redirect $sameHostCheck")
    if (sameHostCheck) {
      withHeaders(action)
    } else {
      if (redirect) {
        Logger.warn(s"HTTP Referrer : $referer. Conf Referrer : $startPage. HTTP Host : $host")
        MovedPermanently(startPage)
      } else {
        withHeaders(action)
      }
    }
  }

  private def redirect: Boolean = {
    getProperty("enforceRedirect", default = true)
  }

  private def withHeaders(result: Result): Result = {
    result
      .withHeaders(CACHE_CONTROL -> "must-revalidate,no-cache,no-store")
      .withHeaders("X-Frame-Options" -> "SAMEORIGIN") // stop click jacking
  }

  private def bestLang()(implicit request: Request[AnyContent]) = {
    val implementedLangs = getProperty("application.langs", defaultLang)

    val listOfPossibleLangs = request.acceptLanguages.flatMap(aL => implementedLangs.split(",").toList.filter(iL => iL == aL.code))

    if (listOfPossibleLangs.size > 0)
      Lang(listOfPossibleLangs.head)
    else
      Lang(defaultLang)
  }
}