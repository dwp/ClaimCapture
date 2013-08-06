package services.submission

import scala.xml.Elem
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.ws.Response
import ExecutionContext.Implicits.global
import play.api.{Logger, http}

class MockClaimSubmission extends ClaimSubmission {

  def submitClaim(claimSubmission: Elem): Future[Response] = {
    val txnId: String = claimSubmission \\ "DWPCAClaim" \ "@id" toString()
    Logger.info(s"Claim submitting mock transactionId : ${ txnId}")

    val resp =
      new Response(null) {
        override def status: Int = http.Status.OK
        override lazy val body: String =
          getBodyString(txnId)
      }
    Future(resp)
  }


  def getBodyString(txnId: String): String = {
    txnId match {
      case "TEST223" => {
        <response>
          <result>response</result>
          <correlationID>correlationID</correlationID>
          <pollEndpoint>pollEndpoint</pollEndpoint>
          <errorCode></errorCode>
        </response>
          .buildString(stripComments = false)
      }
      case "TEST224" => {
        <response>
          <result>error</result>
          <correlationID>correlationID</correlationID>
          <pollEndpoint>pollEndpoint</pollEndpoint>
          <errorCode>3001</errorCode>
        </response>
          .buildString(stripComments = false)
      }
      case "TEST225" => {
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

  def retryClaim(claimRetry: Elem): Future[Response] = {

    val resp = new Response(null) {
      override lazy val body: String = <xml></xml>.buildString(stripComments = false)
    }
    Future(resp)
  }
}

