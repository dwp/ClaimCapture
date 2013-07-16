package services.submission

import play.api.libs.ws.WS
import play.api.libs.ws
import scala.concurrent.Future
import play.Configuration
import scala.xml.Elem
import play.api.Logger

object ClaimSubmissionService {
  def submitClaim(claimSubmission: Elem): Future[ws.Response] = {
    Logger.info(s"Claim submitting transactionId : ${claimSubmission \\ "DWPCAClaim" \ "@id" toString()}")
    val submissionServerEndpoint: String = Configuration.root().getString("submissionServerUrl")
    Logger.debug(s"Submission Server : $submissionServerEndpoint")
    val result = WS.url(submissionServerEndpoint)
      .withHeaders(("Content-Type", "text/xml"))
      .post(claimSubmission.buildString(stripComments = true))
    result
  }
}
