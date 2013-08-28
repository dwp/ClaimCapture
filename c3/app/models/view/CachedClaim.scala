package models.view

import language.implicitConversions
import reflect.ClassTag
import play.api.mvc._
import play.api.cache.Cache
import models.domain.{Claim, QuestionGroup}
import play.Configuration
import play.api.Play._
import play.api.mvc.Results.Redirect
import play.api.data.Form
import play.api.Play
import play.api.Logger
import play.api.http.HeaderNames._
import java.util.UUID._

object CachedClaim {
  val claimKey = "claim"
}

trait CachedClaim {
  type ClaimResult = (Claim, Result)

  implicit def formFiller[Q <: QuestionGroup](form: Form[Q])(implicit classTag: ClassTag[Q]) = new {
    def fill(qi: QuestionGroup.Identifier)(implicit claim: Claim): Form[Q] = {
      claim.questionGroup(qi) match {
        case Some(q: Q) => form.fill(q)
        case _ => form
      }
    }
  }

  implicit def defaultResultToLeft(result: Result) = Left(result)

  implicit def claimAndResultToRight(claimingResult: ClaimResult) = Right(claimingResult)

  def keyAndExpiration(r: Request[AnyContent]): (String, Int) = {
    r.session.get(CachedClaim.claimKey).getOrElse(randomUUID.toString) -> Configuration.root().getInt("cache.expiry", 3600)
  }

  def newClaim(f: (Claim) => Request[AnyContent] => Either[Result, ClaimResult]) = Action {
    request => action(Claim(), request)(f)
  }

  def claiming(f: (Claim) => Request[AnyContent] => Either[Result, ClaimResult]) = Action {
    request => {
      val (key, expiration) = keyAndExpiration(request)

      Cache.getAs[Claim](key) match {
        case Some(claim) => action(claim, request)(f)

        case None =>
          if (Play.isTest) {
            val claim = Claim()
            Cache.set(key, claim, expiration) // place an empty claim in the cache to satisfy tests
            action(claim, request)(f)
          } else {
            Logger.info("Claim timeout")
            Redirect("/timeout").withHeaders("X-Frame-Options" -> "SAMEORIGIN") // stop click jacking
          }
      }
    }
  }

  def action(claim: Claim, request: Request[AnyContent])(f: (Claim) => Request[AnyContent] => Either[Result, ClaimResult]): Result = {
    val (key, expiration) = keyAndExpiration(request)

    f(claim)(request) match {
      case Left(r: Result) =>
        r.withSession(CachedClaim.claimKey -> key)
          .withHeaders(CACHE_CONTROL -> "no-cache, no-store")
          .withHeaders("X-Frame-Options" -> "SAMEORIGIN") // stop click jacking

      case Right((c: Claim, r: Result)) => {
        Cache.set(key, c, expiration)

        r.withSession(CachedClaim.claimKey -> key)
          .withHeaders(CACHE_CONTROL -> "no-cache, no-store")
          .withHeaders("X-Frame-Options" -> "SAMEORIGIN") // stop click jacking
      }
    }
  }

  type JobID = String

  def claimingInJob(f: (JobID) => Claim => Request[AnyContent] => Either[Result, ClaimResult]) = Action { request =>
    claiming(f(request.body.asFormUrlEncoded.getOrElse(Map("" -> Seq(""))).get("jobID").getOrElse(Seq("Missing JobID at request"))(0)))(request)
  }
}