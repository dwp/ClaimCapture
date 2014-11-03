import java.net.InetAddress

import app.ConfigProperties._
import monitor.MonitorFilter
import monitoring._
import org.slf4j.MDC
import play.api._
import play.api.mvc.Results._
import play.api.mvc._
import services.async.AsyncActors
import services.mail.EmailActors
import utils.Injector
import utils.csrf.{DwpCSRFFilter}
import utils.helpers.CarersLanguageHelper

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

object Global extends WithFilters(MonitorFilter, DwpCSRFFilter()) with Injector with CarersLanguageHelper with C3MonitorRegistration {

  override def onStart(app: Application) {
    MDC.put("httpPort", getProperty("http.port", "Value not set"))
    MDC.put("hostName", Option(InetAddress.getLocalHost.getHostName).getOrElse("Value not set"))
    MDC.put("envName", getProperty("env.name", "Value not set"))
    MDC.put("appName", getProperty("app.name", "Value not set"))
    super.onStart(app)

    val secret: String = getProperty("application.secret", "secret")
    val secretDefault: String = getProperty("secret.default", "don't Match")

    duplicateClaimCheckEnabled

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
    Logger.error(ex.getMessage)
    val csrfCookieName = getProperty("csrf.cookie.name","csrf")
    val csrfSecure = getProperty("csrf.cookie.secure",false)
    val C3VERSION = "C3Version"
    val pattern = """.*circumstances.*""".r
    val startUrl: String =  request.headers.get("Referer").getOrElse("Unknown") match {
      case pattern(_*) => controllers.circs.s1_identification.routes.G1ReportAChangeInYourCircumstances.present().url
      case _ => controllers.s1_carers_allowance.routes.G1Benefits.present().url
    }
    Future(Ok(views.html.common.error(startUrl)(Request(request, AnyContentAsEmpty),lang(request))).discardingCookies(DiscardingCookie(csrfCookieName, secure= csrfSecure), DiscardingCookie(C3VERSION)).withNewSession)
  }
}


