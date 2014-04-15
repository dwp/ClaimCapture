package xml

import app.XMLValues._
import org.specs2.mutable.{Tags, Specification}
import models.domain._
import models.{DayMonthYear, MultiLineAddress, NationalInsuranceNumber}
import models.domain.Claim
import models.NationalInsuranceNumber
import scala.Some
import models.domain.Claim
import models.NationalInsuranceNumber
import scala.Some

class IdentificationSpec extends Specification with Tags {
  val nationalInsuranceNr = NationalInsuranceNumber(Some("VO"), Some("12"), Some("34"), Some("56"), Some("D"))


  val yourDetails = CircumstancesReportChange(
    fullName = "Mr Phil Joe Smith",
    nationalInsuranceNumber = nationalInsuranceNr,
    dateOfBirth = DayMonthYear(1, 1, 1963))

  "Identification" should {
    "generate Claimant xml from a given circumstances" in {
      val claim = Claim().update(yourDetails)
      val xml = CircsIdentification.xml(claim)

      (xml \\ "ClaimantDetails" \\ "Surname").text shouldEqual NotAsked
      (xml \\ "ClaimantDetails" \\ "OtherNames").text shouldEqual yourDetails.fullName
      (xml \\ "ClaimantDetails" \\ "Title").text shouldEqual NotAsked
      (xml \\ "ClaimantDetails" \\ "DateOfBirth").text shouldEqual yourDetails.dateOfBirth.`yyyy-MM-dd`
      (xml \\ "ClaimantDetails" \\ "NationalInsuranceNumber").text shouldEqual nationalInsuranceNr.stringify
      (xml \\ "ClaimantDetails" \\ "ConfirmAddress").text shouldEqual NotAsked
      (xml \\ "ClaimantDetails" \\ "HomePhone").text shouldEqual NotAsked
      (xml \\ "ClaimantDetails" \\ "DaytimePhone" \\ "Number").text shouldEqual ""
      (xml \\ "ClaimantDetails" \\ "DaytimePhone" \\ "Qualifier").text shouldEqual ""
    }
  } section "unit"
}