package controllers.s11_pay_details

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import controllers.{ClaimScenarioFactory, BrowserMatchers, Formulate}
import utils.pageobjects.s11_pay_details.{G1HowWePayYouPage, G2BankBuildingSocietyDetailsPage}
import utils.pageobjects.s2_about_you._
import utils.pageobjects.s10_information.G1AdditionalInfoPage
import app.AccountStatus
import utils.pageobjects.{TestData, Page, PageObjects, PageObjectsContext}
import utils.pageobjects.s_claim_date.GClaimDatePage

class G2BankBuildingSocietyDetailsIntegrationSpec extends Specification with Tags {
  "Bank building society details" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo(G2BankBuildingSocietyDetailsPage.url)
      urlMustEqual(G2BankBuildingSocietyDetailsPage.url)
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      browser.goTo(G2BankBuildingSocietyDetailsPage.url)
      urlMustEqual(G2BankBuildingSocietyDetailsPage.url)
      browser.submit("button[type='submit']")

      findMustEqualSize("div[class=validation-summary] ol li", 5)
    }

    "navigate back to Pay details - Pay Details" in new WithBrowser with BrowserMatchers {
      Formulate.howWePayYou(browser)
      urlMustEqual(G2BankBuildingSocietyDetailsPage.url)

      browser.goTo(G2BankBuildingSocietyDetailsPage.url)
      browser.click("#backButton")
      urlMustEqual(G1HowWePayYouPage.url)
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

    "be hidden when age is past 65 years at the claim date" in new WithBrowser with PageObjects{
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate

      val page = claimDatePage submitPage()

      val nextPage = goToBankBuildingSocietyPage(context,page)

      nextPage.submitPage() must beAnInstanceOf[G1AdditionalInfoPage]
    }

    "show bank page when claimant is less than 65 years at the claim date" in new WithBrowser with PageObjects{

      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = new TestData
      claimDate.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "10/10/2012"

      claimDatePage fillPageWith claimDate

      val page = claimDatePage submitPage()

      val nextPage = goToBankBuildingSocietyPage(context,page)

      nextPage.submitPage() must beAnInstanceOf[G2BankBuildingSocietyDetailsPage]
    }

    def goToBankBuildingSocietyPage(context:PageObjectsContext, aboutYouPage:Page):Page = {

      val page = aboutYouPage
      val claim = ClaimScenarioFactory.s2AboutYouWithTimeOutside()

      page goToThePage()

      page fillPageWith claim
      page submitPage ()

      val nationalityAndResidencyPage = page goToPage new G4NationalityAndResidencyPage(context)
      nationalityAndResidencyPage fillPageWith claim
      nationalityAndResidencyPage submitPage()

      val timeOutSideUKPage = nationalityAndResidencyPage goToPage new G5AbroadForMoreThan52WeeksPage(context, iteration = 1)
      timeOutSideUKPage fillPageWith claim
      timeOutSideUKPage submitPage()

      val eeaPage = timeOutSideUKPage goToPage new G7OtherEEAStateOrSwitzerlandPage(context)
      eeaPage fillPageWith claim
      eeaPage submitPage()

      val nextPage = eeaPage goToPage (new G2BankBuildingSocietyDetailsPage(context), throwException = false)

      nextPage
    }

  } section("integration", models.domain.PayDetails.id)
}