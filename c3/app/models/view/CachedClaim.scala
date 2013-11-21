package models.view

import java.util.UUID._
import scala.language.implicitConversions
import scala.reflect.ClassTag
import play.api.Play.current
import play.api.mvc.{Action, AnyContent, Request, Result}
import play.api.data.Form
import play.Configuration
import play.api.cache.Cache
import play.api.{Logger, Play}
import play.api.mvc.Results._
import play.api.http.HeaderNames._
import models.domain._
import controllers.routes

object CachedClaim {
  val key = "claim"
}

trait CachedClaim {
  type ClaimResult = (Claim, Result)

  type JobID = String

  val cacheKey = CachedClaim.key

  val timeout = routes.Application.timeout()

  implicit def formFiller[Q <: QuestionGroup](form: Form[Q])(implicit classTag: ClassTag[Q]) = new {
    def fill(qi: QuestionGroup.Identifier)(implicit claim: Claim): Form[Q] = claim.questionGroup(qi) match {
      case Some(q: Q) => form.fill(q)
      case _ => form
    }
  }

  implicit def defaultResultToLeft(result: Result) = Left(result)

  implicit def claimAndResultToRight(claimingResult: ClaimResult) = Right(claimingResult)

  def newInstance: Claim = new Claim(cacheKey) with FullClaim

  def copyInstance(claim: Claim): Claim = new Claim(claim.key, claim.sections, claim.created)(claim.navigation) with FullClaim

  def keyAndExpiration(r: Request[AnyContent]): (String, Int) = {
    r.session.get(cacheKey).getOrElse(randomUUID.toString) -> Configuration.root().getInt("cache.expiry", 3600)
  }

  def fromCache(request:Request[AnyContent]): Option[Claim] = {
    val (key, _) = keyAndExpiration(request)

    Cache.getAs[Claim](key)
  }

  def newClaim(f: (Claim) => Request[AnyContent] => Either[Result, ClaimResult]): Action[AnyContent] = Action {
    request => {
      val (key, _) = keyAndExpiration(request)

      val claim = if (request.getQueryString("changing").getOrElse("false") == "false") {
        Logger.info(s"Starting new $cacheKey")
        newInstance
      }
      else Cache.getAs[Claim](key).getOrElse(newInstance)

      action(claim, request)(f)
    }
  }

  def claiming(f: (Claim) => Request[AnyContent] => Either[Result, ClaimResult]): Action[AnyContent] = Action {
    request => {

      fromCache(request) match {
        case Some(claim) => action(copyInstance(claim), request)(f)

        case None =>
          if (Play.isTest) {
            val (key, expiration) = keyAndExpiration(request)
            val claim = newInstance
            Cache.set(key, claim, expiration) // place an empty claim in the cache to satisfy tests
            action(claim, request)(f)
          } else {
            Logger.info(s"$cacheKey timeout")
            Redirect(timeout).withHeaders("X-Frame-Options" -> "SAMEORIGIN") // stop click jacking
          }
      }
    }
  }

  def action(claim: Claim, request: Request[AnyContent])(f: (Claim) => Request[AnyContent] => Either[Result, ClaimResult]): Result = {
    val (key, expiration) = keyAndExpiration(request)

    f(claim)(request) match {
      case Left(r: Result) =>
        r.withSession(claim.key -> key)
          .withHeaders(CACHE_CONTROL -> "no-cache, no-store")
          .withHeaders("X-Frame-Options" -> "SAMEORIGIN") // stop click jacking

      case Right((c: Claim, r: Result)) => {
        Cache.set(key, c, expiration)

        r.withSession(claim.key -> key)
          .withHeaders(CACHE_CONTROL -> "no-cache, no-store")
          .withHeaders("X-Frame-Options" -> "SAMEORIGIN") // stop click jacking
      }
    }
  }

  def claimingInJob(f: (JobID) => Claim => Request[AnyContent] => Either[Result, ClaimResult]) = Action { request =>
    claiming(f(request.body.asFormUrlEncoded.getOrElse(Map("" -> Seq(""))).get("jobID").getOrElse(Seq("Missing JobID at request"))(0)))(request)
  }
}