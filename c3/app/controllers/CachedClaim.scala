package controllers

import play.api.mvc.{Action, Result, AnyContent, Request}
import models.view.example.{ClaimHolder, Claim}
import play.api.cache.Cache
import play.api.Play.current

trait CachedClaim {
  def ActionWithClaim(f: => ClaimHolder => Request[AnyContent] => Result) = Action {
    request => {
      val key = request.session.get("connected").getOrElse(java.util.UUID.randomUUID().toString)
      val expiration: Int = 3600 // 1 hour
      val claim = Cache.getOrElse(key, expiration)(new ClaimHolder(Claim()))
      f(claim)(request).withSession("connected" -> key)
    }
  }
}
