import app.ConfigProperties._
import java.io.File
import java.net.InetAddress
import com.typesafe.config.ConfigFactory
import java.util.UUID
import org.slf4j.MDC
import play.api._
import play.api.Configuration
import play.api.mvc._
import play.api.mvc.Results._
import play.api.Play.current
import com.google.inject.Guice
import jmx.JMXActors
import modules.{ProdModule, DevModule}
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

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

object Global extends GlobalSettings {
  lazy val injector = Guice.createInjector(module)

  def module = if (Play.isProd) ProdModule else DevModule

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
  override def onHandlerNotFound(request: RequestHeader): Future[SimpleResult] = Future(NotFound(views.html.errors.onHandlerNotFound(request)))

  override def getControllerInstance[A](controllerClass: Class[A]): A = injector.getInstance(controllerClass)

  override def onError(request: RequestHeader, ex: Throwable) = {
    Logger.error(ex.getMessage)
    val startUrl: String = getProperty("claim.start.page", "/allowance/benefits")
    Future(Ok(views.html.common.error(startUrl)))
  }

  def actorSystems = {
    JMXActors
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