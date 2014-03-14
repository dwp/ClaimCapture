package xml

import app.XMLValues._
import org.specs2.mutable.{Tags, Specification}
import models.domain._
import models.DayMonthYear
import models.domain.Claim
import models.NationalInsuranceNumber
import scala.Some
import xml.circumstances.CircsIdentification

class IdentificationSpec extends Specification with Tags {
  val nationalInsuranceNr = NationalInsuranceNumber(Some("VO"), Some("12"), Some("34"), Some("56"), Some("D"))
  val contact = "by post"
  val yourDetails = CircumstancesReportChange(
    fullName = "Mr Phil Joe Smith",
    nationalInsuranceNumber = nationalInsuranceNr,
    dateOfBirth = DayMonthYear(1, 1, 1963),
    theirFullName = "Betty Smith",
    theirRelationshipToYou = "Wife or civil partner")

  "Identification" should {
    "generate Claimant xml from a given circumstances" in {
      val claim = Claim().update(yourDetails)
      val xml = CircsIdentification.xml(claim)

      (xml \\ "ClaimantDetails" \\ "FullName").text shouldEqual yourDetails.fullName
      (xml \\ "ClaimantDetails" \\ "DateOfBirth").text shouldEqual yourDetails.dateOfBirth.`yyyy-MM-dd`
      (xml \\ "ClaimantDetails" \\ "NationalInsuranceNumber").text shouldEqual nationalInsuranceNr.stringify
      (xml \\ "CareeDetails" \\ "FullName").text shouldEqual yourDetails.theirFullName
      (xml \\ "CareeDetails" \\ "RelationToClaimant").text shouldEqual yourDetails.theirRelationshipToYou

    }.pendingUntilFixed("Pending till schema changes and modifying the code to new structure")
  } section "unit"
}