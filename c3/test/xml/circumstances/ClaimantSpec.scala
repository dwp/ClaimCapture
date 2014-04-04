package xml.circumstances

import org.specs2.mutable.{Tags, Specification}
import models.domain.{Claim, CircumstancesReportChange}
import models.DayMonthYear
import models.NationalInsuranceNumber
import scala.Some

class ClaimantSpec extends Specification with Tags {
  val nationalInsuranceNr = NationalInsuranceNumber(Some("VO"), Some("12"), Some("34"), Some("56"), Some("D"))
  val contact = "by post"
  val yourDetails = CircumstancesReportChange(
    fullName = "Mr Phil Joe Smith",
    nationalInsuranceNumber = nationalInsuranceNr,
    dateOfBirth = DayMonthYear(1, 1, 1963))

  "Claimant" should {
    "generate Claimant xml from a given circumstances" in {
      val claim = Claim().update(yourDetails)
      val xml = Claimant.xml(claim)

      (xml \\ "ClaimantDetails" \\ "FullName" \\ "Answer").text shouldEqual yourDetails.fullName
      (xml \\ "ClaimantDetails" \\ "DateOfBirth" \\ "Answer").text shouldEqual yourDetails.dateOfBirth.`dd-MM-yyyy`
      (xml \\ "ClaimantDetails" \\ "NationalInsuranceNumber" \\ "Answer").text shouldEqual nationalInsuranceNr.stringify
    }
  } section "unit"
}