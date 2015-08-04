package controllers.s_pay_details

import org.specs2.mutable.{Tags, Specification}
import play.api.Logger
import utils.WithBrowser
import controllers.{ClaimScenarioFactory, BrowserMatchers, Formulate}
import utils.pageobjects.s_other_money._
import utils.pageobjects.s_pay_details.{GBankBuildingSocietyDetailsPage, GHowWePayYouPage}
import utils.pageobjects.s_information.GAdditionalInfoPage
import utils.pageobjects.{PageObjects, PageObjectsContext}

class GHowWePayYouIntegrationSpec extends Specification with Tags {
  "Pay details" should {
    "be presented" in new WithBrowser with PageObjects {
      val page =  GAboutOtherMoneyPage(context)
      page goToThePage()
    }

    "be hidden when having state pension" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.yourDetails(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)

      browser.goTo(GHowWePayYouPage.url)
      urlMustEqual(GAdditionalInfoPage.url)
    }

    "contain errors on invalid submission" in new WithBrowser with PageObjects  {
      val page =  GAboutOtherMoneyPage(context)
      page goToThePage()
      val samePage = page.submitPage()
      Logger.info(samePage.toString)
      samePage.listErrors.nonEmpty must beTrue
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.howWePayYou(browser)
      urlMustEqual(GBankBuildingSocietyDetailsPage.url)
    }

    /**
     * This test case has been modified to be in line with the new Page Object pattern.
     * Please modify the other test cases when you address them
     */
    "navigate back to Other Statutory Pay - Other Money" in new WithBrowser with PageObjects{
			val page =  GAboutOtherMoneyPage(context)
      val claim = ClaimScenarioFactory.s9otherMoney
      page goToThePage()
      page fillPageWith claim
      page submitPage()

      val OtherStatutoryPage = page goToPage new GAboutOtherMoneyPage(PageObjectsContext(browser))
      OtherStatutoryPage fillPageWith claim
      OtherStatutoryPage submitPage()

      val howWePayPage = OtherStatutoryPage goToPage new GHowWePayYouPage(PageObjectsContext(browser))
      val previousPage = howWePayPage goBack()
      previousPage must beAnInstanceOf[GAboutOtherMoneyPage]
    }

    "navigate to 'Consent And Declaration'" in new WithBrowser with PageObjects{
			val page =  GHowWePayYouPage(context)
      val claim = ClaimScenarioFactory.s6PayDetails()
      page goToThePage()
      page fillPageWith claim
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GAdditionalInfoPage]
    }

  } section("integration", models.domain.PayDetails.id)
}