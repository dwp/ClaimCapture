package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain._
import models.{NationalInsuranceNumber, DayMonthYear, MultiLineAddress}

class ClaimantSpec extends Specification with Tags {
  val nationalInsuranceNr = NationalInsuranceNumber(Some("VO"), Some("12"), Some("34"), Some("56"), Some("D"))

  val nationalInsuranceNrOption = Some(nationalInsuranceNr)
  val yourDetails = YourDetails(title = "mr", firstName = "Phil", middleName = Some("Joe"), surname = "Smith",
                                otherSurnames = Some("O'Dwyer"), nationalInsuranceNumber = nationalInsuranceNrOption, nationality = "French",
                                dateOfBirth = DayMonthYear(1, 1, 1963), maritalStatus = "m", alwaysLivedUK = "yes")

  val contactDetails = ContactDetails(address = MultiLineAddress(Some("Line1"), None, None),
                                      postcode = Some("PR2 8AE"), phoneNumber = Some("01772 700806"), None)

  "Claimant" should {
    "generate Claimant xml from a given claim" in {
      val claim = Claim().update(ClaimDate(DayMonthYear(1, 1, 1999)))
                         .update(yourDetails).update(contactDetails)

      val claimantXml = Claimant.xml(claim)

      (claimantXml \\ "DateOfClaim").text shouldEqual "1999-01-01"
      (claimantXml \\ "Surname").text shouldEqual yourDetails.surname
      (claimantXml \\ "OtherNames").text shouldEqual yourDetails.otherNames
      (claimantXml \\ "Title").text shouldEqual yourDetails.title
      (claimantXml \\ "MaritalStatus").text shouldEqual yourDetails.maritalStatus
      (claimantXml \\ "DateOfBirth").text shouldEqual yourDetails.dateOfBirth.`yyyy-MM-dd`
      (claimantXml \\ "NationalInsuranceNumber").text shouldEqual nationalInsuranceNr.stringify
      (claimantXml \\ "Address" \\ "PostCode").text shouldEqual contactDetails.postcode.get
      (claimantXml \\ "DaytimePhoneNumber" \\ "Number").text shouldEqual contactDetails.phoneNumber.get
    }
  } section "unit"
}