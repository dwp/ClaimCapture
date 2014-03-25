package services.submission

import org.specs2.mutable.{Tags, Specification}
import services.{ DBTests, WithApplicationAndDB, ClaimTransactionComponent}
import play.api.http
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.ws.Response
import models.domain.Claim
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

  def asyncService( status:Int=200, result: String= "", correlationID: String="", messageClass:String="", errorCode: String="", pollEndpoint: String ="") =
    new AsyncClaimSubmissionService with ClaimTransactionComponent with WebServiceClientComponent {
      import ExecutionContext.Implicits.global
      val webServiceClient = mock[WebServiceClient]
      val response = mock[Response]
      response.status returns status
      response.body returns resultXml(result,correlationID,messageClass,errorCode,pollEndpoint).buildString(stripComments = false)

      webServiceClient.submitClaim(any[Claim],any[String]) returns Future(response)
      val claimTransaction = spy(new ClaimTransaction)
      org.mockito.Mockito.doReturn("1234567").when(claimTransaction).generateId
  }

  "claim submission should record the correct status based on WS call results" should {
    "record BAD_REQUEST" in new WithApplicationAndDB {
      val service = asyncService(http.Status.BAD_REQUEST)

      DBTests.createId("1234567")
      service.claimTransaction.registerId("1234567", "0000", 0)
      service.submission(mock[Claim])

      Thread.sleep(5000)
      val id = DBTests.getId("1234567")

      println("RETRIEVED VALUE:"+id)

      id must not beEmpty
    }
  }
}
