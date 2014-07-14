package services.submission

import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.ws
import play.Configuration
import play.api.{http, Logger}
import play.api.libs.ws.WS
import services.util.CharacterStripper
import java.net.ConnectException
import ExecutionContext.Implicits.global
import java.util.concurrent.TimeoutException
import controllers.submission._
import play.api.libs.ws.Response
import models.domain.Claim
import play.api.i18n.Lang
import xml.DWPBody

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
        .withHeaders(("Content-Type", "text/xml"))
        .withHeaders(("CarersClaimLang",claim.lang.getOrElse(new Lang("en")).language))
        .post(claimSubmission.buildString(stripComments = false)) recover {

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
