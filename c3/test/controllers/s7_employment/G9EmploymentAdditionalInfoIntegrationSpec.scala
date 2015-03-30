package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.{TestData, PageObjects}
import utils.pageobjects.s7_employment.{G1EmploymentPage, G9EmploymentAdditionalInfoPage}
import utils.pageobjects.s8_self_employment.G4SelfEmploymentPensionsAndExpensesPage
import controllers.ClaimScenarioFactory
import utils.pageobjects.s9_other_money.G1AboutOtherMoneyPage
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage


class G9EmploymentAdditionalInfoIntegrationSpec extends Specification with Tags {
  "Employment Additional Info" should {
    "be presented" in new WithBrowser with PageObjects{
      val page =  G9EmploymentAdditionalInfoPage(context)
      page goToThePage()
    }

    "should be presented when self employment is answered yes and no employment" in new WithBrowser with PageObjects{
      val empData = new TestData
      empData.EmploymentHaveYouBeenEmployedAtAnyTime_0 = "No"
      empData.EmploymentHaveYouBeenSelfEmployedAtAnyTime = "Yes"

      val claimDatePage = G1ClaimDatePage(context) goToThePage()
      claimDatePage fillPageWith ClaimScenarioFactory.s12ClaimDate()
      claimDatePage submitPage()

      val empPage = G1EmploymentPage(context) goToThePage ()
      empPage fillPageWith empData
      empPage submitPage()

      val page =  G4SelfEmploymentPensionsAndExpensesPage(context)
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G9EmploymentAdditionalInfoPage]
    }

    "should present self employment pensions and expenses page when back is clicked" in new WithBrowser with PageObjects{
      val page =  G4SelfEmploymentPensionsAndExpensesPage(context)
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G9EmploymentAdditionalInfoPage]

      val pensionsAndExpensesPage = nextPage goBack()
      pensionsAndExpensesPage must beAnInstanceOf[G4SelfEmploymentPensionsAndExpensesPage]

    }

    "should present other money page on successful submission" in new WithBrowser with PageObjects{
      val page = G9EmploymentAdditionalInfoPage(context) goToThePage()
      page fillPageWith ClaimScenarioFactory.s7EmploymentAdditionalInfo

      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

  }
}
