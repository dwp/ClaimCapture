package services.submission

import java.net.ConnectException
import java.util.concurrent.TimeoutException

import app.ConfigProperties
import models.domain.Claim
import play.api.i18n.Lang
import play.api.libs.ws.ning.NingWSResponse
import play.api.libs.ws.{WSResponse, WS}
import play.api.{Logger, http}
import play.api.Play.current
import xml.ValidXMLBuilder


import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait WebServiceClientComponent {

  val webServiceClient: WebServiceClient

  class WebServiceClient {
    def submitClaim(claim: Claim, txnId: String): Future[WSResponse] = {
      Logger.info (s"Entered on submitClaim for : ${claim.key} : transactionId [$txnId].")
      val claimSubmission = ValidXMLBuilder ().xml (claim, txnId)
      Logger.debug ("Created xml")
      val submissionServerEndpoint = ConfigProperties.getProperty ("submissionServerUrl", "SubmissionServerEndpointNotSet") + "submission"
      Logger.info (s"Submission Server : $submissionServerEndpoint")
      val result = WS.url (submissionServerEndpoint)
        .withRequestTimeout (ConfigProperties.getProperty ("cr.timeout", 60000)) // wait 1 minute
        .withHeaders (("Content-Type", "application/xml"))
        .withHeaders (("CarersClaimLang", claim.lang.getOrElse (new Lang ("en")).language))
        .post (claimSubmission) recover {

        case e: ConnectException =>
          Logger.error (s"ConnectException for transactionId [$txnId]: ${e.getMessage}")
          // Spoof service unavailable
          // submission failed - remove from cache
          UnavailableResponse ()

        case e: TimeoutException =>
          Logger.error (s"TimeoutException for transactionId [$txnId]: ${e.getMessage}")
          // Spoof service unavailable
          // submission failed - remove from cache
          UnavailableResponse ()
      }
      result
    }
  }

}

object UnavailableResponse {

  def apply(): WSResponse = new NingWSResponse (null) {

    override def status: Int = http.Status.SERVICE_UNAVAILABLE

    override def statusText: String = ClaimSubmissionService.httpStatusCodes (status)
  }
}