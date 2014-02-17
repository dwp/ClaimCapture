package services.submission

import controllers.submission.ClaimSubmissionController
import scala.xml.Elem
import scala.concurrent.Future
import play.api.libs.ws.Response
import play.api.{Mode, Configuration, GlobalSettings, Logger}
import controllers.circs.submission.ChangeOfCircsSubmissionController
import java.io.File
import com.typesafe.config.ConfigFactory
import scala.language.existentials
import submission.FormSubmission

trait MockInjector {
  // used to create different test conditions
  var txnId: String = ""
  val mockSubmission : FormSubmission
  import scala.reflect.{classTag, ClassTag}

  val global = new GlobalSettings {
    override def onLoadConfig(configuration: Configuration, path: File, classloader: ClassLoader, mode: Mode.Mode): Configuration = {
      val applicationConf = System.getProperty("config.file", s"application.${mode.toString.toLowerCase}.conf")
      val environmentOverridingConfiguration = configuration ++ Configuration(ConfigFactory.load(applicationConf))
      super.onLoadConfig(environmentOverridingConfiguration, path, classloader, mode)
    }

    override def getControllerInstance[A](controllerClass: Class[A]): A = resolve(controllerClass)
  }


  def resolve[A](clazz: Class[A]) = instances(clazz).asInstanceOf[A]

  private val instances: Map[Class[_ <: Any], Any] = {
    def controller[A: ClassTag](instance: A) = classTag[A].runtimeClass -> instance
    Map(
      controller[ClaimSubmissionController](new ClaimSubmissionController {
        override val webServiceClient = new WebServiceClient {
          override def submitClaim(claimSubmission: Elem): Future[Response] = mockSubmission.submitClaim(claimSubmission)
        }
        override val claimTransaction = new ClaimTransaction {
          override def generateId: String = txnId

          override def registerId(id: String, statusCode: String, claimType: Int) {
            Logger.info(s"MockTransactionIdService.registerId: $id, $statusCode, $claimType")
          }

          override def updateStatus(id: String, statusCode: String, claimType: Int) {
            Logger.info(s"MockTransactionIdService.updateStatus: $id, $statusCode, $claimType")
          }
        }
      }),
      controller[ChangeOfCircsSubmissionController](new ChangeOfCircsSubmissionController {
        override val webServiceClient = new WebServiceClient {
          override def submitClaim(claimSubmission: Elem): Future[Response] = mockSubmission.submitClaim(claimSubmission)
        }
        override val claimTransaction = new ClaimTransaction {
          override def generateId: String = txnId

          override def registerId(id: String, statusCode: String, claimType: Int) {
            Logger.info(s"MockTransactionIdService.registerId: $id, $statusCode, $claimType")
          }

          override def updateStatus(id: String, statusCode: String, claimType: Int) {
            Logger.info(s"MockTransactionIdService.updateStatus: $id, $statusCode, $claimType")
          }

        }
      })
    )
  }
}
