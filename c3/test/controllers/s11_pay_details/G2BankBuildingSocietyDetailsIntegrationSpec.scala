package controllers.s11_pay_details

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{ClaimScenarioFactory, BrowserMatchers, Formulate}
import utils.pageobjects.s11_pay_details.{G1HowWePayYouPage, G2BankBuildingSocietyDetailsPage}
import utils.pageobjects.s2_about_you._
import utils.pageobjects.s10_information.G1AdditionalInfoPage
import app.AccountStatus
import utils.pageobjects.{PageObjects, PageObjectsContext}
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage

class G2BankBuildingSocietyDetailsIntegrationSpec extends Specification with Tags {
  "Bank building society details" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/pay-details/bank-building-society-details")
      titleMustEqual("Bank/Building society details - Pay details")
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      browser.goTo("/pay-details/bank-building-society-details")
      titleMustEqual("Bank/Building society details - Pay details")
      browser.submit("button[type='submit']")

      findMustEqualSize("div[class=validation-summary] ol li", 6)
    }

    "navigate back to Pay details - Pay Details" in new WithBrowser with BrowserMatchers {
      Formulate.howWePayYou(browser)
      titleMustEqual("Bank/Building society details - Pay details")

      browser.goTo("/pay-details/bank-building-society-details")
      browser.click("#backButton")
      titleMustEqual("How would you like to get paid? - Pay details")
    }

    "navigate to 'Consent And Declaration'" in new WithBrowser with PageObjects{
			val page =  G1HowWePayYouPage(context)
      val claim = ClaimScenarioFactory.s6PayDetails()
      claim.HowWePayYouHowWouldYouLikeToGetPaid = AccountStatus.BankBuildingAccount
      page goToThePage ()
      page fillPageWith claim

      val bankBuildingSocietyDetailsPage = page submitPage()
      val bankDetailsClaim = ClaimScenarioFactory.s6BankBuildingSocietyDetails()
      bankBuildingSocietyDetailsPage fillPageWith bankDetailsClaim
      val nextPage = bankBuildingSocietyDetailsPage submitPage()

      nextPage.submitPage() must beAnInstanceOf[G1AdditionalInfoPage]
    }

    "be hidden when having state pension" in new WithBrowser with PageObjects{

			val page = new G1YourDetailsPage(PageObjectsContext(browser))
      val claim = ClaimScenarioFactory.s2AboutYouWithTimeOutside()

      page goToThePage()

      page fillPageWith claim
      page submitPage ()

      val nationalityAndResidencyPage = page goToPage new G4NationalityAndResidencyPage(PageObjectsContext(browser))
      nationalityAndResidencyPage fillPageWith claim
      nationalityAndResidencyPage submitPage()

      val timeOutSideUKPage = nationalityAndResidencyPage goToPage new G5AbroadForMoreThan52WeeksPage(PageObjectsContext(browser), iteration = 1)
      timeOutSideUKPage fillPageWith claim
      timeOutSideUKPage submitPage()

      val eeaPage = timeOutSideUKPage goToPage new G7OtherEEAStateOrSwitzerlandPage(PageObjectsContext(browser))
      eeaPage fillPageWith claim
      eeaPage submitPage()

      val nextPage = eeaPage goToPage (new G2BankBuildingSocietyDetailsPage(PageObjectsContext(browser)), throwException = false)

      nextPage.submitPage() must beAnInstanceOf[G1AdditionalInfoPage]
    }

  } section("integration", models.domain.PayDetails.id)
}