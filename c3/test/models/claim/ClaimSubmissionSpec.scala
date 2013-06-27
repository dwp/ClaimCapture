package models.claim

import org.specs2.mutable.Specification
import models.domain._
import services.submission.ClaimSubmission
import helpers.ClaimBuilder._
import play.api.test.WithApplication

class ClaimSubmissionSpec extends Specification {

  "Claim Submission" should {
    "build and confirm normal AboutYou input" in new WithApplication {
      val claim = Claim()
        .update(aboutYou.yourDetails)
        .update(aboutYou.claimDate)
        .update(aboutYou.contactDetails)
      val claimSub = ClaimSubmission(claim, "TY6TV9G")

      val claimXml = claimSub.buildDwpClaim

      (claimXml \\ "Claimant" \\ "Title").text mustEqual yourDetails.title
      (claimXml \\ "Claimant" \\ "OtherNames").text mustEqual s"${yourDetails.firstName} "
      (claimXml \\ "Claimant" \\ "OtherSurnames").text mustEqual yourDetails.otherSurnames.get
      (claimXml \\ "Claimant" \\ "DateOfClaim").text mustEqual claimDate.dateOfClaim.toXmlString
      (claimXml \\ "Claimant" \\ "Address" \\ "PostCode").text mustEqual contactDetails.postcode.get
      (claimXml \\ "Claimant" \\ "HomePhoneNumber").text mustEqual contactDetails.mobileNumber.getOrElse("") // holds mobile
    }
  }
}
