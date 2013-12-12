package models.view

import app.ConfigProperties._
import java.util.UUID._
import scala.language.implicitConversions
import scala.reflect.ClassTag
import play.api.Play.current
import play.api.mvc.{Action, AnyContent, Request, Result}
import play.api.data.Form
import play.api.cache.Cache
import play.api.{Logger, Play}
import play.api.mvc.Results._
import play.api.http.HeaderNames._
import models.domain._
import controllers.routes
import models.domain.Claim
import scala.Some
import scala.util.Try
import net.sf.ehcache.CacheManager
import play.api.i18n.Lang

object CachedClaim {
  val missingRefererConfig = "Referer not set in config"
  val key = "claim"
}

trait CachedClaim {
  type ClaimResult = (Claim, Result)

  type JobID = String

  val cacheKey = CachedClaim.key

  val redirect = getProperty("enforceRedirect", default = false)

  val expectedReferer = getProperty("claim.referer", default = CachedClaim.missingRefererConfig)

  val timeoutPage = routes.ClaimEnding.timeout()

  val errorPage = routes.CircsEnding.error()


  implicit def formFiller[Q <: QuestionGroup](form: Form[Q])(implicit classTag: ClassTag[Q]) = new {
    def fill(qi: QuestionGroup.Identifier)(implicit claim: Claim): Form[Q] = claim.questionGroup(qi) match {
      case Some(q: Q) => form.fill(q)
      case _ => form
    }
  }

  implicit def defaultResultToLeft(result: Result) = Left(result)

  implicit def claimAndResultToRight(claimingResult: ClaimResult) = Right(claimingResult)

  def newInstance: Claim = new Claim(cacheKey) with FullClaim

  def copyInstance(claim: Claim): Claim = new Claim(claim.key, claim.sections, claim.created, claim.lang)(claim.navigation) with FullClaim

  def keyAndExpiration(r: Request[AnyContent]): (String, Int) = {
    r.session.get(cacheKey).getOrElse(randomUUID.toString) ->  getProperty("cache.expiry", 3600)
  }

  def refererAndHost(r: Request[AnyContent]): (String, String) = {
    r.headers.get("Referer").getOrElse("No Referer in header") -> r.headers.get("Host").getOrElse("No Host in header")
  }

  def fromCache(request:Request[AnyContent]): Option[Claim] = {
    val (key, _) = keyAndExpiration(request)

    Cache.getAs[Claim](key)
  }

  def newClaim(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult]): Action[AnyContent] = Action {
    request => {
      implicit val r = request

      if (request.getQueryString("changing").getOrElse("false") == "false") {
        //TODO Replace default cases in newClaim and claiming for default lang case
        originCheck(refererCheck, action(newInstance, request, Lang("en"))(f))
      }
      else {
        Logger.info(s"Changing $cacheKey")
        val key = request.session.get(cacheKey).getOrElse(throw new RuntimeException("I expected a key in the session!"))
        val claim = Cache.getAs[Claim](key).getOrElse(throw new RuntimeException("I expected a claim in the cache!"))
        originCheck(sameHostCheck, action(claim, request, claim.lang.getOrElse(Lang("en")))(f))
      }
    }
  }

  def claiming(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult]): Action[AnyContent] = Action {
    request => {
      implicit val r = request
      originCheck(sameHostCheck,
        fromCache(request) match {
          case Some(claim) => {
            val lang = claim.lang.getOrElse(Lang("en"))
            action(copyInstance(claim), request, lang)(f)
          }

          case None =>
            if (Play.isTest) {
              val (key, expiration) = keyAndExpiration(request)
              val claim = newInstance
              Cache.set(key, claim, expiration) // place an empty claim in the cache to satisfy tests
              action(claim, request, Lang("en"))(f)
            } else {
              Logger.info(s"$cacheKey timeout")
              Redirect(timeoutPage)
            }
        })
    }
  }

  def ending(f: => Result): Action[AnyContent] = Action {
    request => {
      implicit val r = request
      val sessionCount = Try(CacheManager.getInstance().getCache("play").getKeys.size()).getOrElse(0)
      Logger.info(s"sessionCount : $sessionCount")
      originCheck(sameHostCheck, f).withNewSession
    }
  }

  def claimingInJob(f: (JobID) => Claim => Request[AnyContent] => Lang => Either[Result, ClaimResult]) = Action { request =>
    claiming(f(request.body.asFormUrlEncoded.getOrElse(Map("" -> Seq(""))).get("jobID").getOrElse(Seq("Missing JobID at request"))(0)))(request)
  }

  private def action(claim: Claim, request: Request[AnyContent], lang: Lang)(f: (Claim) => Request[AnyContent] => Lang => Either[Result, ClaimResult]): Result = {

    val (key, expiration) = keyAndExpiration(request)

    f(claim)(request)(lang) match {
      case Left(r: Result) =>
        r.withSession(claim.key -> key)
      case Right((c: Claim, r: Result)) =>
        Cache.set(key, c, expiration)
        r.withSession(claim.key -> key)
    }
  }

  private def sameHostCheck()(implicit request: Request[AnyContent]) = {
    val (referer, host) = refererAndHost(request)
    referer.contains(host)
  }

  private def refererCheck()(implicit request: Request[AnyContent]) = {
    val (referer, _) = refererAndHost(request)
    referer.startsWith(expectedReferer)
  }

  private def originCheck(doCheck: => Boolean, action: => Result)(implicit request: Request[AnyContent])  = {
    val (referer, host) = refererAndHost(request)

    if (doCheck) {
      withHeaders(action)
    } else {
      if (redirect) {
        Logger.warn(s"HTTP Referer : $referer")
        Logger.warn(s"Conf Referer : $expectedReferer")
        Logger.warn(s"HTTP Host : $host")
        Redirect(expectedReferer)
      } else {
        withHeaders(action)
      }
    }
  }

  private def withHeaders(result:Result) : Result = {
    result
      .withHeaders(CACHE_CONTROL -> "no-cache, no-store")
      .withHeaders("X-Frame-Options" -> "SAMEORIGIN") // stop click jacking
  }


}