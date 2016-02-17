package services.submission

import java.net.ConnectException
import java.util.concurrent.TimeoutException

import app.ConfigProperties
import gov.dwp.carers.xml.validation.XmlErrorHandler
import models.domain.Claim
import play.api.i18n.Lang
import play.api.libs.ws.ning.NingWSResponse
import play.api.libs.ws.{WSResponse, WS}
import play.api.{Logger, http}
import play.api.Play.current
import xml.ValidXMLBuilder
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try
import app.ConfigProperties._
import controllers.submission._
import scala.collection.JavaConverters._

trait WebServiceClientComponent {

  val webServiceClient: WebServiceClient

  class WebServiceClient {
    def submitClaim(claim: Claim, txnId: String): Future[WSResponse] = {
      Logger.info (s"Entered on submitClaim for : ${claim.key} : transactionId [$txnId].")
      val claimSubmission = ValidXMLBuilder ().xml (claim, txnId)
      Logger.debug ("Created xml")

      //validate xml before submitting
      val xmlErrors = Try(xmlValidator(claim).validate(claimSubmission.toString())).getOrElse(new XmlErrorHandler())
      if(xmlErrors.hasFoundErrorOrWarning) {
        Logger.error(s"Validation failed for transactionId [$txnId]")
        xmlErrors.getWarningAndErrors.asScala.foreach(error => Logger.error(s"Validation error: $error"))
        if(getProperty("submit.fail.on.validation", default = false)){
          throw new RuntimeException(s"Validation failed for transactionId [$txnId]")
        }
      }

      Logger.debug ("Validated xml")

      val submissionServerEndpoint = ConfigProperties.getProperty ("submissionServerUrl", "SubmissionServerEndpointNotSet") + "submission"
      Logger.debug (s"Submission Server : $submissionServerEndpoint")
      WS.url (submissionServerEndpoint)
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
    }
  }

}

object UnavailableResponse {

  def apply(): WSResponse = new NingWSResponse (null) {

    override def status: Int = http.Status.SERVICE_UNAVAILABLE

    override def statusText: String = ClaimSubmissionService.httpStatusCodes (status)
  }
}
