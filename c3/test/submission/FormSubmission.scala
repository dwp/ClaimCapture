package submission

import play.api.libs.ws
import scala.concurrent.Future
import scala.xml.Elem
import models.domain.Claim

trait FormSubmission {
  def submitClaim(claim: Claim, txnId:String): Future[ws.Response]
}
