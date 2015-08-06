package controllers

import play.api.mvc.{Controller, Action, AnyContent, Request}
import java.util.UUID._
import play.api.cache.Cache
import models.domain.Claim
import models.view.{CacheHandling, CachedClaim, CachedChangeOfCircs}
import play.api.i18n.Lang
import play.api.Play.current

object Language extends Controller {
  def change(chosenLanguage: String) = Action {
    implicit request => {

      fromCache(request) match {
        case Some(claim) =>
          val (key, expiration) = keyAndExpiration(request)
          Cache.set(key, Claim(claim.key, claim.sections, claim.created, Some(Lang(chosenLanguage)), claim.uuid), expiration)
          redirectUrl(request) match {
            case Left(urlPath) => Redirect(urlPath)
            case Right(url) => Redirect(url._1, url._2)
          }

          // language can be changed only on first page. So if we do not get session info this means cookies are disabled.
        case None =>
          val (referer, host) = refererAndHost(request)
          if (referer.contains("/circumstances/")) Redirect(routes.CircsEnding.errorCookie())
          else Redirect(routes.ClaimEnding.errorCookie())
      }
    }
  }

  def keyAndExpiration(request: Request[AnyContent]): (String, Int) = {
    val (referer, host) = refererAndHost(request)
    val sessionKey = if (referer.contains("/circumstances/"))
      CachedChangeOfCircs.key
    else
      CachedClaim.key
    request.session.get(sessionKey).getOrElse(randomUUID.toString) ->  CacheHandling.expiration
  }

  def fromCache(request:Request[AnyContent]): Option[Claim] = {
    val (key, _) = keyAndExpiration(request)
    Cache.getAs[Claim](key)
  }

  def redirectUrl(request: Request[AnyContent]) = {
    val (referer, host) = refererAndHost(request)
    val url = referer.substring(referer.indexOf(host) + host.length)
    if (url.contains("changing=true"))
      Left(url)
    else
      Right(url -> Map("changing" -> Seq("true")))
  }

  def refererAndHost(request: Request[AnyContent]) = {
    request.headers.get("Referer").getOrElse("No Referer in header") -> request.headers.get("Host").getOrElse("No Host in header")
  }
}