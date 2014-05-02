package services.submission

import org.specs2.mutable.{Tags, Specification}
import services.{TransactionStatus, DBTests, WithApplicationAndDB, ClaimTransactionComponent}
import play.api.http
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.ws.Response
import models.domain.{FullClaim, Claim}
import org.specs2.mock.Mockito
import models.view.CachedClaim
import play.api.test.FakeApplication


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

  def asyncService( status:Int=200, transactionId:String, result: String= "",
                    correlationID: String="", messageClass:String="",
                    errorCode: String="", pollEndpoint: String ="") = new AsyncClaimSubmissionService with ClaimTransactionComponent
                                                                                                      with WebServiceClientComponent {
      import ExecutionContext.Implicits.global
      val webServiceClient = mock[WebServiceClient]
      val response = mock[Response]
      response.status returns status
      response.body returns resultXml(result,correlationID,messageClass,errorCode,pollEndpoint).buildString(stripComments = false)

      webServiceClient.submitClaim(any[Claim],any[String]) returns Future(response)
      val claimTransaction = new ClaimTransaction
  }

  val transactionId = "1234567"


  "claim submission" should {
    "record BAD_REQUEST" in new WithApplicationAndDB {

      val service = asyncService(http.Status.BAD_REQUEST,transactionId)

      val claim = new Claim(transactionId = Some(transactionId)) with FullClaim

      serviceSubmission(service, claim)

      Thread.sleep(500)
      val transactionStatus = service.claimTransaction.getTransactionStatusById(transactionId)

      transactionStatus mustEqual Some(TransactionStatus(transactionId,ClaimSubmissionService.BAD_REQUEST_ERROR,1,Some(0),None,Some("en")))

    }

    "record SUCCESS" in new WithApplicationAndDB {

      val service = asyncService(http.Status.OK,transactionId,result = "response")

      val claim = new Claim(transactionId = Some(transactionId)) with FullClaim

      serviceSubmission(service, claim)

      Thread.sleep(500)
      val transactionStatus = service.claimTransaction.getTransactionStatusById(transactionId)

      transactionStatus mustEqual Some(TransactionStatus(transactionId,ClaimSubmissionService.SUCCESS,1,Some(0),None,Some("en")))

    }

    "record SERVICE_UNAVAILABLE" in new WithApplicationAndDB {

      val service = asyncService(http.Status.SERVICE_UNAVAILABLE,transactionId)

      val claim = new Claim(transactionId = Some(transactionId)) with FullClaim

      serviceSubmission(service, claim)

      Thread.sleep(500)
      val transactionStatus = service.claimTransaction.getTransactionStatusById(transactionId)

      transactionStatus mustEqual Some(TransactionStatus(transactionId,ClaimSubmissionService.SERVICE_UNAVAILABLE,1,Some(0),None,Some("en")))

    }

    "record REQUEST_TIMEOUT_ERROR" in new WithApplicationAndDB {

      val service = asyncService(http.Status.REQUEST_TIMEOUT,transactionId)

      val claim = new Claim(transactionId = Some(transactionId)) with FullClaim

      serviceSubmission(service, claim)

      Thread.sleep(500)
      val transactionStatus = service.claimTransaction.getTransactionStatusById(transactionId)

      transactionStatus mustEqual Some(TransactionStatus(transactionId,ClaimSubmissionService.REQUEST_TIMEOUT_ERROR,1,Some(0),None,Some("en")))

    }

    "record SERVER_ERROR" in new WithApplicationAndDB {

      val service = asyncService(http.Status.INTERNAL_SERVER_ERROR,transactionId)

      val claim = new Claim(transactionId = Some(transactionId)) with FullClaim

      serviceSubmission(service, claim)

      Thread.sleep(500)
      val transactionStatus = service.claimTransaction.getTransactionStatusById(transactionId)

      transactionStatus mustEqual Some(TransactionStatus(transactionId,ClaimSubmissionService.SERVER_ERROR,1,Some(0),None,Some("en")))

    }
  } section "unit"


  def serviceSubmission(service: AsyncClaimSubmissionService with ClaimTransactionComponent, claim: Claim)(implicit app: FakeApplication) {
    DBTests.createId(transactionId)
    service.claimTransaction.registerId(transactionId, ClaimSubmissionService.SUBMITTED, controllers.submission.claimType(claim), true)
    service.submission(claim)
  }
}
