package xml

import app.XMLValues._
import org.specs2.mutable.{Tags, Specification}
import models.domain._
import models.{DayMonthYear, MultiLineAddress, NationalInsuranceNumber}

class IdentificationSpec extends Specification with Tags {
  val nationalInsuranceNr = NationalInsuranceNumber(Some("VO"), Some("12"), Some("34"), Some("56"), Some("D"))


  val yourDetails = CircumstancesReportChange(title = "mr",
    firstName = "Phil",
    middleName = Some("Joe"),
    lastName = "Smith",
    nationalInsuranceNumber = nationalInsuranceNr,
    dateOfBirth = DayMonthYear(1, 1, 1963))

  "Identification" should {
    "generate Claimant xml from a given circumstances" in {
      val claim = Claim().update(yourDetails)
      val xml = CircsIdentification.xml(claim)

      (xml \\ "ClaimantDetails" \\ "Surname").text shouldEqual yourDetails.lastName
      (xml \\ "ClaimantDetails" \\ "OtherNames").text shouldEqual yourDetails.otherNames
      (xml \\ "ClaimantDetails" \\ "Title").text shouldEqual yourDetails.title
      (xml \\ "ClaimantDetails" \\ "DateOfBirth").text shouldEqual yourDetails.dateOfBirth.`yyyy-MM-dd`
      (xml \\ "ClaimantDetails" \\ "NationalInsuranceNumber").text shouldEqual nationalInsuranceNr.stringify
      (xml \\ "ClaimantDetails" \\ "ConfirmAddress").text shouldEqual NotAsked
      (xml \\ "ClaimantDetails" \\ "HomePhone").text shouldEqual NotAsked
      (xml \\ "ClaimantDetails" \\ "DaytimePhone" \\ "Number").text shouldEqual ""
      (xml \\ "ClaimantDetails" \\ "DaytimePhone" \\ "Qualifier").text shouldEqual ""
    }
  } section "unit"
}