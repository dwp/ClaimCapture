package xml.circumstances

import org.specs2.mutable.{Tags, Specification}
import models.{DayMonthYear, NationalInsuranceNumber}
import models.domain.{Claim, CircumstancesReportChange}

/**
 * Created by neddakaltcheva on 3/14/14.
 */
class CareeSpec extends Specification with Tags {

  val yourDetails = CircumstancesReportChange(
    theirFullName = "Mr Phil Joe Smith",
    theirRelationshipToYou = "Wife of civil partner")

  "Careree" should {
    "generate Careree xml from a given circumstances" in {
      val claim = Claim().update(yourDetails)
      val xml = Caree.xml(claim)

      (xml \\ "CareeDetails" \\ "FullName" \\ "Answer").text shouldEqual yourDetails.theirFullName
      (xml \\ "CareeDetails" \\ "RelationToClaimant" \\ "Answer").text shouldEqual yourDetails.theirRelationshipToYou

    }
  } section "unit"
}