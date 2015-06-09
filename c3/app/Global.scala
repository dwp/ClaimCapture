import java.net.InetAddress

import app.ConfigProperties._
import monitor.MonitorFilter
import monitoring._
import org.slf4j.MDC
import play.api._
import play.api.http.HeaderNames._
import play.api.mvc.Results._
import play.api.mvc._
import services.async.AsyncActors
import services.mail.EmailActors
import utils.Injector
import utils.csrf.DwpCSRFFilter
import utils.filters.{UserAgentCheckException, CSRFCreation, UserAgentCheckFilter}
import utils.helpers.CarersLanguageHelper

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Global extends WithFilters(MonitorFilter, UserAgentCheckFilter(), DwpCSRFFilter(createIfFound = CSRFCreation.createIfFound, createIfNotFound = CSRFCreation.createIfNotFound)) with Injector with CarersLanguageHelper with C3MonitorRegistration {

  override def onStart(app: Application) {
    MDC.put("httpPort", getProperty("http.port", "Value not set"))
    MDC.put("hostName", Option(InetAddress.getLocalHost.getHostName).getOrElse("Value not set"))
    MDC.put("envName", getProperty("env.name", "Value not set"))
    MDC.put("appName", getProperty("app.name", "Value not set"))
    super.onStart(app)

    val secret: String = getProperty("application.secret", "secret")
    val secretDefault: String = getProperty("secret.default", "don't Match")

    duplicateClaimCheckEnabled()

    if (secret.equals(secretDefault)) {
      Logger.warn("application.secret is using default value")
    }

    actorSystems()

    registerReporters()
    registerHealthChecks()

    Logger.info(s"c3 Started : memcachedplugin is ${getProperty("memcachedplugin", "Not defined")}") // used for operations, do not remove
    Logger.info(s"c3 property include.analytics is ${getProperty("include.analytics", "Not defined")}") // used for operations, do not remove
  }

  def actorSystems() {
    EmailActors
    AsyncActors
  }

  def duplicateClaimCheckEnabled() = {
    val checkLabel: String = "duplicate.submission.check"
    val check = getProperty(checkLabel, default = true)
    Logger.info(s"$checkLabel = $check")
  }

  override def onStop(app: Application) {
    super.onStop(app)
    Logger.info("c3 Stopped") // used for operations, do not remove
  }

  // 404 - page not found error http://alvinalexander.com/scala/handling-scala-play-framework-2-404-500-errors
  override def onHandlerNotFound(requestHeader: RequestHeader): Future[Result] = {
    implicit val request = Request(requestHeader, AnyContentAsEmpty)
    Future(NotFound(views.html.common.onHandlerNotFound()))
  }

  override def getControllerInstance[A](controllerClass: Class[A]): A = resolve(controllerClass)

  override def onError(request: RequestHeader, ex: Throwable) = {
    val csrfCookieName = getProperty("csrf.cookie.name", "csrf")
    val csrfSecure = getProperty("csrf.cookie.secure", getProperty("session.secure", default = false))
    val theDomain = Play.current.configuration.getString("session.domain")
    val C3VERSION = "C3Version"
    val pattern = """.*circumstances.*""".r
    val cookiesAbsent = request.cookies.isEmpty

    val referer = request.headers.get("Referer")

    val url = referer match {
      case Some(r) => r
      case _ => request.path
    }

    Logger.error(s"${ex.getCause.toString}. Origin path: ${request.path} Referer: $referer Cookies empty $cookiesAbsent")

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
