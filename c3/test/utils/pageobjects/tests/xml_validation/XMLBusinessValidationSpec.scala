package utils.pageobjects.tests.xml_validation

import utils.pageobjects.xml_validation.XMLBusinessValidation
import org.specs2.mutable.Specification
import controllers.ClaimScenarioFactory
import scala.io.Source
import utils.pageobjects.ClaimScenario


/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 23/07/2013
 */
class XMLBusinessValidationSpec extends Specification {
  "The XML Business Validator object" should {

    "be able to build the XML mapping from a valid csv file" in {
      val mapping = XMLBusinessValidation buildXmlMappingFromFile "/unit_tests/tests_XMLMapping.csv"
      mapping("AboutYouHaveYouSubletYourHome") mustEqual "path1"
      mapping("AboutYouWhatIsYourVisaReferenceNumber") mustEqual "path2"
      mapping("AboutYouAddress") mustEqual "path3"
      mapping("AboutYouAllOtherSurnamesorFamilyNames") mustEqual "path4"
      mapping("AboutYouDateofBirth") mustEqual "path5"
      mapping("EmploymentHowManyHoursAWeekYouNormallyWork") mustEqual "PayStructure>WeeklyHoursWorked"
      mapping("EmploymentCareExpensesWhatRelationIsToYou") mustEqual "CareExpensesStructure>RelationshipCarerToClaimant"
    }
  }

  "The XML Business Validation class" should {
    "be able to parse a claim and raise exception with list errors if content xml differs from claim object" in {
      val validator = new XMLBusinessValidation("/ClaimScenarioXmlMapping.csv")
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      val xml = Source.fromURL(getClass getResource "/unit_tests/Claim.xml").mkString

      val errors = validator.validateXMLClaim(claim, xml, throwException = false)
      errors.nonEmpty must beTrue
      errors(0) mustEqual "AboutYouFirstName Claimant>OtherNames value expected: [john] value read: [mickey]"
    }

    "be able to parse a claim from a file and check XML content is valid" in {
      val validator = new XMLBusinessValidation("/ClaimScenarioXmlMapping.csv")
      val claim = ClaimScenario.buildClaimFromFile("/unit_tests/ClaimScenario_ClaimMickey.csv")
      val xml = Source.fromURL(getClass getResource "/unit_tests/ClaimMickey.xml").mkString

      val errors = validator.validateXMLClaim(claim, xml,throwException = true)
      errors.isEmpty must beTrue
    }
  }

}
