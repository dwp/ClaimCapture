package services.submission

import scala.concurrent.ExecutionContext
import play.api.{http, Logger}
import controllers.submission._
import services.{SubmissionCacheService, EncryptionService, ClaimTransactionComponent}
import ExecutionContext.Implicits.global
import ClaimSubmissionService._
import play.api.libs.ws.Response
import models.domain.Claim
import scala.Some

class AsyncClaimSubmissionComponent extends AsyncClaimSubmissionService
with ClaimTransactionComponent
with WebServiceClientComponent
 {
  val webServiceClient = new WebServiceClient
  val claimTransaction = new ClaimTransaction
}

case class DuplicateClaimException(msg:String) extends Exception(msg)

trait AsyncClaimSubmissionService extends SubmissionCacheService with EncryptionService {

  this: ClaimTransactionComponent with WebServiceClientComponent =>


  def submission(claim: Claim): Unit = {
    val txnID = claim.transactionId.get
    Logger.info(s"Retrieved transaction Id [$txnID]")

    checkCacheStore(claim)

    try {
      webServiceClient.submitClaim(claim, txnID).map(
        response => processClaimSubmission(claim, response)
      )
    } catch {
      case e:Exception =>
        Logger.error(s"INTERNAL_SERVER_ERROR transactionId [$txnID]")
        Logger.error("global error talking to ingress ",e)
        updateTransactionAndCache(txnID, SERVER_ERROR, claim)
    }
  }

  def processClaimSubmission(claim: Claim, response: Response) = {
    val txnID = claim.transactionId.get
    Logger.debug("Got response from WS:" + response)
    try {
      claimTransaction.updateStatus(txnID, SUBMITTED, claimType(claim))
      ClaimSubmissionService.recordMi(claim, txnID, claimTransaction.recordMi)
      processResponse(claim, txnID, response)
    } catch {
      case e: Exception =>
        Logger.error(s"Error processing claim submission for transactionId [$txnID].",e)
        removeFromCache(claim)
    }
  }

  def checkCacheStore(claim:Claim): Unit = {
    if (checkEnabled) {
      getFromCache(claim) match {
        case Some(x) =>
        Logger.error("Already in cache, should not be submitting again! Duplicate claim submission. Claim fingerprint: " + encrypt(x))
        claimTransaction.updateStatus(claim.transactionId.get, SERVER_ERROR, claimType(claim))

        // this gets logged by the actor
        throw new DuplicateClaimException("Duplicate claim submission.")
        case _ => storeInCache(claim)
      }
    }
  }

  private def updateTransactionAndCache(txnID: String, status: String, claim: Claim) = {
    claimTransaction.updateStatus(txnID, status, claimType(claim))
    removeFromCache(claim)
  }

  private def processErrorResponse(claim: Claim, txnID: String, response: Response): Unit = {
    val statusMsg = httpStatusCodes(response.status)
    Logger.error(s"$statusMsg : ${response.status} : ${response.toString}, transactionId [$txnID]")
    updateTransactionAndCache(txnID, txnStatusConst(statusMsg), claim)
  }

  private def processResponse(claim: Claim, txnID: String, response: Response): Unit = {
    Logger.debug("Response status is " + response.status)
    response.status match {
      case http.Status.OK => ok(claim, txnID, response)
      case _ => processErrorResponse(claim, txnID, response)
    }
  }

  private def ok(claim: Claim, txnID: String, response: Response) = {
    claimTransaction.updateStatus(txnID, SUCCESS, claimType(claim))
    Logger.info(s"Successful submission : ${claim.key} : $txnID")
  }
}

object AsyncClaimSubmissionService {
  val GENERATED = "0100" //submitting
}
