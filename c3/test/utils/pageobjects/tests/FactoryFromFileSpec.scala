package utils.pageobjects.tests

import org.specs2.mutable._
import utils.pageobjects.{TestData, FactoryFromFile}

/**
 * Unit test of FactoryFromFile
 * @author Jorge Migueis
 *         Date: 16/07/2013
 */
class FactoryFromFileSpec extends Specification {
  section("unit")
  "The Factory from file" should {
    "be able to build a claim from a valid csv file" in {
      val claim = new TestData
      FactoryFromFile.buildFromFileLast2Columns("/unit_tests/tests.csv", claim.updateDynamic)

      claim.AboutYouAllOtherSurnamesorFamilyNames mustEqual "value 4"
      claim.AboutYouDateofBirth mustEqual "value 5"
      claim.AboutYouAreYouCurrentlyLivingintheUk must beNull[String]
      claim.EmploymentHowManyHoursAWeekYouNormallyWork mustEqual "13"
      claim.EmploymentCareExpensesWhatRelationIsToYou mustEqual "Partner"
    }
  }
  section("unit")
}
