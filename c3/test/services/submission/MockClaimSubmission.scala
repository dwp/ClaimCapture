package services.submission

import scala.xml.Elem
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.ws.Response
import ExecutionContext.Implicits.global

class MockClaimSubmission extends ClaimSubmission {

  def submitClaim(claimSubmission: Elem): Future[Response] = {

    val resp = new Response(null) {
      override lazy val body: String = <xml></xml>.buildString(stripComments = false)
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

