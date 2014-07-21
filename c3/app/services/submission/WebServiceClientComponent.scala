package services.submission

import java.net.ConnectException
import java.util.concurrent.TimeoutException

import models.domain.Claim
import play.Configuration
import play.api.i18n.Lang
import play.api.libs.ws
import play.api.libs.ws.{Response, WS}
import play.api.{Logger, http}
import xml.DWPBody

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait WebServiceClientComponent {

  val webServiceClient: WebServiceClient

  class WebServiceClient {
    def submitClaim(claim: Claim, txnId: String): Future[ws.Response] = {
      Logger.info("Entered on submitClaim")
      val claimSubmission = DWPBody().xml(claim, txnId)
      Logger.info("Created xml")
      val submissionServerEndpoint: String =
        Configuration.root().getString("submissionServerUrl", "SubmissionServerEndpointNotSet") + "submission"
      Logger.info(s"Submission Server : $submissionServerEndpoint")
      val result = WS.url(submissionServerEndpoint)
        .withRequestTimeout(60000) // wait 1 minute
        .withHeaders(("Content-Type", "application/xml"))
        .withHeaders(("CarersClaimLang",claim.lang.getOrElse(new Lang("en")).language))
        .post(claimSubmission) recover {

        case e: ConnectException =>
          Logger.error(s"ConnectException ! transactionId [$txnId]")
          Logger.error(s"ConnectException ! ${e.getMessage}")
          // Spoof service unavailable
          // submission failed - remove from cache
          new Response(null) {
            override def status: Int = http.Status.SERVICE_UNAVAILABLE
          }
        case e: TimeoutException =>
          Logger.error(s"TimeoutException ! transactionId [$txnId]")
          Logger.error(s"TimeoutException ! ${e.getMessage}")
          // Spoof service unavailable
          // submission failed - remove from cache
          new Response(null) {
            override def status: Int = http.Status.SERVICE_UNAVAILABLE
          }
      }
      result
    }
  }

}
