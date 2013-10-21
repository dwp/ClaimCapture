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
import play.api.{mvc, Play, Logger}
import play.api.http.HeaderNames._
import java.util.UUID._
import akka.actor.FSM.->

object CachedClaim {
  val claimKey = "claim"
}

trait CachedClaim {
  type ClaimResult = (Claim, Result)
  // This val is crated so we can check wether we come from the second page to the first one so we don't create a new claim, and we load the one we have in the cache.
  private val secondPage = "/allowance/hours"

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

  def newKeyAndExpiration(r: Request[AnyContent]): (String, Int) = {
    (r.session.get(CachedClaim.claimKey) match {
      case Some(key) => {
        Logger.warn("Session key being re-used !!!")
        key
      }
      case None => {
        randomUUID.toString
      }
    }) -> Configuration.root().getInt("cache.expiry", 3600)
  }

  def existingKeyAndExpiration(r: Request[AnyContent]): (String, Int) = {
    (r.session.get(CachedClaim.claimKey) match {
      case Some(key) => key
      case None => {
        Logger.warn("Session key is being regenerated !!!")
        randomUUID.toString
      }
    }) -> Configuration.root().getInt("cache.expiry", 3600)
  }

  def newClaim(f: => Claim => Request[AnyContent] => Result) = Action {
    implicit request => {
      val (key, expiration) = newKeyAndExpiration(request)

      def apply(claim: Claim) = f(claim)(request).withSession(CachedClaim.claimKey -> key)
        .withHeaders(CACHE_CONTROL -> "no-cache, no-store")
        .withHeaders("X-Frame-Options" -> "SAMEORIGIN") // stop click jacking

      if (request.getQueryString("changing").getOrElse("false") == "false" && !request.headers.get("referer").getOrElse("").contains(secondPage)) {
        val claim = Claim()
        Cache.set(key, claim, expiration)
        Logger.info("Starting new claim (old claim will be erased!)")
        apply(claim)
      } else if (request.getQueryString("changing").getOrElse("false") == "true" || request.headers.get("referer").getOrElse("").contains(secondPage)) {
        Cache.getAs[Claim](key) match {
          case Some(claim) => apply(claim)
          case None => Redirect("/timeout")
        }
      } else {
        Redirect("/timeout")
      }
    }
  }

  def claiming(f: => Claim => Request[AnyContent] => Either[Result, ClaimResult]) = Action {
    request => {
      val (key, expiration) = existingKeyAndExpiration(request)

      def action(claim: Claim): Result = {
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

      Cache.getAs[Claim](key) match {
        case Some(claim) => action(claim)

        case None =>
          if (Play.isTest) {
            val claim = Claim()
            Cache.set(key, claim, 600) // place an empty claim in the cache to satisfy tests
            action(claim)
          } else {
            Logger.info("Claim timeout")
            Redirect("/timeout").withHeaders("X-Frame-Options" -> "SAMEORIGIN") // stop click jacking
          }
      }
    }
  }

  type JobID = String

  def claimingInJob(f: => JobID => Claim => Request[AnyContent] => Either[Result, ClaimResult]) = Action { request =>
    claiming(f(request.body.asFormUrlEncoded.getOrElse(Map("" -> Seq(""))).get("jobID").getOrElse(Seq("Missing JobID at request"))(0)))(request)
  }
}