package submission

import scala.xml.Elem
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.ws.Response
import ExecutionContext.Implicits.global
import play.api.{Logger, http}
import services.submission.FormSubmission
import services.UnavailableTransactionIdException
import java.net.ConnectException

class ErrorMockFormSubmission extends FormSubmission {

  def submitClaim(claimSubmission: Elem): Future[Response] = {
    val txnId = claimSubmission \ "@id" toString()
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
          Logger.info("throw ConnectException")
          throw new ConnectException("Duff")
        case "TRANSACTION_ID_EXCEPTION" =>
          Logger.info("throw UnavailableTransactionIdException")
          throw new UnavailableTransactionIdException("Cannot generate an unique transaction ID.", new Exception)
      }
    Future(resp)
  }
}

