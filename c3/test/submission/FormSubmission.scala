package submission

import play.api.libs.ws
import scala.concurrent.Future
import models.domain.Claim

trait FormSubmission {
  def submitClaim(claim: Claim, txnId:String): Future[ws.Response]
}
