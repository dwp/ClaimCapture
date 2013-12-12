package controllers

import play.api.mvc.{Controller, Action, AnyContent, Request}
import play.api.{Logger, Play}
import java.util.UUID._
import play.api.cache.Cache
import models.domain.Claim
import models.view.CachedClaim
import play.api.i18n.Lang
import play.api.Play.current
import app.ConfigProperties._

object Language extends Controller {
  def change(chosenLanguage: String) = Action {
    implicit request => {
      fromCache(request) match {
        case Some(claim) => {
          val (key, expiration) = keyAndExpiration(request)
          Cache.set(key, Claim(claim.key, claim.sections, claim.created, Some(Lang(chosenLanguage))), expiration)

          Redirect(redirectUrl(request))
        }

        case None => {
          Redirect(routes.ClaimEnding.timeout())
        }
      }
    }
  }

  def keyAndExpiration(request: Request[AnyContent]): (String, Int) = {
    request.session.get(CachedClaim.key).getOrElse(randomUUID.toString) ->  getProperty("cache.expiry", 3600)
  }

  def fromCache(request:Request[AnyContent]): Option[Claim] = {
    val (key, _) = keyAndExpiration(request)

    Cache.getAs[Claim](key)
  }

  def redirectUrl(request: Request[AnyContent]): String = {
    val (referer, host) = request.headers.get("Referer").getOrElse("No Referer in header") -> request.headers.get("Host").getOrElse("No Host in header")
    val url = referer.substring(referer.indexOf(host) + host.length)
    if (url.contains("/allowance/benefits") || url.contains("/circumstances/identification/about-you"))
      url + "?changing=true"
    else
      url
  }
}