package modules

import com.tzavellas.sse.guice.ScalaModule
import controllers.submission.{WebServiceSubmitter, XmlSubmitter, Submitter}

class ProdModule extends ScalaModule {
  def configure() {
    bind[Submitter].to[WebServiceSubmitter]
  }
}

class DevModule extends ScalaModule {
  def configure() {
    bind[Submitter].to[XmlSubmitter]
  }
}