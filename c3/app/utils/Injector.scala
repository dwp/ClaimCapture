package utils

import app.ConfigProperties._
import models.view.ClaimHandling.ClaimResult
import play.api.libs.ws.ning.NingWSResponse
import scala.reflect._
import controllers.s12_consent_and_declaration.G3Declaration
import play.api.mvc.{AnyContent, Request}
import controllers.submission.XmlSubmitter
import controllers.circs.s3_consent_and_declaration.G1Declaration
import models.domain.Claim
import play.api.{http, Logger}
import services.submission.AsyncClaimSubmissionComponent
import scala.language.existentials
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.ws.WSResponse
import ExecutionContext.Implicits.global
import monitoring.{ProdHealthMonitor, ClaimTransactionCheck}
import monitor.HealthMonitor

trait Injector {

  def resolve[A](clazz: Class[A]) = instances(clazz).asInstanceOf[A]

  private lazy val instances: Map[Class[_ <: Any], Any] = {

    def bind[A: ClassTag](instance: A) = classTag[A].runtimeClass -> instance

    def xmlPrintControllers: Map[Class[_], Any] = {
      Map(
        bind[G3Declaration](new G3Declaration {
          override def submission(claim: Claim, request: Request[AnyContent], jsEnabled: Boolean): ClaimResult = XmlSubmitter.submission(claim, request)

          override val claimTransaction = new StubClaimTransaction
        }),
        bind[G1Declaration](new G1Declaration {
          override def submission(claim: Claim, request: Request[AnyContent], jsEnabled: Boolean): ClaimResult = XmlSubmitter.submission(claim, request)

          override val claimTransaction = new StubClaimTransaction
        }),
        bind[AsyncClaimSubmissionComponent](new AsyncClaimSubmissionComponent {
          override val claimTransaction = new StubClaimTransaction
          override val webServiceClient: WebServiceClient = new WebServiceClient {
            override def submitClaim(claim: Claim, txnId: String): Future[WSResponse] = {
              val resp =
                new NingWSResponse(null) {
                  override def status: Int = http.Status.OK
                  override lazy val body: String =
                    <Response>
                      <statusCode>0000</statusCode>
                    </Response>.buildString(stripComments = false)
                }
              Future(resp)
            }
          }
        }),
        bind[ClaimTransactionCheck](new ClaimTransactionCheck {
          override val claimTransaction = new StubClaimTransaction
        }),
        bind[HealthMonitor](ProdHealthMonitor)
      )
    }

    def stubDBControllers: Map[Class[_], Any] = {
      Map(
        bind[G3Declaration](new G3Declaration {
          override val claimTransaction = new StubClaimTransaction
        }),
        bind[G1Declaration](new G1Declaration {
          override val claimTransaction = new StubClaimTransaction
        }),
        bind[AsyncClaimSubmissionComponent](new AsyncClaimSubmissionComponent {
          override val claimTransaction = new StubClaimTransaction
        }),
        bind[ClaimTransactionCheck](new ClaimTransactionCheck {
          override val claimTransaction = new StubClaimTransaction
        }),
        bind[HealthMonitor](ProdHealthMonitor)
      )
    }

    if (getProperty("submit.prints.xml", default = false)) {
      Logger.warn("submit.prints.xml = true")
      xmlPrintControllers
    } else if (getProperty("stub.db", default = false)) {
      Logger.warn("stub.db = true")
      stubDBControllers
    }
    else {
      Map(bind[G3Declaration](new G3Declaration),
        bind[G1Declaration](new G1Declaration),
        bind[AsyncClaimSubmissionComponent](new AsyncClaimSubmissionComponent),
        bind[ClaimTransactionCheck](new ClaimTransactionCheck),
        bind[HealthMonitor](ProdHealthMonitor)
      )
    }
  }
}
