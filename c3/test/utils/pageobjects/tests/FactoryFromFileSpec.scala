package utils.pageobjects.tests

import org.specs2.mutable.Specification
import utils.pageobjects.{ClaimScenario, FactoryFromFile}

/**
 * Unit test of FactoryFromFile
 * @author Jorge Migueis
 *         Date: 16/07/2013
 */
class FactoryFromFileSpec extends Specification {

  "The Factory from file" should {

    "be able to build a claim from a valid csv file" in {
      val claim = new ClaimScenario
      FactoryFromFile.buildFromFileLast2Columns("/unit_tests/tests.csv", claim.updateDynamic)

      claim.AboutYouHaveYouSubletYourHome mustEqual "value 1"
      claim.AboutYouWhatIsYourVisaReferenceNumber mustEqual "value 2"
      claim.AboutYouAddress mustEqual "value 3"
      claim.AboutYouAllOtherSurnamesorFamilyNames mustEqual "value 4"
      claim.AboutYouDateofBirth mustEqual "value 5"
      claim.AboutYouAreYouCurrentlyLivingintheUk must beNull[String]
      claim.EmploymentHowManyHoursAWeekYouNormallyWork mustEqual "13"
      claim.EmploymentCareExpensesWhatRelationIsToYou mustEqual "Partner"
    }

  }

}
