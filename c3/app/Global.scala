import java.net.InetAddress

import app.ConfigProperties._
import monitoring._
import org.slf4j.MDC
import play.api._
import play.api.http.HeaderNames._
import play.api.i18n.{Messages, MMessages, MessagesApi}
import play.api.mvc.Results._
import play.api.mvc._
import services.async.AsyncActors
import services.mail.EmailActors
import utils.filters.UserAgentCheckException
import utils.helpers.CarersLanguageHelper

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.Play.current

object Global extends GlobalSettings with CarersLanguageHelper with C3MonitorRegistration {

  override def onStart(app: Application) {
    MDC.put("httpPort", getStringProperty("http.port", throwError = false))
    MDC.put("hostName", Option(InetAddress.getLocalHost.getHostName).getOrElse("Value not set"))
    MDC.put("envName", getStringProperty("env.name", throwError = false))
    MDC.put("appName", getStringProperty("app.name", throwError = false))
    super.onStart(app)

    val secret: String = getStringProperty("play.crypto.secret", throwError = false)
    val secretDefault: String = getStringProperty("secret.default", throwError = false)

    duplicateClaimCheckEnabled()

    if (secret.equals(secretDefault)) {
      Logger.warn("play.crypto.secret is using default value")
    }

    actorSystems()

    registerReporters()
    registerHealthChecks()

    Logger.info(s"c3 property include.analytics is ${getBooleanProperty("include.analytics", throwError = false)}") // used for operations, do not remove
  }

  def actorSystems() {
    EmailActors
    AsyncActors
  }

  def duplicateClaimCheckEnabled() = {
    val check = getBooleanProperty("duplicate.submission.check")
    Logger.info(s"duplicate.submission.check = $check")
  }

  override def onStop(app: Application) {
    super.onStop(app)
    Logger.info("c3 Stopped") // used for operations, do not remove
  }

  // 404 - page not found error http://alvinalexander.com/scala/handling-scala-play-framework-2-404-500-errors
  override def onHandlerNotFound(requestHeader: RequestHeader): Future[Result] = {
    implicit val request = Request(requestHeader, AnyContentAsEmpty)
    implicit val flash = request.flash
    implicit val messages = Messages(implicitly[play.api.i18n.Lang], current.injector.instanceOf[MMessages])
    Future(NotFound(views.html.common.onHandlerNotFound()))
  }

  override def onError(request: RequestHeader, ex: Throwable) = {
    val csrfCookieName = getStringProperty("play.filters.csrf.cookie.name")
    val csrfSecure = getBooleanProperty("play.filters.csrf.cookie.secure")
    val theDomain = Play.current.configuration.getString("session.domain")
    val domainRoot = getStringProperty("domainRoot")
    val C3VERSION = "C3Version"
    val pattern = """.*circumstances.*""".r
    val cookiesAbsent = request.cookies.isEmpty

    Logger.error("Unexpected error on application",ex)
    val referer = request.headers.get("Referer")

    val url = referer match {
      case Some(r) if r.contains(domainRoot) => r
      case _ => request.path
    }

    Logger.info(s"Origin path: ${request.path} Referer: $referer Cookies empty $cookiesAbsent")

    url match {
      // we redirect to the error page with specific cookie error message if cookies are disabled.
      case pattern(_*) if cookiesAbsent =>
        Logger.warn("Redirecting to Error cookie page for a change-of-circs.")
        Future(Redirect(controllers.routes.CircsEnding.errorCookie()))
      case _ if cookiesAbsent =>
        Logger.warn("Redirecting to Error cookie page for a claim.")
        Future(Redirect(controllers.routes.ClaimEnding.errorCookie()))
      // We redirect and do not stay in same URL to update Google Analytics
      // We delete our cookies to ensure we restart anew
      case pattern(_*) =>
        Logger.warn("Redirecting to Error page for a change-of-circs.")
        ex.getCause match {
          case e: UserAgentCheckException => Future(withHeaders(Redirect(controllers.routes.CircsEnding.error())))
          case _ =>
            Logger.warn("Change of Circs error and delete cookies")
            Future(withHeaders(Redirect(controllers.routes.CircsEnding.error()))
              .discardingCookies(DiscardingCookie(csrfCookieName, secure = csrfSecure, domain = theDomain), DiscardingCookie(C3VERSION)).withNewSession)
        }
      case _ =>
        Logger.warn("Redirecting to Error page for a claim.")
        ex.getCause match {
          case e: UserAgentCheckException => Future(withHeaders(Redirect(controllers.routes.ClaimEnding.error())))
          case _ =>
            Logger.warn("Claim error and delete cookies")
            Future(withHeaders(Redirect(controllers.routes.ClaimEnding.error()))
              .discardingCookies(DiscardingCookie(csrfCookieName, secure = csrfSecure, domain = theDomain), DiscardingCookie(C3VERSION)).withNewSession)
        }
    }
  }

  private def withHeaders(result: Result): Result = {
    result.withHeaders(CACHE_CONTROL -> "must-revalidate,no-cache,no-store", "X-Frame-Options" -> "SAMEORIGIN")
  }
}
