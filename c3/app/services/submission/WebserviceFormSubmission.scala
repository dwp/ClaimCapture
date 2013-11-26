package services.submission

import play.api.libs.ws.WS
import play.api.libs.ws
import play.api._
import scala.concurrent.Future
import scala.xml.Elem
import play.api.Logger

class WebserviceFormSubmission extends FormSubmission {

  def submitClaim(claimSubmission: Elem): Future[ws.Response] = {
    val ingressServerEndpoint: String =  Play.current.configuration.getString("ingressServerUrl").getOrElse("IngressServerEndpointNotSet") + "submission"
    Logger.debug(s"Ingress Service URL: $ingressServerEndpoint")
    val result = WS.url(ingressServerEndpoint)
      .withHeaders(("Content-Type", "text/xml"))
      .post(claimSubmission.buildString(stripComments = false))
    result
  }
}
