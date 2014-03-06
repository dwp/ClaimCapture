package submission

import scala.xml.Elem
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.ws.Response
import ExecutionContext.Implicits.global
import play.api.{Logger, http}
import services.submission.FormSubmission
import xml.XMLHelper
import org.joda.time.DateTime


class MockFormSubmission extends FormSubmission {

  def submitClaim(claimSubmission: Elem): Future[Response] = {
    val txnId: String = XMLHelper.extractIdFrom(claimSubmission)
    Logger.info(s"Claim submitting mock transactionId : ${ txnId}")

    val resp =
      new Response(null) {
        override def status: Int = http.Status.OK
        override lazy val body: String = getBodyString(txnId)
      }
    Future(resp)
  }

  def getBodyString(txnId: String): String = {
    txnId match {
      case "TEST223" => {
        <Response>
          <statusCode>0000</statusCode>
          <timestamp>{DateTime.now().toString()}</timestamp>
        </Response>
          .buildString(stripComments = false)
      }
      case "TEST224" => {
        <Response>
          <statusCode>2006</statusCode>
          <timestamp>{DateTime.now().toString()}</timestamp>
        </Response>
          .buildString(stripComments = false)
      }
      case transactionId:String => {
        <Response>
          <statusCode>unknown {transactionId}</statusCode>
          <timestamp>{DateTime.now().toString()}</timestamp>
        </Response>
          .buildString(stripComments = false)
      }
    }
  }

}

