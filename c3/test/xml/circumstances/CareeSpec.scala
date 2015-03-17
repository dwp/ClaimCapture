package xml.circumstances

import javax.xml.bind.DatatypeConverter

import gov.dwp.carers.security.encryption.EncryptorAES
import models.domain.{CircumstancesReportChange, Claim}
import models.view.CachedChangeOfCircs
import org.specs2.mutable.{Specification, Tags}

/**
 * Created by neddakaltcheva on 3/14/14.
 */
class CareeSpec extends Specification with Tags {

  val yourDetails = CircumstancesReportChange(
    theirFullName = "Mr Phil Joe Smith",
    theirRelationshipToYou = "Wife of civil partner")

  "Careree" should {
    "generate Careree xml from a given circumstances" in {
      val claim = Claim(CachedChangeOfCircs.key).update(yourDetails)
      val xml = Caree.xml(claim)

      (new  EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary((xml \\ "CareeDetails" \\ "FullName" \\ "Answer").text)) shouldEqual yourDetails.theirFullName
      (xml \\ "CareeDetails" \\ "RelationToClaimant" \\ "Answer").text shouldEqual yourDetails.theirRelationshipToYou

    }
  } section "unit"
}