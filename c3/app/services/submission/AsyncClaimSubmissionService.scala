package services.submission

import scala.concurrent.ExecutionContext
import play.api.{http, Logger}
import controllers.submission._
import services.ClaimTransactionComponent
import ExecutionContext.Implicits.global
import play.api.libs.ws.Response
import models.domain.Claim
import ClaimSubmissionService._
import monitoring.Counters

class AsyncClaimSubmissionComponent extends AsyncClaimSubmissionService
with ClaimTransactionComponent
with WebServiceClientComponent {
  val webServiceClient = new WebServiceClient
  val claimTransaction = new ClaimTransaction
}

trait AsyncClaimSubmissionService {

  this: ClaimTransactionComponent with WebServiceClientComponent =>

  def submission(claim: Claim): Unit = {
    val txnID = claim.transactionId.get
    Logger.info(s"Retrieved Id : $txnID")

    try {
      webServiceClient.submitClaim(claim, txnID).map(
        response => {
          Logger.info("Got response from WS:" + response)
          claimTransaction.updateStatus(txnID, SUBMITTED, claimType(claim))
          ClaimSubmissionService.recordMi(claim, txnID, claimTransaction.recordMi)
          processResponse(claim, txnID, response)
        })
    } catch {
      case e: Exception =>
        Logger.error(s"INTERNAL_SERVER_ERROR TxnId : $txnID")
        Logger.error("global error talking to ingress " + e.getMessage + " " + e.getStackTraceString)
        claimTransaction.updateStatus(txnID, SERVER_ERROR, claimType(claim))
    }
  }

  private def ok(claim: Claim, txnID: String, response: Response) = {
    Logger.info(s"Received OK : ${claim.key}")

    claimTransaction.updateStatus(txnID, SUCCESS, claimType(claim))
    Logger.info(s"Successful submission : ${claim.key} : $txnID")

  }

  private def processResponse(claim: Claim, txnID: String, response: Response): Unit = {
    Logger.debug("Response status is " + response.status)
    response.status match {
      case http.Status.OK =>
        Counters.incrementClaimSubmissionCount()
        ok(claim, txnID, response)
      case http.Status.SERVICE_UNAVAILABLE =>
        Counters.incrementSubmissionErrorStatus("service-unavailable")
        Logger.error(s"SERVICE_UNAVAILABLE : ${response.status} : ${response.toString}, TxnId : $txnID")
        claimTransaction.updateStatus(txnID, SERVICE_UNAVAILABLE, claimType(claim))
      case http.Status.BAD_REQUEST =>
        Counters.incrementSubmissionErrorStatus("bad-request-error")
        Logger.error(s"BAD_REQUEST : ${response.status} : ${response.toString}, TxnId : $txnID")
        claimTransaction.updateStatus(txnID, BAD_REQUEST_ERROR, claimType(claim))
      case http.Status.REQUEST_TIMEOUT =>
        Counters.incrementSubmissionErrorStatus("request-timeout-error")
        Logger.error(s"REQUEST_TIMEOUT : ${response.status} : ${response.toString}, TxnId : $txnID")
        claimTransaction.updateStatus(txnID, REQUEST_TIMEOUT_ERROR, claimType(claim))
      case http.Status.INTERNAL_SERVER_ERROR =>
        Counters.incrementSubmissionErrorStatus("internal-server-error")
        Logger.error(s"INTERNAL_SERVER_ERROR : ${response.status} : ${response.toString}, TxnId : $txnID")
        claimTransaction.updateStatus(txnID, SERVER_ERROR, claimType(claim))
    }
  }
}

object AsyncClaimSubmissionService {
  val GENERATED = "0100" //submitting
}
