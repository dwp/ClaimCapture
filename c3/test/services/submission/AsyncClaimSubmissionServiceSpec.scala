package services.submission

import org.specs2.mutable.{Tags, Specification}
import services.ClaimTransactionComponent
import play.api.http
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import play.api.libs.ws.Response
import models.domain.Claim
import org.specs2.mock.Mockito
import models.view.CachedClaim


class AsyncClaimSubmissionServiceSpec extends Specification with Mockito with Tags with CachedClaim
with AsyncClaimSubmissionService
with ClaimTransactionComponent
with WebServiceClientComponent {

  val webServiceClient = mock[WebServiceClient]
  val claimTransaction = mock[ClaimTransaction]

  "claim submission should record the correct status based on WS call results" should {
    "record BAD_REQUEST" in {
      pending
      webServiceClient.submitClaim(any[Claim], any[String]) returns
        Future(new Response(null) {
          override def toString: String = "Dummy response"
          override def status: Int = http.Status.BAD_REQUEST
        }
        )

      claimTransaction.generateId returns "TEST333"

      submission(newInstance)

      there was one(claimTransaction).updateStatus("TEST333", BAD_REQUEST_ERROR, 1)
    }
  } section "unit"
}
