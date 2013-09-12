package xml

import app.XMLValues._
import org.specs2.mutable.{Tags, Specification}
import models.domain._
import models.DayMonthYear
import models.domain.Circs
import models.MultiLineAddress
import models.NationalInsuranceNumber
import scala.Some

class ClaimantSpec extends Specification with Tags {
  val nationalInsuranceNr = NationalInsuranceNumber(Some("VO"), Some("12"), Some("34"), Some("56"), Some("D"))

  val nationalInsuranceNrOption = nationalInsuranceNr

  val yourDetails = CircumstancesAboutYou(title = "mr",
    firstName = "Phil",
    middleName = Some("Joe"),
    lastName = "Smith",
    nationalInsuranceNumber = nationalInsuranceNrOption,
    dateOfBirth = DayMonthYear(1, 1, 1963))

  val contactDetails = CircumstancesYourContactDetails(address = MultiLineAddress(Some("Line1"), None, None),
    postcode = Some("PR2 8AE"),
    phoneNumber = Some("01772 700806"),
    mobileNumber = Some("01818 118181"))

  "Claimant" should {
    "generate Claimant xml from a given circumstances" in {
      val claim = Circs()().update(ClaimDate(DayMonthYear(1, 1, 1999)))
        .update(yourDetails).update(contactDetails)

      val claimantXml = Claimant.xml(claim.asInstanceOf[Circs])

      (claimantXml \\ "DateOfClaim").text shouldEqual "1999-01-01"
      (claimantXml \\ "Surname").text shouldEqual yourDetails.lastName
      (claimantXml \\ "OtherNames").text shouldEqual yourDetails.otherNames
      (claimantXml \\ "OtherSurnames").text shouldEqual NotAsked
      (claimantXml \\ "Title").text shouldEqual yourDetails.title
      (claimantXml \\ "MaritalStatus").text shouldEqual NotAsked
      (claimantXml \\ "DateOfBirth").text shouldEqual yourDetails.dateOfBirth.`yyyy-MM-dd`
      (claimantXml \\ "NationalInsuranceNumber").text shouldEqual nationalInsuranceNr.stringify
      (claimantXml \\ "Address" \\ "PostCode").text shouldEqual contactDetails.postcode.get
      (claimantXml \\ "ConfirmAddress").text shouldEqual yes
      (claimantXml \\ "HomePhoneNumber").text shouldEqual contactDetails.phoneNumber.get
      (claimantXml \\ "DaytimePhoneNumber" \\ "Number").text shouldEqual contactDetails.mobileNumber.get
      (claimantXml \\ "DaytimePhoneNumber" \\ "Qualifier").text shouldEqual "mobile"
      (claimantXml \\ "ClaimedBefore").text shouldEqual NotAsked
    }
  } section "unit"
}