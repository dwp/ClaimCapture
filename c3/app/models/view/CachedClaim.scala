package models.view

import play.api.mvc.{Action, AnyContent, Request, Result}
import play.api.cache.Cache
import models.domain.Claim

trait CachedClaim {
  import play.api.Play.current
  import scala.language.implicitConversions
  import play.api.http.HeaderNames._

  implicit def defaultResultToLeft(result: Result) = Left(result)

  implicit def claimAndResultToRight(claimingResult: (Claim, Result)) = Right(claimingResult)

  def newClaim(f: => Claim => Request[AnyContent] => Result) = Action {
    implicit request => {
      val key = request.session.get("connected").getOrElse(java.util.UUID.randomUUID().toString)
      val expiration = 3600

      def apply(claim: Claim) = f(claim)(request).withSession("connected" -> key).withHeaders(CACHE_CONTROL -> "no-cache, no-store")

      if (request.getQueryString("changing").getOrElse("false") == "false") {
        val claim = Claim()
        Cache.set(key, claim, expiration)
        apply(claim)
      } else {
        val claim = Cache.getOrElse(key, expiration)(Claim())
        apply(claim)
      }
    }
  }

  def claiming(f: => Claim => Request[AnyContent] => Either[Result, (Claim, Result)]) = Action {
    request => {
      val key = request.session.get("connected").getOrElse(java.util.UUID.randomUUID().toString)
      val expiration = 3600
      val claim = Cache.getOrElse(key, expiration)(Claim())

      f(claim)(request) match {
        case Left(r: Result) => r.withSession("connected" -> key).withHeaders(CACHE_CONTROL -> "no-cache, no-store")

        case Right((c: Claim, r: Result)) => {
          Cache.set(key, c, expiration)
          r.withSession("connected" -> key).withHeaders(CACHE_CONTROL -> "no-cache, no-store")
        }
      }
    }
  }
}