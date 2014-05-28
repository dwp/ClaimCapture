package services.submission

import scala.concurrent.ExecutionContext
import play.api.{http, Logger}
import controllers.submission._
import services.ClaimTransactionComponent
import ExecutionContext.Implicits.global
import play.api.libs.ws.Response
import models.domain.Claim
import ClaimSubmissionService._

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

    try{
      // TODO: If  submission fails, remove the key from the cache
      webServiceClient.submitClaim(claim, txnID).map(
        response => {
          Logger.debug("Got response from WS:" + response)
          try {
            claimTransaction.updateStatus(txnID, SUBMITTED, claimType(claim))
            ClaimSubmissionService.recordMi(claim, txnID, claimTransaction.recordMi)
            processResponse(claim, txnID, response)
          } catch {
            case e: Exception => Logger.error(e.getMessage + " " + e.getStackTraceString)
          }
        }
      )
    }catch{
      case e:Exception =>
        Logger.error(s"INTERNAL_SERVER_ERROR TxnId : $txnID")
        Logger.error("global error talking to ingress "+e.getMessage+" "+e.getStackTraceString)
        claimTransaction.updateStatus(txnID, SERVER_ERROR, claimType(claim))
    }
  }

  private def ok(claim: Claim, txnID: String, response: Response) = {
    val responseStr = response.body
    Logger.info(s"Received response : ${claim.key} : $responseStr")
    val responseXml = scala.xml.XML.loadString(responseStr)
    val result = (responseXml \\ "result").text
    Logger.info(s"Received result : ${claim.key} : $result")
    result match {
      case "response" =>
        claimTransaction.updateStatus(txnID, SUCCESS, claimType(claim))
        Logger.info(s"Successful submission : ${claim.key} : $txnID")
      case "acknowledgement" =>
        claimTransaction.updateStatus(txnID, ACKNOWLEDGED, claimType(claim))
        Logger.info(s"Successful submission : ${claim.key} : $txnID")
      case "error" =>
        val errorCode = (responseXml \\ "errorCode").text
        Logger.error(s"Received error : $result, TxnId : $txnID, Error code : $errorCode")
        claimTransaction.updateStatus(txnID, errorCode, claimType(claim))
      case _ =>
        Logger.error(s"Received error : $result, TxnId : $txnID, Error code : $UNKNOWN_ERROR")
        claimTransaction.updateStatus(txnID, UNKNOWN_ERROR, claimType(claim))
    }
  }

  private def processResponse(claim: Claim, txnID: String, response: Response): Unit = {
    Logger.debug("Response status is " + response.status)
    response.status match {
      case http.Status.OK => ok(claim, txnID, response)

      case http.Status.SERVICE_UNAVAILABLE =>
        Logger.error(s"SERVICE_UNAVAILABLE : ${response.status} : ${response.toString}, TxnId : $txnID")
        claimTransaction.updateStatus(txnID, SERVICE_UNAVAILABLE, claimType(claim))
      case http.Status.BAD_REQUEST =>
        Logger.error(s"BAD_REQUEST : ${response.status} : ${response.toString}, TxnId : $txnID")
        claimTransaction.updateStatus(txnID, BAD_REQUEST_ERROR, claimType(claim))
      case http.Status.REQUEST_TIMEOUT =>
        Logger.error(s"REQUEST_TIMEOUT : ${response.status} : ${response.toString}, TxnId : $txnID")
        claimTransaction.updateStatus(txnID, REQUEST_TIMEOUT_ERROR, claimType(claim))
      case http.Status.INTERNAL_SERVER_ERROR =>
        Logger.error(s"INTERNAL_SERVER_ERROR : ${response.status} : ${response.toString}, TxnId : $txnID")
        claimTransaction.updateStatus(txnID, SERVER_ERROR, claimType(claim))
    }
  }
}

object AsyncClaimSubmissionService {
  val GENERATED = "0100" //submitting
}
