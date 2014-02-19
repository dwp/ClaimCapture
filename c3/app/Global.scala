import app.ConfigProperties._
import java.io.File
import java.net.InetAddress
import com.typesafe.config.ConfigFactory
import java.util.UUID
import monitoring.ApplicationMonitor
import org.slf4j.MDC
import play.api._
import play.api.Configuration
import play.api.mvc._
import play.api.mvc.Results._
import jmx.JMXActors
import play.api.mvc.SimpleResult
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import play.api.mvc.Controller

/**
 * Application configuration is in a hierarchy of files:
 *
 *                            application.conf
 *                      /             |            \
 * application.prod.conf    application.dev.conf    application.test.conf <- these can override and add to application.conf
 *
 * play test  <- test mode picks up application.test.conf
 * play run   <- dev mode picks up application.dev.conf
 * play start <- prod mode picks up application.prod.conf
 *
 * To override and stipulate a particular "conf" e.g.
 * play -Dconfig.file=conf/application.test.conf run
 */

object Global extends GlobalSettings with Injector with Controller {

  override def onStart(app: Application) {
    MDC.put("httpPort", Option(System.getProperty("http.port")).getOrElse("Value not set"))
    MDC.put("hostName", Option(InetAddress.getLocalHost.getHostName).getOrElse("Value not set"))
    MDC.put("envName", Option(System.getProperty("env.name")).getOrElse("Value not set"))
    MDC.put("appName", Option(System.getProperty("app.name")).getOrElse("Value not set"))
    super.onStart(app)

    actorSystems

    Logger.info("c3 Started") // used for operations, do not remove
  }

  override def onLoadConfig(configuration: Configuration, path: File, classloader: ClassLoader, mode: Mode.Mode): Configuration = {
    val dynamicConfig = Configuration.from(Map("session.cookieName" -> UUID.randomUUID().toString.substring(0, 16)))
    val applicationConf = System.getProperty("config.file", s"application.${mode.toString.toLowerCase}.conf")
    val environmentOverridingConfiguration = configuration ++
      Configuration(ConfigFactory.load(applicationConf)) ++
      dynamicConfig
    super.onLoadConfig(environmentOverridingConfiguration, path, classloader, mode)
  }

  override def onStop(app: Application) {
    super.onStop(app)
    Logger.info("c3 Stopped") // used for operations, do not remove
  }

  // 404 - page not found error http://alvinalexander.com/scala/handling-scala-play-framework-2-404-500-errors
  override def onHandlerNotFound(request: RequestHeader): Future[SimpleResult] = Future(NotFound(views.html.errors.onHandlerNotFound(Request(request,AnyContentAsEmpty))))

  override def getControllerInstance[A](controllerClass: Class[A]): A = resolve(controllerClass)

  override def onError(request: RequestHeader, ex: Throwable) = {
    Logger.error(ex.getMessage)
    val startUrl: String = getProperty("claim.start.page", "/allowance/benefits")
    Future(Ok(views.html.common.error(startUrl)(lang(request), Request(request, AnyContentAsEmpty))))
  }

  def actorSystems = {
    JMXActors
    ApplicationMonitor.begin
  }

}

// Add WithFilters(LoggingFilter) to enable good debug
object LoggingFilter extends Filter {
  def apply(nextFilter: (RequestHeader) => Future[SimpleResult])
           (requestHeader: RequestHeader): Future[SimpleResult] = {
    val startTime = System.currentTimeMillis
    nextFilter(requestHeader).map { result =>
      val endTime = System.currentTimeMillis
      val requestTime = endTime - startTime
      Logger.info(s"${requestHeader.method} ${requestHeader.uri} " +
        s"took ${requestTime}ms and returned ${result.header.status}")
      result.withHeaders("Request-Time" -> requestTime.toString)
    }
  }
}

class JMXFilter extends Filter {
  def apply(f: (RequestHeader) => Future[SimpleResult])(rh: RequestHeader): Future[SimpleResult] = f(rh)

//  def apply(f: (RequestHeader) => Result)(rh: RequestHeader): Result = f(rh)

  override def apply(f: EssentialAction): EssentialAction = {
    if (play.Configuration.root().getBoolean("jmxEnabled", false)) super.apply(f)
    else f
  }
}