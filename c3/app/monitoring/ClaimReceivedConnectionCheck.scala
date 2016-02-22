package monitoring

import app.ConfigProperties
import gov.dwp.carers.CADSHealthCheck
import gov.dwp.carers.CADSHealthCheck.Result
import models.view.ClaimHandling
import play.Configuration
import play.api.http.Status
import play.api.libs.ws.WSResponse
import utils.HttpUtils.HttpMethodWrapper
import scala.concurrent.duration._
import scala.language.{implicitConversions, postfixOps}

/**
 * Try to ping the ClaimReceived service in IL3.
 */
class ClaimReceivedConnectionCheck extends CADSHealthCheck(ClaimHandling.C3NAME, ClaimHandling.C3VERSION_VALUE) {

  implicit def stringGetWrapper(s:String):HttpMethodWrapper = new HttpMethodWrapper(s,ConfigProperties.getProperty("cr.timeout",60000).milliseconds)

  override def check(): Result = {
    val submissionServerEndpoint: String =
      Configuration.root().getString("submissionServerUrl", "SubmissionServerEndpointNotSet") + "ping"

    submissionServerEndpoint get { response: WSResponse =>
      response.status match {
        case Status.OK =>
          Result.healthy
        case status@_ =>
          Result.unhealthy(s"Claim Received ping failed: $status.")
      }
    }

  }

}
