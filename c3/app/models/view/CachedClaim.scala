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
import scala.concurrent.{ExecutionContext, Future}
import models.domain.Claim
import ExecutionContext.Implicits.global
import models.view.CachedClaim.ClaimResult
import monitoring.Histograms
import scala.util.Try
import net.sf.ehcache.CacheManager

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

  def copyInstance(claim: Claim): Claim = new Claim(claim.key, claim.sections, claim.created, claim.lang, claim.uuid,claim.transactionId)(claim.navigation) with FullClaim

  private def keyAndExpiration(r: Request[AnyContent]): (String, Int) = {
    r.session.get(cacheKey).getOrElse("") -> getProperty("cache.expiry", 3600)  //.getOrElse(randomUUID.toString)
  }

  def refererAndHost(r: Request[AnyContent]): (String, String) = {
    r.headers.get("Referer").getOrElse("No Referer in header") -> r.headers.get("Host").getOrElse("No Host in header")
  }

  def fromCache(request: Request[AnyContent]): Option[Claim] = {
    val (key, _) = keyAndExpiration(request)
    if (key.isEmpty) None else Cache.getAs[Claim](key)
  }

  def recordMeasurements() = {
    Histograms.recordCacheSize(Try(CacheManager.getInstance().getCache("play").getKeysWithExpiryCheck.size()).getOrElse(0))
  }


  def newClaim(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult]): Action[AnyContent] = Action {
    request => {
      // Need to overwrite CSRF and Sessions because this could be an user that has an old cookie with CSRF and session
      val token = DwpCSRF.SignedTokenProvider.generateToken
      implicit val r = Request[AnyContent](request.copy(tags = request.tags.filterNot(_._1 == DwpCSRF.Token.RequestTag) + (DwpCSRF.Token.RequestTag -> token)), request.body )

      recordMeasurements()

      if (request.getQueryString("changing").getOrElse("false") == "false") {
        val claim = newInstance()
        val result = withHeaders(action(claim, r, bestLang)(f))
        Logger.info(s"New ${claim.key} ${claim.uuid} cached. Token ${token}")
        result.withSession((claim.key -> claim.uuid) ,(DwpCSRF.TokenName -> token))
      }
      else {
        val key = request.session.get(cacheKey).getOrElse(throw new RuntimeException("I expected a key in the session!"))
        Logger.info(s"Changing $cacheKey - $key")
        val claim = Cache.getAs[Claim](key).getOrElse(throw new RuntimeException("I expected a claim in the cache!"))
        val result = originCheck(action(claim, r, claim.lang.getOrElse(bestLang))(f))
        result.withSession(r.session - (DwpCSRF.TokenName) + (DwpCSRF.TokenName -> token))
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

  def submitting(f: (Claim) => Request[AnyContent] => Lang => Future[SimpleResult]) = Action.async {
    request => {
      val (referer, host) = refererAndHost(request)
      implicit val r = request

      def doSubmit() = {
        fromCache(request) match {
          case Some(claim) =>
            Logger.debug(s"submitting - ${claim.key} ${claim.uuid}")
            val (key, _) = keyAndExpiration(request)
            if (key != claim.uuid) Logger.error(s"submitting - Claim uuid ${claim.uuid} does not match cache key $key")
            val lang = claim.lang.getOrElse(bestLang)
            f(copyInstance(claim))(request)(lang).map(res => res.withSession(claim.key -> key))
          case None =>
            Logger.warn(s"Cache for $cacheKey - ${keyAndExpiration(request)._1} timeout")
            Future(Redirect(timeoutPage))
        }
      }

      if (sameHostCheck) {
        doSubmit()
      } else {
        if (getProperty("enforceRedirect", default = true)) {
          Logger.warn(s"HTTP Referrer : $referer. Conf Referrer : $startPage. HTTP Host : $host")
          Future(MovedPermanently(startPage))
        } else {
          doSubmit()
        }
      }.map(res => res)
    }
  }

  def ending(f: Claim => Request[AnyContent] => Lang => Result): Action[AnyContent] = Action {
    request => {
      implicit val r = request
      implicit val cl = new Claim()
      fromCache(request) match {
        case Some(claim) =>
          val lang = claim.lang.getOrElse(bestLang)
          originCheck(f(claim)(request)(lang)).withNewSession
        case _ => originCheck(f(cl)(request)(bestLang)).withNewSession
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
      case Left(r: Result) => {
        r
      }
      case Right((c: Claim, r: Result)) => {
        Cache.set(claim.uuid, c, expiration)
        r
      }
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

  def redirect: Boolean = {
    getProperty("enforceRedirect", default = true)
  }

  private def withHeaders(result: Result): Result = {
    result
      .withHeaders(CACHE_CONTROL -> "no-cache, no-store")
      .withHeaders("X-Frame-Options" -> "SAMEORIGIN") // stop click jacking
  }

  def bestLang()(implicit request: Request[AnyContent]) = {
    val implementedLangs = getProperty("application.langs", defaultLang)

    val listOfPossibleLangs = request.acceptLanguages.flatMap(aL => implementedLangs.split(",").toList.filter(iL => iL == aL.code))

    if (listOfPossibleLangs.size > 0)
      Lang(listOfPossibleLangs.head)
    else
      Lang(defaultLang)
  }
}