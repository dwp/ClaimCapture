import com.typesafe.config.ConfigFactory
import java.io.File
import play.api.{GlobalSettings, Mode, Configuration}

/**
 * play -Dconfig.file=conf/application.test.conf run
 */
object Global extends GlobalSettings {
  override def onLoadConfig(configuration: Configuration, path: File, classloader: ClassLoader, mode: Mode.Mode): Configuration = {
    val applicationConf = System.getProperty("config.file", s"application.${mode.toString.toLowerCase}.conf")
    val environmentOverridingConfiguration = configuration ++ Configuration(ConfigFactory.load(applicationConf))
    super.onLoadConfig(environmentOverridingConfiguration, path, classloader, mode)
  }
}