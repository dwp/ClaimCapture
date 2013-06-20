package models.claim

import org.specs2.mutable.Specification
import models.domain._
import models.{MultiLineAddress, DayMonthYear}
import scala.Some
import services.submission.ClaimSubmission

class ClaimSubmissionSpec extends Specification {

  val yourDetails = YourDetails(title = "mr", firstName = "Phil", middleName = None, surname = "Smith",
    otherSurnames = Some("O'Dwyer"), None, nationality = "French",
    dateOfBirth = DayMonthYear(1, 1, 1963), maritalStatus = "m", alwaysLivedUK = "yes")

  val contactDetails = ContactDetails(address = MultiLineAddress(Some("Line1"), None, None),
    postcode = Some("PR2 8AE"),
    phoneNumber = Some("01772 700806"), None)

  val timeOutsideUK = TimeOutsideUK(currentlyLivingInUK = "no", arrivedInUK = Some(DayMonthYear()),
    originCountry = None, planToGoBack = Some("yes"), whenPlanToGoBack = None,
    visaReference = None)

  val claimDate = ClaimDate(dateOfClaim = DayMonthYear(1, 1, 2013))

  "Claim Submission" should {
    "build and confirm normal AboutYou input" in {
      val aboutYou = AboutYou(yourDetails, contactDetails, timeOutsideUK, claimDate)
      val claimSub = ClaimSubmission(aboutYou)
      val claimXml = claimSub.createClaimSubmission

      (claimXml \\ "Claimant" \\ "Title").text mustEqual yourDetails.title
      (claimXml \\ "Claimant" \\ "OtherNames").text mustEqual s"${yourDetails.firstName} "
      (claimXml \\ "Claimant" \\ "OtherSurnames").text mustEqual yourDetails.otherSurnames.get
      (claimXml \\ "Claimant" \\ "DateOfClaim").text mustEqual claimDate.dateOfClaim.toXmlString
    }
  }
}
