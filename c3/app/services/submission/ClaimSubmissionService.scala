package services.submission

import play.api.libs.ws.WS
import play.api.libs.ws
import scala.concurrent.Future
import play.Configuration
import scala.xml.Elem

object ClaimSubmissionService {
  def submitClaim(claimSubmission: Elem): Future[ws.Response] = {
    val result = WS.url(Configuration.root().getString("submissionServerUrl"))
      .withHeaders(("Content-Type", "text/xml"))
      .post(claimSubmission.buildString(stripComments = true))
    result
  }
}
