package modules

import com.tzavellas.sse.guice.ScalaModule
import controllers.submission.{WebServiceSubmitter, XmlSubmitter, Submitter}
import services.{PostgresTransactionIdService, TransactionIdService}

class ProdModule extends ScalaModule {
  def configure() {
    bind[Submitter].to[WebServiceSubmitter]
    bind[TransactionIdService].to[PostgresTransactionIdService]
  }
}

class DevModule extends ScalaModule {
  def configure() {
    bind[Submitter].to[XmlSubmitter]
  }
}