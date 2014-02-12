package submission

import scala.xml.Elem
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.ws.Response
import ExecutionContext.Implicits.global
import play.api.{Logger, http}
import services.submission.FormSubmission

class MockFormSubmission extends FormSubmission {

  def submitClaim(claimSubmission: Elem): Future[Response] = {
    val txnId = claimSubmission \ "@id" toString()
    Logger.info(s"Claim submitting mock transactionId : ${ txnId}")
    val resp =
      new Response(null) {
        override def status: Int = http.Status.OK
        override lazy val body: String =
          getBodyString(txnId)
      }
    Future(resp)
  }

  def retryClaim(claimRetry: Elem): Future[Response] = {
    println("retryClaim")
    val resp = new Response(null) {
      override def status: Int = http.Status.OK
      override lazy val body: String =
        <response>
          <result>response</result>
          <correlationID>correlationID</correlationID>
          <pollEndpoint>pollEndpoint</pollEndpoint>
          <errorCode></errorCode>
        </response>
          .buildString(stripComments = false)
    }
    Future(resp)
  }

  def getBodyString(txnId: String): String = {
    txnId match {
      case "TEST223" =>
        <response>
          <result>response</result>
          <correlationID>correlationID</correlationID>
          <pollEndpoint>pollEndpoint</pollEndpoint>
          <errorCode></errorCode>
        </response>
          .buildString(stripComments = false)
      case "TEST224" =>
        <response>
          <result>error</result>
          <correlationID>correlationID</correlationID>
          <pollEndpoint>pollEndpoint</pollEndpoint>
          <errorCode>3001</errorCode>
        </response>
          .buildString(stripComments = false)
      case "TEST225" =>
        <response>
          <result>acknowledgement</result>
          <correlationID>correlationID</correlationID>
          <pollEndpoint>pollEndpoint</pollEndpoint>
          <errorCode></errorCode>
        </response>
          .buildString(stripComments = false)
    }
  }

}

