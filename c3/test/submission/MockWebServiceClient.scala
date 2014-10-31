package submission

import play.api.libs.ws.WSResponse
import play.api.libs.ws.ning.NingWSResponse

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import play.api.{Logger, http}
import models.domain.Claim

class MockWebServiceClient extends FormSubmission {

  def submitClaim(claim: Claim, txnId:String): Future[WSResponse] = {
    Logger.info(s"Claim submitting mock transactionId : ${ txnId}")
    val resp =
      new NingWSResponse(null) {
        override def status: Int = http.Status.OK
        override lazy val body: String =
          getBodyString(txnId)
      }
    Future(resp)
  }

  def getBodyString(txnId: String): String = {
    txnId match {
      case "GOOD_SUBMIT" =>
        <response>
          <result>response</result>
          <correlationID>correlationID</correlationID>
          <pollEndpoint>pollEndpoint</pollEndpoint>
          <errorCode></errorCode>
        </response>
          .buildString(stripComments = false)
      case "ERROR_SUBMIT" =>
        <response>
          <result>error</result>
          <correlationID>correlationID</correlationID>
          <pollEndpoint>pollEndpoint</pollEndpoint>
          <errorCode>3001</errorCode>
        </response>
          .buildString(stripComments = false)
      case "RECOVER_SUBMIT" =>
        <response>
          <result>acknowledgement</result>
          <correlationID>correlationID</correlationID>
          <pollEndpoint>pollEndpoint</pollEndpoint>
          <errorCode></errorCode>
        </response>
          .buildString(stripComments = false)
      case "UNKNOWN_SUBMIT" =>
        <response>
          <result></result>
          <correlationID>correlationID</correlationID>
          <pollEndpoint>pollEndpoint</pollEndpoint>
          <errorCode></errorCode>
        </response>
          .buildString(stripComments = false)
    }
  }

}