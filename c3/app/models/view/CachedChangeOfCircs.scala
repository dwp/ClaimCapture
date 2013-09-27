package models.view

import play.api.Play.current
import play.api.mvc.{Action, Result, AnyContent, Request}
import play.api.cache.Cache
import play.api.{Logger, Play}
import play.api.mvc.Results._
import models.domain.{ChangeOfCircs, Claim}
import java.util.UUID._
import models.domain.Claim
import scala.Some
import play.Configuration

object CachedChangeOfCircs {
  val key = "change-of-circs"
}

trait CachedChangeOfCircs extends CachedClaim {
  override def keyAndExpiration(r: Request[AnyContent]): (String, Int) = {
    r.session.get(CachedChangeOfCircs.key).getOrElse(randomUUID.toString) -> Configuration.root().getInt("cache.expiry", 3600)
  }

  override def newClaim(f: (Claim) => Request[AnyContent] => Either[Result, ClaimResult]): Action[AnyContent] = Action {
    request => {
      val (key, _) = keyAndExpiration(request)

      val claim = if (request.getQueryString("changing").getOrElse("false") == "false") new Claim(CachedChangeOfCircs.key) with ChangeOfCircs
                  else Cache.getAs[Claim with ChangeOfCircs](key).getOrElse(new Claim(CachedChangeOfCircs.key) with ChangeOfCircs)

      action(claim, request)(f)
    }
  }

  override def claiming(f: (Claim) => Request[AnyContent] => Either[Result, ClaimResult]): Action[AnyContent] = Action {
    request => {
      fromCache(request) match {
        case Some(claim) => action(new Claim(claim.key, claim.sections)(claim.navigation) with ChangeOfCircs, request)(f)

        case None =>
          if (Play.isTest) {
            val (key, expiration) = keyAndExpiration(request)
            val claim = new Claim(CachedChangeOfCircs.key) with ChangeOfCircs
            Cache.set(key, claim, expiration) // place an empty claim in the cache to satisfy tests
            action(claim, request)(f)
          } else {
            Logger.info("Change of circs timeout")
            Redirect("/circs-timeout").withHeaders("X-Frame-Options" -> "SAMEORIGIN") // stop click jacking
          }
      }
    }
  }
}