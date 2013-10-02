import akka.actor.Props
import com.google.inject.Guice
import com.typesafe.config.ConfigFactory
import java.io.File
import java.net.InetAddress
import jmx.{ClaimInspector, RequestTimeWrapper}
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

object Global extends GlobalSettings {
  lazy val injector = Guice.createInjector(module)

  def module = if (Play.isProd) ProdModule else DevModule

  override def onStart(app: Application) {
    MDC.put("httpPort", Option(System.getProperty("http.port")).getOrElse("Value not set"))
    MDC.put("hostName", Option(InetAddress.getLocalHost.getHostName).getOrElse("Value not set"))
    MDC.put("envName", Option(System.getProperty("env.name")).getOrElse("Value not set"))
    super.onStart(app)
    Logger.info("c3 Started") // used for operations, do not remove
  }

  override def onLoadConfig(configuration: Configuration, path: File, classloader: ClassLoader, mode: Mode.Mode): Configuration = {
    val applicationConf = System.getProperty("config.file", s"application.${mode.toString.toLowerCase}.conf")
    val environmentOverridingConfiguration = configuration ++ Configuration(ConfigFactory.load(applicationConf))
    super.onLoadConfig(environmentOverridingConfiguration, path, classloader, mode)
  }

  override def onStop(app: Application) {
    super.onStop(app)
    Logger.info("c3 Stopped") // used for operations, do not remove
  }

  // 404 - page not found error http://alvinalexander.com/scala/handling-scala-play-framework-2-404-500-errors
  override def onHandlerNotFound(request: RequestHeader): Result = NotFound(views.html.errors.onHandlerNotFound(request))

  override def getControllerInstance[A](controllerClass: Class[A]): A = injector.getInstance(controllerClass)

  override def doFilter(action: EssentialAction) = {
    val composedFunction = (RefererCheck(_:EssentialAction)) compose(RequestTime(_:EssentialAction))
    composedFunction(action)
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

class JMXFilter extends Filter {
  def apply(f: (RequestHeader) => Result)(rh: RequestHeader): Result = f(rh)

  override def apply(f: EssentialAction):EssentialAction = {
    if (play.Configuration.root().getBoolean("jmxEnabled")) super.apply(f)
    else f
  }
}

object RequestTime extends JMXFilter {
  override def apply(f: (RequestHeader) => Result)(rh: RequestHeader): Result = {
    val requestTime = System.currentTimeMillis()
    val ret = f(rh)

    if (!""".*\..*""".r.pattern.matcher(rh.path).matches()) {
      Logger.info("Filter!")
      Actors.claimInspector ! RequestTimeWrapper(rh.path, requestTime, System.currentTimeMillis())
    }

    ret
  }
}

object Actors {
  import play.api.libs.concurrent.Akka

  val claimInspector = Akka.system.actorOf(Props[ClaimInspector], name = "claim-inspector")
}