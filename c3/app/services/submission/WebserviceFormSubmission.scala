package services.submission

import play.api.libs.ws.WS
import play.api.libs.ws
import play.api._
import scala.concurrent.Future
import scala.xml.Elem
import play.api.Logger
import services.util.CharacterStripper

trait WebserviceFormSubmission extends FormSubmission {

  def submitClaim(claimSubmission: Elem): Future[ws.Response] = {
    // Logger.info(s"Claim submitting transactionId : ${claimSubmission \\ "DWPCAClaim" \ "@id" toString()}") : Better to change this to debug : If debug turned off in production
    val ingressServerEndpoint: String =  Play.current.configuration.getString("ingressServerUrl").getOrElse("IngressServerEndpointNotSet") + "submit/claim"
    Logger.debug(s"Ingress Service URL: $ingressServerEndpoint")
    val result = WS.url(ingressServerEndpoint)
      .withHeaders(("Content-Type", "text/xml"))
      .post(CharacterStripper.stripNonPdf(claimSubmission.buildString(stripComments = true)))
    result
  }
}
