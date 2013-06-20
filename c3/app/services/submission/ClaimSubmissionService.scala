package services.submission

import models.domain._

object ClaimSubmissionService {

  def submitClaim(claim:Claim) = {
    val aboutYou = buildAboutYou(claim)
    val claimSubmission = ClaimSubmission(aboutYou).createClaimSubmission
    println(claimSubmission)
    true
  }

  private def buildAboutYou(claim: Claim) = {
    val yourDetails = claim.questionGroup(YourDetails.id).asInstanceOf[Option[YourDetails]].get
    val contactDetails = claim.questionGroup(ContactDetails.id).asInstanceOf[Option[ContactDetails]].get
    val timeOutsideUK = claim.questionGroup(TimeOutsideUK.id).asInstanceOf[Option[TimeOutsideUK]].get
    val claimDate = claim.questionGroup(ClaimDate.id).asInstanceOf[Option[ClaimDate]].get
    AboutYou(yourDetails, contactDetails, timeOutsideUK, claimDate)
  }
}
