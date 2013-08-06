package services.submission

import scala.xml.Elem
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.ws.Response
import ExecutionContext.Implicits.global
import play.api.{Logger, http}

class MockClaimSubmission extends ClaimSubmission {

  def submitClaim(claimSubmission: Elem): Future[Response] = {
    Logger.info(s"Claim submitting mock transactionId : ${claimSubmission \\ "DWPCAClaim" \ "@id" toString()}")

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

  def retryClaim(claimRetry: Elem): Future[Response] = {

    val resp = new Response(null) {
      override lazy val body: String = <xml></xml>.buildString(stripComments = false)
    }
    Future(resp)
  }
}

