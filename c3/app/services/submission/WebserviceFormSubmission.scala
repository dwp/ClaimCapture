package services.submission

import app.ConfigProperties._
import play.api.libs.ws.WS
import play.api.libs.ws
import scala.concurrent.Future
import scala.xml.Elem
import play.api.Logger

class WebserviceFormSubmission extends FormSubmission {

  def submitClaim(claimSubmission: Elem): Future[ws.Response] = {
    val ingressServerEndpoint: String =  getProperty("ingressServerUrl","IngressServerEndpointNotSet") + "submission"
    Logger.debug(s"Ingress Service URL: $ingressServerEndpoint")
    val result = WS.url(ingressServerEndpoint)
      .withHeaders(("Content-Type", "text/xml"))
      .post(claimSubmission.buildString(stripComments = false))
    result
  }
}
