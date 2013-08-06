package models.view

import language.implicitConversions
import reflect.ClassTag
import play.api.mvc.{Action, AnyContent, Request, Result}
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

trait CachedClaim {
  implicit def formFiller[Q <: QuestionGroup](form: Form[Q])(implicit classTag: ClassTag[Q]) = new {
    def fill(qi: QuestionGroup.Identifier)(implicit claim: Claim): Form[Q] = {
      claim.questionGroup(qi) match {
        case Some(q: Q) => form.fill(q)
        case _ => form
      }
    }
  }

  implicit def defaultResultToLeft(result: Result) = Left(result)

  implicit def claimAndResultToRight(claimingResult: (Claim, Result)) = Right(claimingResult)

  def newClaim(f: => Claim => Request[AnyContent] => Result) = Action {
    implicit request => {
      val (key, expiration) = keyAndExpiration(request)

      def apply(claim: Claim) = f(claim)(request).withSession("connected" -> key)
        .withHeaders(CACHE_CONTROL -> "no-cache, no-store")
        .withHeaders("X-Frame-Options" -> "SAMEORIGIN") // stop click jacking

      if (request.getQueryString("changing").getOrElse("false") == "false") {
        val claim = Claim()
        Cache.set(key, claim, expiration)
        Logger.info("Starting new claim (old claim will be erased!)")
        apply(claim)
      } else {
        Cache.getAs[Claim](key) match {
          case Some(claim) => apply(claim)
          case None => Redirect("/timeout")
        }
      }
    }
  }

  def claiming(f: => Claim => Request[AnyContent] => Either[Result, (Claim, Result)]) = Action {
    request => {
      val (key, expiration) = keyAndExpiration(request)

      def action(claim: Claim): Result = {
        f(claim)(request) match {
          case Left(r: Result) =>
            r.withSession("connected" -> key)
              .withHeaders(CACHE_CONTROL -> "no-cache, no-store")
              .withHeaders("X-Frame-Options" -> "SAMEORIGIN") // stop click jacking

          case Right((c: Claim, r: Result)) => {
            Cache.set(key, c, expiration)

            r.withSession("connected" -> key)
              .withHeaders(CACHE_CONTROL -> "no-cache, no-store")
              .withHeaders("X-Frame-Options" -> "SAMEORIGIN") // stop click jacking
          }
        }
      }

      Cache.getAs[Claim](key) match {
        case Some(claim) => action(claim)

        case None =>
          if (Play.isTest) {
            val claim = Claim()
            Cache.set(key, claim, 20) // place an empty claim in the cache to satisfy tests
            action(claim)
          } else {
            Logger.info("Claim timeout")
            Redirect("/timeout").withHeaders("X-Frame-Options" -> "SAMEORIGIN") // stop click jacking

          }
      }
    }
  }

  type JobID = String

  def claimingInJob(f: => JobID => Claim => Request[AnyContent] => Either[Result, (Claim, Result)]) = Action {
    request => {
      claiming(f(request.body.asFormUrlEncoded.getOrElse(Map("" -> Seq(""))).get("jobID").getOrElse(Seq("Missing JobID at request"))(0)))(request)
    }
  }

  private def keyAndExpiration(r: Request[AnyContent]): (String, Int) = {
    r.session.get("connected").getOrElse(randomUUID.toString) -> Configuration.root().getInt("cache.expiry", 3600)
  }
}