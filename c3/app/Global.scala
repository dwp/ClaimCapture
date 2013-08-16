import com.google.inject.Guice
import com.typesafe.config.ConfigFactory
import java.io.File
import java.net.InetAddress
import modules.{ProdModule, DevModule}
import org.slf4j.MDC
import play.api._
import play.api.Configuration
import play.api.mvc._
import play.api.mvc.Results._
import play.api.Play.current

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
object Global extends WithFilters(RefererCheck) {
  private lazy val injector = Play.isProd match {
    case true => Guice.createInjector(new ProdModule)
    case false => Guice.createInjector(new DevModule)
  }

  override def onStart(app: Application) {
    MDC.put("httpPort", Option(System.getProperty("http.port")).getOrElse("Value not set"))
    MDC.put("hostName", Option(InetAddress.getLocalHost.getHostName).getOrElse("Value not set"))
    MDC.put("envName", Option(System.getProperty("env.name")).getOrElse("Value not set"))
    Logger.info("C3 Started")
    super.onStart(app)
  }

  override def onLoadConfig(configuration: Configuration, path: File, classloader: ClassLoader, mode: Mode.Mode): Configuration = {
    val applicationConf = System.getProperty("config.file", s"application.${mode.toString.toLowerCase}.conf")
    val environmentOverridingConfiguration = configuration ++ Configuration(ConfigFactory.load(applicationConf))
    super.onLoadConfig(environmentOverridingConfiguration, path, classloader, mode)
  }

  // 404 - page not found error     http://alvinalexander.com/scala/handling-scala-play-framework-2-404-500-errors
  override def onHandlerNotFound(request: RequestHeader): Result = {
    NotFound(views.html.errors.onHandlerNotFound(request))
  }

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    injector.getInstance(controllerClass)
  }

  override def onStop(app: Application) {
    Logger.info("C3 Stopped")
    super.onStop(app)
  }

}

object RefererCheck extends Filter {
  override def apply(next: RequestHeader => Result)(request: RequestHeader): Result = {
    val expectedReferer = Option(play.Configuration.root().getString("referer")).get
    val host = request.headers.get("Host").getOrElse("No Host in header")
    val httpReferer = request.headers.get("Referer").getOrElse("No Referer in header")

    if (httpReferer.contains(host) || httpReferer.startsWith(expectedReferer)) {
      next(request)
    } else if (play.Configuration.root().getBoolean("enforceRedirect", true)) {
      Logger.debug(s"HTTP Referer : $httpReferer")
      Logger.debug(s"Conf Referer : $expectedReferer")
      Logger.debug(s"HTTP Host : $host")
      Redirect(expectedReferer)
    } else {
      next(request)
    }
  }
}