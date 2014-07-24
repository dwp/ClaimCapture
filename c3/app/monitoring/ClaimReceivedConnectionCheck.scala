package monitoring

import app.ConfigProperties
import com.codahale.metrics.health.HealthCheck
import com.codahale.metrics.health.HealthCheck.Result
import play.Configuration
import play.api.http
import play.api.http.Status
import play.api.libs.ws.Response
import utils.HttpUtils.HttpMethodWrapper

import scala.concurrent.duration._
import scala.language.{implicitConversions, postfixOps}



/**
 * Try to ping the ClaimReceived service in IL3.
 */
class ClaimReceivedConnectionCheck extends HealthCheck {
  override def check(): Result = {
    val submissionServerEndpoint: String =
      Configuration.root().getString("submissionServerUrl", "SubmissionServerEndpointNotSet") + "ping"

    new HttpMethodWrapper(submissionServerEndpoint, ConfigProperties.getProperty("cr.timeout",60000).milliseconds)  get { response: Response =>
      response.status match {
        case Status.OK =>
          Result.healthy
        case _ =>
          Result.unhealthy(s"Claim Received ping failed: ${http.Status.toString}.")
      }
    }

  }

}
