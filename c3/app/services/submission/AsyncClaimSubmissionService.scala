package services.submission

import app.ConfigProperties._
import models.view.CachedClaim
import monitoring.{Counters}
import play.api.libs.ws.WSResponse

import scala.concurrent.ExecutionContext
import play.api.{http, Logger}
import controllers.submission._
import services.{EmailServices, SubmissionCacheService, ClaimTransactionComponent}
import ExecutionContext.Implicits.global
import ClaimSubmissionService._
import models.domain.Claim

class AsyncClaimSubmissionComponent extends AsyncClaimSubmissionService
with ClaimTransactionComponent
with WebServiceClientComponent
 {
  val webServiceClient = new WebServiceClient
  val claimTransaction = new ClaimTransaction
}

case class DuplicateClaimException(msg:String) extends Exception(msg)

/**
* This service relies WS to submit a claim and then it processes the asynchronous response.
* The response is stored into the transaction database.
* This service uses a submission cache to detect resubmission of a claim already submitted successfully (duplicate claims).
*/
trait AsyncClaimSubmissionService extends SubmissionCacheService {

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
        Logger.error(s"Global error while submitting ${claim.key} ${claim.uuid}. INTERNAL_SERVER_ERROR [$INTERNAL_ERROR] transactionId [$txnID].", e)
        updateTransactionAndCache(txnID, INTERNAL_ERROR, claim)
        Counters.incrementSubmissionErrorStatus(INTERNAL_ERROR)
    }
  }

  private def processClaimSubmission(claim: Claim, response: WSResponse) = {
    val txnID = claim.transactionId.get
    Logger.debug(s"Got response ${response.statusText} from WS for: ${claim.key} transactionId [$txnID].")
    try {
      Logger.info(s"Claim submitted [${SUBMITTED}] - response status ${response.status} for: ${claim.key} transactionId [$txnID].")
      claimTransaction.updateStatus(txnID, SUBMITTED, claimType(claim))
      ClaimSubmissionService.recordMi(claim, txnID, claimTransaction.recordMi)
      processResponse(claim, txnID, response)
    } catch {
      case e: Exception =>
        Logger.error(s"Error processing submitted ${claim.key} [${SUBMITTED}] submission's response with response status ${response.status} for transactionId [$txnID].",e)
        removeFromCache(claim)
    }
  }

  private def checkCacheStore(claim:Claim): Unit = {
    if (checkEnabled) {
      getFromCache(claim) match {
        case Some(x) =>
          Logger.error(s"Status ${INTERNAL_ERROR}. Already in cache, should not be submitting again! Duplicate claim submission. ${claim.key} ${claim.uuid} transactionId [${claim.transactionId.get}]")
          claimTransaction.updateStatus(claim.transactionId.get, INTERNAL_ERROR, claimType(claim))

        // this gets logged by the actor
        throw new DuplicateClaimException(s"Duplicate claim submission. transactionId [${claim.transactionId.get}]")
        case _ => storeInCache(claim)
      }
    }
  }

  private def updateTransactionAndCache(txnID: String, status: String, claim: Claim) = {
    claimTransaction.updateStatus(txnID, status, claimType(claim))
    removeFromCache(claim)
  }

  private def processErrorResponse(claim: Claim, txnID: String, response: WSResponse): Unit = {
    val statusMsg = httpStatusCodes(response.status)
    Logger.error(s"Submission failed. [${txnStatusConst(statusMsg)}] : response status ${response.status} : ${response.toString}, transactionId [$txnID].")
    updateTransactionAndCache(txnID, txnStatusConst(statusMsg), claim)
    Counters.incrementSubmissionErrorStatus(statusMsg)
  }

  private def processResponse(claim: Claim, txnID: String, response: WSResponse): Unit = {
    Logger.debug(s"Response status is ${response.status} for : ${claim.key} : transactionId [$txnID].")
    response.status match {
      case http.Status.OK =>
        Counters.incrementClaimSubmissionCount
        ok(claim, txnID, response)
      case _ => processErrorResponse(claim, txnID, response)
    }
  }

  private def ok(claim: Claim, txnID: String, response: WSResponse) = {
    Logger.info(s"Successful submission  [${SUCCESS}] - response status ${response.status} for : ${claim.key}  transactionId [$txnID].")
    claimTransaction.updateStatus(txnID, SUCCESS, claimType(claim))

    //We send email after we update status and we verify that the submission has been successful
    if (getProperty("mailer.enabled",default=false)) {
      Logger.debug("Mailer enabled true, proceeding to send email.")
      EmailServices.sendEmail(claim)
    }
  }
}

object AsyncClaimSubmissionService {
  val GENERATED = "0100" //submitting
}
