package services.submission

import play.api.{GlobalSettings, Logger}
import scala.language.existentials
import play.api.i18n.Lang
import controllers.s11_consent_and_declaration.G5Submit
import controllers.circs.s3_consent_and_declaration.G1Declaration

trait MockInjector {
  // used to create different test conditions
  var txnId: String = ""

  import scala.reflect.{classTag, ClassTag}

  val global = new GlobalSettings {
    override def getControllerInstance[A](controllerClass: Class[A]): A = resolve(controllerClass)
  }

  def resolve[A](clazz: Class[A]) = instances(clazz).asInstanceOf[A]

  private val instances: Map[Class[_ <: Any], Any] = {
    def controller[A: ClassTag](instance: A) = classTag[A].runtimeClass -> instance
    Map(
      controller[G5Submit](new G5Submit {
        override val claimTransaction = new ClaimTransaction {
          override def generateId: String = txnId

          override def registerId(id: String, statusCode: String, claimType: Int, jsEnabled:Int) {
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
      controller[G1Declaration](new G1Declaration {
        override val claimTransaction = new ClaimTransaction {
          override def generateId: String = txnId

          override def registerId(id: String, statusCode: String, claimType: Int, jsEnabled:Int) {
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
      controller[AsyncClaimSubmissionComponent](new AsyncClaimSubmissionComponent {
        override val claimTransaction = new ClaimTransaction {
          override def generateId: String = txnId

          override def registerId(id: String, statusCode: String, claimType: Int, jsEnabled:Int) {
            Logger.info(s"MockTransactionIdService.registerId: $id, $statusCode, $claimType")
          }

          override def recordMi(id: String, thirdParty: Boolean = false, circsChange: Option[Int] = None, lang: Option[Lang]) {
            Logger.info(s"MockTransactionIdService.recordMi: $id, $thirdParty, $lang")
          }

          override def updateStatus(id: String, statusCode: String, claimType: Int) {
            Logger.info(s"MockTransactionIdService.updateStatus: $id, $statusCode, $claimType")
          }
        }
      })
    )
  }
}
