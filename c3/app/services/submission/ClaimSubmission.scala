package services.submission

import play.api.libs.ws
import scala.concurrent.Future
import scala.xml.Elem

trait ClaimSubmission {

  def submitClaim(claimSubmission: Elem): Future[ws.Response]

  def retryClaim(claimRetry: Elem): Future[ws.Response]
}
