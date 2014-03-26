package services.submission

import org.specs2.mutable.{Tags, Specification}
import services.{TransactionStatus, DBTests, WithApplicationAndDB, ClaimTransactionComponent}
import play.api.http
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.ws.Response
import models.domain.{FullClaim, Claim}
import org.specs2.mock.Mockito
import models.view.CachedClaim


class AsyncClaimSubmissionServiceSpec extends Specification with Mockito with Tags with CachedClaim {

  def resultXml(result: String, correlationID: String, messageClass:String, errorCode: String, pollEndpoint: String) = {
    <response>
      <result>{result}</result>
      <correlationID>{correlationID}</correlationID>
      <messageClass>{messageClass}</messageClass>
      <pollEndpoint>{pollEndpoint}</pollEndpoint>
      <errorCode>{errorCode}</errorCode>
    </response>
  }

  def asyncService( status:Int=200, transactionId:String, result: String= "", correlationID: String="", messageClass:String="", errorCode: String="", pollEndpoint: String ="") =
    new AsyncClaimSubmissionService with ClaimTransactionComponent with WebServiceClientComponent {
      import ExecutionContext.Implicits.global
      val webServiceClient = mock[WebServiceClient]
      val response = mock[Response]
      response.status returns status
      response.body returns resultXml(result,correlationID,messageClass,errorCode,pollEndpoint).buildString(stripComments = false)

      webServiceClient.submitClaim(any[Claim],any[String]) returns Future(response)
      val claimTransaction = spy(new ClaimTransaction)
      org.mockito.Mockito.doReturn(transactionId).when(claimTransaction).generateId
  }

  "claim submission should record the correct status based on WS call results" should {
    "record BAD_REQUEST" in new WithApplicationAndDB {
      val transactionId = "1234567"
      val service = asyncService(http.Status.BAD_REQUEST,transactionId)

      DBTests.createId(transactionId)
      service.submission(new Claim with FullClaim)

      Thread.sleep(5000)
      val transactionStatus = DBTests.getId(transactionId)

      transactionStatus must not beEmpty

      transactionStatus mustEqual Some(TransactionStatus(transactionId,AsyncClaimSubmissionService.BAD_REQUEST_ERROR,1,Some(0),None,Some("en")))

    }
  }

  "claim submission should record the correct status based on WS call results" should {
    "record SERVICE_UNAVAILABLE" in new WithApplicationAndDB {
      val transactionId = "1234567"
      val service = asyncService(http.Status.SERVICE_UNAVAILABLE,transactionId)

      DBTests.createId(transactionId)
      service.submission(new Claim with FullClaim)

      Thread.sleep(5000)
      val transactionStatus = DBTests.getId(transactionId)

      transactionStatus must not beEmpty

      transactionStatus mustEqual Some(TransactionStatus(transactionId,AsyncClaimSubmissionService.SERVICE_UNAVAILABLE,1,Some(0),None,Some("en")))

    }
  }

  "claim submission should record the correct status based on WS call results" should {
    "record REQUEST_TIMEOUT_ERROR" in new WithApplicationAndDB {
      val transactionId = "1234567"
      val service = asyncService(http.Status.REQUEST_TIMEOUT,transactionId)

      DBTests.createId(transactionId)
      service.submission(new Claim with FullClaim)

      Thread.sleep(5000)
      val transactionStatus = DBTests.getId(transactionId)

      transactionStatus must not beEmpty

      transactionStatus mustEqual Some(TransactionStatus(transactionId,AsyncClaimSubmissionService.REQUEST_TIMEOUT_ERROR,1,Some(0),None,Some("en")))

    }
  }

  "claim submission should record the correct status based on WS call results" should {
    "record SERVER_ERROR" in new WithApplicationAndDB {
      val transactionId = "1234567"
      val service = asyncService(http.Status.INTERNAL_SERVER_ERROR,transactionId)

      DBTests.createId(transactionId)
      service.submission(new Claim with FullClaim)

      Thread.sleep(5000)
      val transactionStatus = DBTests.getId(transactionId)

      transactionStatus must not beEmpty

      transactionStatus mustEqual Some(TransactionStatus(transactionId,AsyncClaimSubmissionService.SERVER_ERROR,1,Some(0),None,Some("en")))

    }
  }
}
