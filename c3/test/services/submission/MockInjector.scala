package services.submission

import play.api.{Application, GlobalSettings, Logger}
import scala.language.existentials
import play.api.i18n.Lang
import controllers.s12_consent_and_declaration.G3Declaration
import controllers.circs.s3_consent_and_declaration.G1Declaration
import monitoring._
import utils.Injector
import monitor.HealthMonitor

trait MockInjector  extends Injector { //extends C3MonitorRegistration
  // used to create different test conditions
  var txnId: String = ""

  import scala.reflect.{classTag, ClassTag}

  val global = new GlobalSettings {

    override def getControllerInstance[A](controllerClass: Class[A]): A = resolve(controllerClass)

    override def onStart(app: Application): Unit = {
//      registerReporters()
//      registerHealthChecks()
    }
  }

  override def resolve[A](clazz: Class[A]) = {
    instances(clazz).asInstanceOf[A]
  }

  private val instances: Map[Class[_ <: Any], Any] = {
    def bind[A: ClassTag](instance: A) = classTag[A].runtimeClass -> instance
    Map(
      bind[G3Declaration](new G3Declaration {
        override val claimTransaction = new ClaimTransaction {
          override def generateId: String = txnId

          override def registerId(id: String, statusCode: String, claimType: Int, jsEnabled: Int) {
            Logger.info(s"MockTransactionIdService.registerId: $id, $statusCode, $claimType")
          }

          override def recordMi(id: String, thirdParty: Boolean = false, circsChange: Option[Int] = None, lang: Option[Lang]) {
            Logger.info(s"MockTransactionIdService.recordMi: $id, $thirdParty, $lang")
          }

          override def updateStatus(id: String, statusCode: String, claimType: Int) {
            Logger.info(s"MockTransactionIdService.updateStatus: $id, $statusCode, $claimType")
          }
        }
      }),
      bind[G1Declaration](new G1Declaration {
        override val claimTransaction = new ClaimTransaction {
          override def generateId: String = txnId

          override def registerId(id: String, statusCode: String, claimType: Int, jsEnabled: Int) {
            Logger.info(s"MockTransactionIdService.registerId: $id, $statusCode, $claimType")
          }

          override def recordMi(id: String, thirdParty: Boolean = false, circsChange: Option[Int] = None, lang: Option[Lang]) {
            Logger.info(s"MockTransactionIdService.recordMi: $id, $thirdParty, $lang")
          }

          override def updateStatus(id: String, statusCode: String, claimType: Int) {
            Logger.info(s"MockTransactionIdService.updateStatus: $id, $statusCode, $claimType")
          }

        }
      }),
      bind[AsyncClaimSubmissionComponent](new AsyncClaimSubmissionComponent {
        override val claimTransaction = new ClaimTransaction {
          override def generateId: String = txnId

          override def registerId(id: String, statusCode: String, claimType: Int, jsEnabled: Int) {
            Logger.info(s"MockTransactionIdService.registerId: $id, $statusCode, $claimType")
          }

          override def recordMi(id: String, thirdParty: Boolean = false, circsChange: Option[Int] = None, lang: Option[Lang]) {
            Logger.info(s"MockTransactionIdService.recordMi: $id, $thirdParty, $lang")
          }

          override def updateStatus(id: String, statusCode: String, claimType: Int) {
            Logger.info(s"MockTransactionIdService.updateStatus: $id, $statusCode, $claimType")
          }
        }
      }),
      bind[ClaimTransactionCheck](new ClaimTransactionCheck {
        override val claimTransaction = new ClaimTransaction {
          override def health(): Unit = {
            throw new Exception("I'm unhealthy")
          }
        }
      }),
      bind[HealthMonitor](TestHealthMonitor)
    )
  }
}

object TestHealthMonitor extends HealthMonitor


