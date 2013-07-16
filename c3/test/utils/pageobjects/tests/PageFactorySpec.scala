package utils.pageobjects.tests

import org.specs2.mutable.Specification
import utils.pageobjects.PageFactory
import utils.pageobjects.ClaimScenario

/**
 * To change this template use Preferences | File and Code Templates.
 * @author Jorge Migueis
 *         Date: 12/07/2013
 */
class PageFactorySpec extends Specification {

  "The PageFactory" should {

    "be able to build teh XML mapping from a valid csv file" in {
      val mapping = PageFactory buildXmlMappingFromFile ("/tests_XMLMapping.csv")
      mapping("AboutYouHaveYouSubletYourHome")(0) mustEqual "path1"
      mapping("AboutYouWhatIsYourVisaReferenceNumber")(0) mustEqual "path2"
      mapping("AboutYouAddress")(0) mustEqual "path3"
      mapping("AboutYouAllOtherSurnamesorFamilyNames")(0) mustEqual "path4"
      mapping("AboutYouDateofBirth")(0) mustEqual "path5"
      //"AboutYouAreYouCurrentlyLivingintheUk" is ignored since not path associated to it
      mapping("EmploymentHowManyHoursAWeekYouNormallyWork")(0) mustEqual "PayStructure"
      mapping("EmploymentHowManyHoursAWeekYouNormallyWork")(1) mustEqual "WeeklyHoursWorked"
      mapping("EmploymentCareExpensesWhatRelationIsToYou")(0) mustEqual "CareExpensesStructure"
      mapping("EmploymentCareExpensesWhatRelationIsToYou")(1) mustEqual "RelationshipCarerToClaimant"
    }
  }

}
