import services.submission.StaticTransactionIdService

package object modules {
  import com.tzavellas.sse.guice.ScalaModule
  import controllers.submission.{WebServiceSubmitter, XmlSubmitter, Submitter}
  import services.{PostgresTransactionIdService, TransactionIdService}
  import services.submission.{WebserviceFormSubmission, FormSubmission}

  object ProdModule extends ScalaModule {
    def configure() {
      bind[FormSubmission].to[WebserviceFormSubmission]
      bind[Submitter].to[WebServiceSubmitter]
      bind[TransactionIdService].to[StaticTransactionIdService]
    }
  }

  object DevModule extends ScalaModule {
    def configure() {
      bind[Submitter].to[XmlSubmitter]
    }
  }
}