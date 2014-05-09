package services.submission

import scala.concurrent.ExecutionContext
import play.api.{http, Logger}
import controllers.submission._
import services.ClaimTransactionComponent
import ExecutionContext.Implicits.global
import play.api.libs.ws.Response
import models.domain.Claim
import ClaimSubmissionService._

trait AsyncClaimSubmissionService {

  this: ClaimTransactionComponent with WebServiceClientComponent =>

  def submission(claim: Claim): Unit = {
    val txnID = claim.transactionId.get
    Logger.info(s"Retrieved Id : $txnID")
    Logger.info("Making sure gets new version, webServiceClient:"+webServiceClient.toString)

    try{
      webServiceClient.submitClaim(claim, txnID).map(
        response => {
          Logger.info("Got response from WS:"+response)
          try{
            claimTransaction.updateStatus(txnID, SUBMITTED, claimType(claim))
            ClaimSubmissionService.recordMi(claim, txnID,claimTransaction.recordMi)
            processResponse(claim, txnID, response)
          }catch{
            case e:Exception => Logger.error(e.getMessage+" "+e.getStackTraceString)
          }
        }
      )
    }catch{
      case e:Exception => Logger.error("global error talking to ingress "+e.getMessage+" "+e.getStackTraceString)
    }
  }

  private def ok(claim: Claim, txnID: String, response: Response) = {
    Logger.info(s"Received OK : ${claim.key}")

    claimTransaction.updateStatus(txnID, SUCCESS, claimType(claim))
    Logger.info(s"Successful submission : ${claim.key} : $txnID")

  }

  private def processResponse(claim: Claim, txnID: String, response: Response): Unit = {
    Logger.debug("Response status is "+response.status)
    response.status match {
      case http.Status.OK => ok(claim,txnID,response)

      case http.Status.SERVICE_UNAVAILABLE =>
        Logger.error(s"SERVICE_UNAVAILABLE : ${response.status} , TxnId : $txnID")
        claimTransaction.updateStatus(txnID, SERVICE_UNAVAILABLE, claimType(claim))
      case http.Status.BAD_REQUEST =>
        Logger.error(s"BAD_REQUEST : ${response.status} , TxnId : $txnID")
        claimTransaction.updateStatus(txnID, BAD_REQUEST_ERROR, claimType(claim))
      case http.Status.REQUEST_TIMEOUT =>
        Logger.error(s"REQUEST_TIMEOUT : ${response.status} , TxnId : $txnID")
        claimTransaction.updateStatus(txnID, REQUEST_TIMEOUT_ERROR, claimType(claim))
      case http.Status.INTERNAL_SERVER_ERROR =>
        Logger.error(s"INTERNAL_SERVER_ERROR : ${response.status} , TxnId : $txnID")
        claimTransaction.updateStatus(txnID, SERVER_ERROR, claimType(claim))
    }
  }


}

object AsyncClaimSubmissionService{
  val GENERATED = "0100" //submitting
}
