package controllers

import models.view.example.Claim
import play.api.mvc.{Action, Result, AnyContent, Request}
import play.api.cache.Cache
import play.api.Play.current

trait CachedClaim {

  def newClaim(f: => Claim => Request[AnyContent] => Result) = Action {
    implicit request => {
      val key = request.session.get("connected").getOrElse(java.util.UUID.randomUUID().toString)
      val expiration: Int = 3600
      val claim = Claim()
      Cache.set(key, claim, expiration)
      f(claim)(request).withSession("connected" -> key)
    }
  }

  def ActionWithClaim(f: => Claim => Request[AnyContent] => Result) = Action {
    request => {
      val key = request.session.get("connected").getOrElse(java.util.UUID.randomUUID().toString)
      val expiration: Int = 3600
      val claim = Cache.getOrElse(key, expiration)(Claim())
      f(claim)(request).withSession("connected" -> key)
    }
  }


}
