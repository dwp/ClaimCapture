package services.submission

import play.api.libs.ws
import scala.concurrent.Future
import scala.xml.Elem

trait FormSubmission {
  def submitClaim(claimSubmission: Elem): Future[ws.Response]
}
