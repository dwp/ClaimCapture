package controllers

import javax.inject.Inject

import play.api.Play._
import play.api.mvc._
import models.domain.Claim
import models.view.{CacheHandlingWithClaim, CacheHandlingWithCircs}
import play.api.i18n._

object Language extends Controller with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  def isClaim(request: Request[AnyContent]): Boolean = if (referer(request).contains("/circumstances/")) false else true

  def referer(request: Request[AnyContent]): String = request.headers.get("Referer").getOrElse("No Referer in header")

  def change(chosenLanguage: String) = Action {
    implicit request => {
      val cacheHandler = isClaim(request) match{
        case true => new CacheHandlingWithClaim()
        case false => new CacheHandlingWithCircs()
      }
      cacheHandler.fromCache(request) match {
        case Some(claim) =>
          cacheHandler.saveInCache(Claim(cacheHandler.cacheKey, claim.sections, claim.created, Some(Lang(chosenLanguage)), claim.gacid, claim.uuid))
          redirectUrl(request) match {
            case Left(urlPath) => messagesApi.setLang(Redirect(urlPath), Lang(chosenLanguage))
            case Right(url) => messagesApi.setLang(Redirect(url._1, url._2), Lang(chosenLanguage))
          }

        // language can be changed only on first page. So if we do not get session info this means cookies are disabled.
        case None =>
          if (isClaim(request)) Redirect(routes.ClaimEnding.errorCookie())
          else Redirect(routes.CircsEnding.errorCookie())
      }
    }
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
