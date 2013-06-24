package services.submission

import models.domain._
import play.api.libs.ws.WS
import play.api.libs.ws
import scala.concurrent.Future
import play.api.Logger

object ClaimSubmissionService {

  def submitClaim(claim: Claim): Future[ws.Response] = {
    val aboutYou = buildAboutYou(claim)
    val claimSubmission = ClaimSubmission(aboutYou).createClaimSubmission
    Logger.debug(claimSubmission.buildString(stripComments = true).substring(0, 50))
    val result = WS.url("http://localhost:19001/submit")
      .withHeaders(("Content-Type", "text/xml"))
      .post(claimSubmission.buildString(stripComments = true))
    result
  }

  private def buildAboutYou(claim: Claim) = {
    val yourDetails = claim.questionGroup(YourDetails.id).asInstanceOf[Option[YourDetails]].get
    val contactDetails = claim.questionGroup(ContactDetails.id).asInstanceOf[Option[ContactDetails]].get
    val timeOutsideUK = claim.questionGroup(TimeOutsideUK.id).asInstanceOf[Option[TimeOutsideUK]]
    val claimDate = claim.questionGroup(ClaimDate.id).asInstanceOf[Option[ClaimDate]].get
    AboutYou(yourDetails, contactDetails, timeOutsideUK, claimDate)
  }
}
