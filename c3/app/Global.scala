import com.typesafe.config.ConfigFactory
import java.io.File
import play.api._
import play.api.mvc._
import play.api.mvc.Results._

/**
 * Application configuration is in a hierarchy of files:
 *                            application.conf
 *                    /               |               \
 * application.prod.conf    application.dev.conf    application.test.conf <- these can override and add to application.conf
 * play test  <- test mode picks up application.test.conf
 * play run   <- dev mode picks up application.dev.conf
 * play start <- prod mode picks up application.prod.conf
 * To override and stipulate a particular "conf" e.g.
 * play -Dconfig.file=conf/application.test.conf run
 */
object Global extends GlobalSettings {
  override def onLoadConfig(configuration: Configuration, path: File, classloader: ClassLoader, mode: Mode.Mode): Configuration = {
    val applicationConf = System.getProperty("config.file", s"application.${mode.toString.toLowerCase}.conf")
    val environmentOverridingConfiguration = configuration ++ Configuration(ConfigFactory.load(applicationConf))
    super.onLoadConfig(environmentOverridingConfiguration, path, classloader, mode)
  }
  
  // 404 - page not found error     http://alvinalexander.com/scala/handling-scala-play-framework-2-404-500-errors
  override def onHandlerNotFound(request: RequestHeader): Result = {
    NotFound(views.html.errors.onHandlerNotFound(request))
  }
}