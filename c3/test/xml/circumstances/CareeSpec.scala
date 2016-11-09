package xml.circumstances

import javax.xml.bind.DatatypeConverter
import gov.dwp.carers.security.encryption.EncryptorAES
import models.domain.{CircumstancesYourDetails, Claim}
import models.view.CachedChangeOfCircs
import org.specs2.mutable._
import utils.WithApplication

/**
 * Created by neddakaltcheva on 3/14/14.
 */
class CareeSpec extends Specification {
  section("unit")
  "Careree" should {
    "generate Careree xml from a given circumstances" in new WithApplication {
      val yourDetails = CircumstancesYourDetails(
        theirFirstName = "Phil",
        theirSurname = "Smith",
        theirRelationshipToYou = "Wife of civil partner"
      )

      val claim = Claim(CachedChangeOfCircs.key).update(yourDetails)
      val xml = Caree.xml(claim)

      (xml \\ "CareeDetails" \\ "OtherNames" \\ "Answer").text shouldEqual yourDetails.theirFirstName
      (new  EncryptorAES).decrypt(DatatypeConverter.parseBase64Binary((xml \\ "CareeDetails" \\ "Surname" \\ "Answer").text)) shouldEqual yourDetails.theirSurname
      (xml \\ "CareeDetails" \\ "RelationToClaimant" \\ "Answer").text shouldEqual yourDetails.theirRelationshipToYou
    }
  }
  section("unit")
}
