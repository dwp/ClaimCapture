package utils.pageobjects.tests.xml_validation

import utils.pageobjects.xml_validation.XMLClaimBusinessValidation
import org.specs2.mutable.Specification
import controllers.ClaimScenarioFactory
import scala.io.Source
import utils.pageobjects.TestData


/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 23/07/2013
 */
class XMLBusinessValidationSpec extends Specification {
  "The XML Business Validator object" should {

    "be able to build the XML mapping from a valid csv file" in {
      val mapping = XMLClaimBusinessValidation buildXmlMappingFromFile "/unit_tests/tests_XMLMapping.csv"
      mapping("AboutYouAddress") mustEqual Tuple2("path3","question3")
      mapping("AboutYouAllOtherSurnamesorFamilyNames") mustEqual Tuple2("path4","question4, with comma")
      mapping("AboutYouDateofBirth") mustEqual Tuple2("path5","question5")
      mapping("EmploymentHowManyHoursAWeekYouNormallyWork") mustEqual Tuple2("PayStructure>WeeklyHoursWorked","How many hours a week [[past=did you]] [[present=do you]] normally work?")
      mapping("EmploymentCareExpensesWhatRelationIsToYou") mustEqual Tuple2("CareExpensesStructure>RelationshipCarerToClaimant","What relation, if any, is the person to you?")
    }
  }

  "The XML Business Validation class" should {
    "be able to parse a claim and raise exception with list errors if content xml differs from claim object" in {
      val validator = new XMLClaimBusinessValidation("/ClaimScenarioXmlMapping.csv")
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      val xml = Source.fromURL(getClass getResource "/unit_tests/Claim.xml").mkString

      val errors = validator.validateXMLClaim(claim, xml, throwException = false)
      errors.nonEmpty should beTrue
    }

//    "be able to parse a claim from a file and check XML content is valid" in {
//      val validator = new XMLBusinessValidation("/ClaimScenarioXmlMapping.csv")
//      val claim = ClaimScenario.buildClaimFromFile("/unit_tests/ClaimScenario_ClaimMickey.csv")
//      val xml = Source.fromURL(getClass getResource "/unit_tests/ClaimMickey.xml").mkString
//
//      val errors = validator.validateXMLClaim(claim, xml,throwException = true)
//      errors.isEmpty should beTrue
//    }
  }

}
