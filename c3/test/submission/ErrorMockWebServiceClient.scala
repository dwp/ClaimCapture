package submission

import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.ws.Response
import ExecutionContext.Implicits.global
import play.api.{Logger, http}
import services.UnavailableTransactionIdException
import models.domain.Claim

class ErrorMockWebServiceClient extends FormSubmission {

  def submitClaim(claim: Claim, txnId:String): Future[Response] = {
    Logger.info(s"Claim submitting mock transactionId : $txnId")
    val resp =
      txnId match {
        case "BAD_REQUEST" =>
          new Response(null) {
            override def status: Int = http.Status.BAD_REQUEST
          }
        case "TIMEOUT_REQUEST" =>
          new Response(null) {
            override def status: Int = http.Status.REQUEST_TIMEOUT
          }
        case "INTERNAL_ERROR" =>
          new Response(null) {
            override def status: Int = http.Status.INTERNAL_SERVER_ERROR
          }
        case "CONNECT_EXCEPTION" =>
          new Response(null) {
            override def status: Int = http.Status.SERVICE_UNAVAILABLE
          }
        case "TRANSACTION_ID_EXCEPTION" =>
          Logger.info("throw UnavailableTransactionIdException")
          throw new UnavailableTransactionIdException("Cannot generate an unique transaction ID.", new Exception)
      }
    Future(resp)
  }
}

