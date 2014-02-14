package services.submission

import play.api.libs.ws.{Response, WS}
import play.api.libs.ws
import scala.concurrent.{ExecutionContext, Future}
import play.Configuration
import scala.xml.Elem
import play.api.{http, Logger}
import services.util.CharacterStripper
import java.net.ConnectException
import ExecutionContext.Implicits.global

class WebserviceFormSubmission extends FormSubmission {

  def submitClaim(claimSubmission: Elem): Future[ws.Response] = {
    val submissionServerEndpoint: String =
      Configuration.root().getString("submissionServerUrl", "SubmissionServerEndpointNotSet") + "submit/claim"
    Logger.debug(s"Submission Server : $submissionServerEndpoint")
    val result = WS.url(submissionServerEndpoint)
      .withHeaders(("Content-Type", "text/xml"))
      .post(CharacterStripper.stripNonPdf(claimSubmission.buildString(stripComments = true))) recover {

      case e: ConnectException =>
        Logger.error(s"ConnectException ! ${e.getMessage}")
        // Spoof service unavailable
        new Response(null) {
          override def status: Int = http.Status.SERVICE_UNAVAILABLE
        }
    }
    result
  }
}
