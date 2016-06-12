package monitoring

import app.ConfigProperties
import gov.dwp.carers.CADSHealthCheck
import gov.dwp.carers.CADSHealthCheck.Result
import models.view.ClaimHandling
import play.Configuration
import play.api.http.Status
import utils.HttpWrapper
import scala.language.{implicitConversions, postfixOps}
import app.ConfigProperties._

/**
 * Try to ping the ClaimReceived service in IL3.
 */
class ClaimReceivedConnectionCheck extends CADSHealthCheck(ClaimHandling.C3NAME, ClaimHandling.C3VERSION_VALUE) {

  override def check(): Result = {
    val url = Configuration.root().getString("submissionServerUrl", "SubmissionServerEndpointNotSet") + "ping"
    val timeout = getIntProperty("cr.timeout")
    val httpWrapper = new HttpWrapper
    val response = httpWrapper.get(url, timeout)
    response.getStatus match {
      case Status.OK => Result.healthy
      case status@_ => Result.unhealthy(s"Claim Received ping failed: $status from $url with timeout $timeout.")
    }
  }
}
