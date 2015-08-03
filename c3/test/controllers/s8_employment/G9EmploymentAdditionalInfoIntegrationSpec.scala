package controllers.s8_employment

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import utils.pageobjects.{PageObjectsContext, TestData, PageObjects}
import utils.pageobjects.s8_employment.{G2BeenEmployedPage, G8PensionAndExpensesPage, G1EmploymentPage, G9EmploymentAdditionalInfoPage}
import utils.pageobjects.s_self_employment.GSelfEmploymentPensionsAndExpensesPage
import controllers.ClaimScenarioFactory
import utils.pageobjects.s9_other_money.G1AboutOtherMoneyPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import controllers.ClaimScenarioFactory._


class G9EmploymentAdditionalInfoIntegrationSpec extends Specification with Tags {
  "Employment Additional Info" should {
    "be presented" in new WithBrowser with PageObjects{
      val page =  G9EmploymentAdditionalInfoPage(context)
      page goToThePage()
    }

    "should be presented when self employment is answered yes and no employment" in new WithBrowser with PageObjects{

      val claim = ClaimScenarioFactory.s7SelfEmployedNotEmployed()

      val claimDatePage = GClaimDatePage(context) goToThePage()
      claimDatePage fillPageWith claim
      claimDatePage submitPage()

      val empPage = G1EmploymentPage(context) goToThePage ()
      empPage fillPageWith claim
      empPage submitPage()

      val page =  GSelfEmploymentPensionsAndExpensesPage(context)
      page goToThePage()
      page fillPageWith ClaimScenarioFactory.s9SelfEmployment

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G9EmploymentAdditionalInfoPage]
    }

    "should present self employment pensions and expenses page when back is clicked" in new WithBrowser with PageObjects{
      val page =  GSelfEmploymentPensionsAndExpensesPage(context)
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G9EmploymentAdditionalInfoPage]

      val pensionsAndExpensesPage = nextPage goBack()
      pensionsAndExpensesPage must beAnInstanceOf[GSelfEmploymentPensionsAndExpensesPage]

    }

    "should present other money page on successful submission" in new WithBrowser with PageObjects{
      val page = G9EmploymentAdditionalInfoPage(context) goToThePage()
      page fillPageWith ClaimScenarioFactory.s7EmploymentAdditionalInfo

      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    "should be presented when self employment is answered no and employment yes" in new WithBrowser with PageObjects{
      val claim = ClaimScenarioFactory.s7EmployedNotSelfEmployed()

      val claimDatePage = GClaimDatePage(context) goToThePage()
      claimDatePage fillPageWith ClaimScenarioFactory.s12ClaimDate()
      claimDatePage submitPage()

      val empPage = G1EmploymentPage(context) goToThePage ()
      empPage fillPageWith claim
      val jobDetailsPage = empPage submitPage()
      jobDetailsPage fillPageWith s7Employment()
      val lastWagePage = jobDetailsPage submitPage()
      val pensionsAndExpensesPage = lastWagePage fillPageWith s7Employment() submitPage()
      val beenEmployedPage = pensionsAndExpensesPage fillPageWith s7Employment() submitPage()

      val beenEmployedData = new TestData
      beenEmployedData.EmploymentHaveYouBeenEmployedAtAnyTime_1 = "No"

      beenEmployedPage fillPageWith beenEmployedData

      val nextPage = beenEmployedPage submitPage()

      nextPage must beAnInstanceOf[G9EmploymentAdditionalInfoPage]
    }

    "should be presented at the end of employment when self employment and employment is answered yes" in new WithBrowser with PageObjects{
      val beenEmployedData = new TestData
      beenEmployedData.EmploymentHaveYouBeenEmployedAtAnyTime_1 = "No"

      val beenEmployedPage = goToBeenEmployedPage(context)

      beenEmployedPage fillPageWith beenEmployedData

      val nextPage = beenEmployedPage submitPage()

      nextPage must beAnInstanceOf[G9EmploymentAdditionalInfoPage]
    }

    "should present Been employed page when back button is clicked" in new WithBrowser with PageObjects{
      val beenEmployedData = new TestData
      beenEmployedData.EmploymentHaveYouBeenEmployedAtAnyTime_1 = "No"

      val beenEmployedPage = goToBeenEmployedPage(context)

      beenEmployedPage fillPageWith beenEmployedData

      val nextPage = beenEmployedPage submitPage()

      nextPage must beAnInstanceOf[G9EmploymentAdditionalInfoPage]

      nextPage goBack() must beAnInstanceOf[G2BeenEmployedPage]
    }

    "should present other money page when displayed after employment section" in new WithBrowser with PageObjects{
      val beenEmployedData = new TestData
      beenEmployedData.EmploymentHaveYouBeenEmployedAtAnyTime_1 = "No"

      val beenEmployedPage = goToBeenEmployedPage(context)

      beenEmployedPage fillPageWith beenEmployedData

      val nextPage = beenEmployedPage submitPage()

      nextPage must beAnInstanceOf[G9EmploymentAdditionalInfoPage]

      val otherMoneyPage = nextPage fillPageWith s7EmploymentAdditionalInfo submitPage()

      otherMoneyPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

  }

  private def goToBeenEmployedPage(context:PageObjectsContext) = {
    val claim = ClaimScenarioFactory.s7SelfEmployedAndEmployed()

    val claimDatePage = GClaimDatePage(context) goToThePage()
    claimDatePage fillPageWith claim
    claimDatePage submitPage()

    val empPage = G1EmploymentPage(context) goToThePage ()
    empPage fillPageWith claim
    empPage submitPage()

    val page =  GSelfEmploymentPensionsAndExpensesPage(context)
    page goToThePage()
    page fillPageWith ClaimScenarioFactory.s9SelfEmployment

    val jobDetailsPage = page submitPage()
    jobDetailsPage fillPageWith s7Employment()
    val lastWagePage = jobDetailsPage submitPage()
    val pensionsAndExpensesPage = lastWagePage fillPageWith s7Employment() submitPage()
    val beenEmployedPage = pensionsAndExpensesPage fillPageWith s7Employment() submitPage()
    beenEmployedPage
  }

}
