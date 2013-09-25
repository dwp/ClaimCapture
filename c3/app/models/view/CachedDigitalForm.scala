package models.view

import language.implicitConversions
import reflect.ClassTag
import play.api.mvc._
import play.api.cache.Cache
import play.Configuration
import play.api.Play._
import play.api.mvc.Results.Redirect
import play.api.data.Form
import play.api.Play
import play.api.Logger
import play.api.http.HeaderNames._
import java.util.UUID._
import models.domain.{DigitalForm, QuestionGroup}

trait CachedDigitalForm {
  type FormResult = (DigitalForm, Result)

  implicit def formFiller[Q <: QuestionGroup](form: Form[Q])(implicit classTag: ClassTag[Q]) = new {
    def fill(qi: QuestionGroup.Identifier)(implicit claim: DigitalForm): Form[Q] = {
      claim.questionGroup(qi) match {
        case Some(q: Q) => form.fill(q)
        case _ => form
      }
    }
  }

  implicit def defaultResultToLeft(result: Result) = Left(result)

  implicit def claimAndResultToRight(claimingResult: FormResult) = Right(claimingResult)

  def keyAndExpiration(r: Request[AnyContent]): (String, Int) = {
    r.session.get(cacheKey).getOrElse(randomUUID.toString) -> Configuration.root().getInt("cache.expiry", 3600)
  }

  def newForm(f: (DigitalForm) => Request[AnyContent] => Either[Result, FormResult]): Action[AnyContent] = Action {
    request => {
      val (key, _) = keyAndExpiration(request)
      val claim = if (request.getQueryString("changing").getOrElse("false") == "false") { buildForm } else Cache.getAs[DigitalForm](key).getOrElse(buildForm)
      action(claim, request)(f)
    }
  }

  def fromCache(request:Request[AnyContent]):Option[DigitalForm] = {
    val (key, expiration) = keyAndExpiration(request)

    Cache.getAs[DigitalForm](key)
  }

  def executeOnForm(f: (DigitalForm) => Request[AnyContent] => Either[Result, FormResult]):Action[AnyContent]  = Action {
    request => {

      fromCache(request) match {
        case Some(claim) => action(claim, request)(f)

        case None =>
          if (Play.isTest) {
            val (key, expiration) = keyAndExpiration(request)
            val claim = buildForm
            Cache.set(key, claim, expiration) // place an empty claim in the cache to satisfy tests
            action(claim, request)(f)
          } else {
            Logger.info("Claim timeout")
            Redirect(timeoutUrl).withHeaders("X-Frame-Options" -> "SAMEORIGIN") // stop click jacking
          }
      }
    }
  }

  def timeoutUrl: String

  def cacheKey: String

  protected def buildForm: DigitalForm

  def action(claim: DigitalForm, request: Request[AnyContent])(f: (DigitalForm) => Request[AnyContent] => Either[Result, FormResult]): Result = {
    val (key, expiration) = keyAndExpiration(request)

    f(claim)(request) match {
      case Left(r: Result) =>
        r.withSession(cacheKey -> key)
          .withHeaders(CACHE_CONTROL -> "no-cache, no-store")
          .withHeaders("X-Frame-Options" -> "SAMEORIGIN") // stop click jacking

      case Right((c: DigitalForm, r: Result)) => {
        Cache.set(key, c, expiration)

        r.withSession(cacheKey -> key)
          .withHeaders(CACHE_CONTROL -> "no-cache, no-store")
          .withHeaders("X-Frame-Options" -> "SAMEORIGIN") // stop click jacking
      }
    }
  }

  type JobID = String

  def claimingInJob(f: (JobID) => DigitalForm => Request[AnyContent] => Either[Result, FormResult]) = Action { request =>
    executeOnForm(f(request.body.asFormUrlEncoded.getOrElse(Map("" -> Seq(""))).get("jobID").getOrElse(Seq("Missing JobID at request"))(0)))(request)
  }
}