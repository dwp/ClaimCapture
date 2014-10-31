package monitoring

import app.ConfigProperties
import com.codahale.metrics.health.HealthCheck
import com.codahale.metrics.health.HealthCheck.Result
import play.Configuration
import play.api.http.Status
import play.api.libs.ws.WSResponse
import utils.HttpUtils.HttpMethodWrapper

import scala.concurrent.duration._
import scala.language.{implicitConversions, postfixOps}



/**
 * Try to ping the ClaimReceived service in IL3.
 */
class ClaimReceivedConnectionCheck extends HealthCheck {

  implicit def stringGetWrapper(s:String) = new HttpMethodWrapper(s,ConfigProperties.getProperty("cr.timeout",60000).milliseconds)

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
