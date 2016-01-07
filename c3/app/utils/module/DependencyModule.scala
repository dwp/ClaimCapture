package utils.module

import controllers.circs.consent_and_declaration.GCircsDeclaration
import play.api.inject.Module
import controllers.s_consent_and_declaration.GDeclaration
import controllers.submission.XmlSubmitter
import models.domain.Claim
import models.view.ClaimHandling._
import monitor.HealthMonitor
import monitoring.{ProdHealthMonitor, ClaimTransactionCheck}
import play.api.libs.ws.WSResponse
import play.api.libs.ws.ning.NingWSResponse
import play.api.mvc.{AnyContent, Request}
import play.api.{http, Logger, Configuration, Environment}
import services.submission.AsyncClaimSubmissionComponent
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class DependencyModule extends Module {
  private def xmlPrintControllers = {
    Seq(bind(classOf[GDeclaration]).to(classOf[GDeclarationXML]),
      bind(classOf[GCircsDeclaration]).to(classOf[GCircsDeclarationXML]),
      bind(classOf[AsyncClaimSubmissionComponent]).to(classOf[AsyncClaimSubmissionComponentXML]),
      bind(classOf[ClaimTransactionCheck]).to(classOf[ClaimTransactionCheckStub]),
      bind(classOf[HealthMonitor]).to(classOf[ProdHealthMonitor]))
  }

  private def stubDBControllers = {
    Seq(bind(classOf[GDeclaration]).to(classOf[GDeclarationDB]),
      bind(classOf[GCircsDeclaration]).to(classOf[GCircsDeclarationDB]),
      bind(classOf[AsyncClaimSubmissionComponent]).to(classOf[AsyncClaimSubmissionComponentDB]),
      bind(classOf[ClaimTransactionCheck]).to(classOf[ClaimTransactionCheckStub]),
      bind(classOf[HealthMonitor]).to(classOf[ProdHealthMonitor]))
  }

  def bindings(environment: Environment, configuration: Configuration) = {
    configuration.getBoolean("submit.prints.xml") match {
      case Some(true) =>
        Logger.warn("submit.prints.xml = true")
        xmlPrintControllers
      case _ =>
        configuration.getBoolean("stub.db") match {
          case Some(true) =>
            Logger.warn("stub.db = true")
            stubDBControllers
          case _ =>
            Seq(bind(classOf[HealthMonitor]).to(classOf[ProdHealthMonitor]))
        }
    }
  }
}

class GDeclarationXML extends GDeclaration {
  override def submission(claim: Claim, request: Request[AnyContent], jsEnabled: Boolean): ClaimResult = XmlSubmitter.submission(claim, request)
  override val claimTransaction = new StubClaimTransaction
}

class GCircsDeclarationXML extends GCircsDeclaration {
  override def submission(claim: Claim, request: Request[AnyContent], jsEnabled: Boolean): ClaimResult = XmlSubmitter.submission(claim, request)
  override val claimTransaction = new StubClaimTransaction
}

class GDeclarationDB extends GDeclaration {
  override val claimTransaction = new StubClaimTransaction
}

class GCircsDeclarationDB extends GCircsDeclaration {
  override val claimTransaction = new StubClaimTransaction
}

class AsyncClaimSubmissionComponentXML extends AsyncClaimSubmissionComponent {
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
}

class AsyncClaimSubmissionComponentDB extends AsyncClaimSubmissionComponent {
  override val claimTransaction = new StubClaimTransaction
}

class ClaimTransactionCheckStub extends ClaimTransactionCheck {
  override val claimTransaction = new StubClaimTransaction
}