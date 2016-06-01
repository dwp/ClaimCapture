package controllers.s_pay_details

import org.specs2.mutable._
import play.api.Logger
import utils.WithBrowser
import controllers.{ClaimScenarioFactory, BrowserMatchers, Formulate}
import utils.pageobjects.s_about_you.{GNationalityAndResidencyPage, GPaymentsFromAbroadPage}
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.s_pay_details.GHowWePayYouPage
import utils.pageobjects.s_information.GAdditionalInfoPage
import utils.pageobjects.your_income.GStatutorySickPayPage
import utils.pageobjects.{Page, TestData, PageObjects, PageObjectsContext}

class GHowWePayYouIntegrationSpec extends Specification {
  section("integration", models.domain.PayDetails.id)
  "Pay details" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = GHowWePayYouPage(context)
      page goToThePage()
      page must beAnInstanceOf[GHowWePayYouPage]
    }

    "be hidden when having state pension" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.yourDetails(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.paymentsFromAbroad(browser)

      browser.goTo(GHowWePayYouPage.url)
      urlMustEqual(GAdditionalInfoPage.url)
    }

    "contain errors on invalid submission" in new WithBrowser with PageObjects {
      val page = GHowWePayYouPage(context)
      page goToThePage()
      val samePage = page.submitPage()
      Logger.info(samePage.toString)
      samePage.listErrors.nonEmpty must beTrue
    }

    "navigate to next page on valid submission" in new WithBrowser with BrowserMatchers {
      Formulate.howWePayYou(browser)
      urlMustEqual(GAdditionalInfoPage.url)
    }

    /**
     * This test case has been modified to be in line with the new Page Object pattern.
     * Please modify the other test cases when you address them
     */
    "navigate back to How we pay you" in new WithBrowser with PageObjects {
      val page = GHowWePayYouPage(context)
      val claim = ClaimScenarioFactory.s9OtherIncome

      page goToThePage()
      page fillPageWith claim
      page submitPage()

      val otherStatutoryPage = page goToPage new GHowWePayYouPage(PageObjectsContext(browser))
      otherStatutoryPage fillPageWith claim
      val nextPage = otherStatutoryPage submitPage()

      val previousPage = nextPage goBack()
      previousPage must beAnInstanceOf[GHowWePayYouPage]
    }

    "navigate to 'Consent And Declaration'" in new WithBrowser with PageObjects {
      val page = GHowWePayYouPage(context)
      val claim = ClaimScenarioFactory.s6PayDetails()
      page goToThePage()
      page fillPageWith claim
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GAdditionalInfoPage]
    }

    "navigate to 'Consent And Declaration'" in new WithBrowser with PageObjects {
      val page = GHowWePayYouPage(context)
      val claim = ClaimScenarioFactory.s6PayDetails()
      claim.HowWePayYouHowWouldYouLikeToGetPaid = "yes"
      page goToThePage()
      page fillPageWith claim

      val bankBuildingSocietyDetailsPage = page submitPage()
      val bankDetailsClaim = ClaimScenarioFactory.s6BankBuildingSocietyDetails()
      bankBuildingSocietyDetailsPage fillPageWith bankDetailsClaim
      val nextPage = bankBuildingSocietyDetailsPage submitPage()

      nextPage.submitPage() must beAnInstanceOf[GAdditionalInfoPage]
    }

    "be hidden when age is past 65 years at the claim date" in new WithBrowser with PageObjects {
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate

      val page = claimDatePage submitPage()

      val nextPage = goToHowWePayYouPage(context, page)

      nextPage.submitPage() must beAnInstanceOf[GAdditionalInfoPage]
    }

    "show bank page when claimant is less than 65 years at the claim date" in new WithBrowser with PageObjects {
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = new TestData
      claimDate.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "10/10/2012"

      claimDatePage fillPageWith claimDate

      val page = claimDatePage submitPage()

      val nextPage = goToHowWePayYouPage(context, page)

      nextPage.submitPage() must beAnInstanceOf[GHowWePayYouPage]
    }
  }
  section("integration", models.domain.PayDetails.id)

  def goToHowWePayYouPage(context:PageObjectsContext, aboutYouPage:Page): Page = {
      val page = aboutYouPage
      val claim = ClaimScenarioFactory.s2AboutYouWithTimeOutside()

      page goToThePage()

      page fillPageWith claim
      page submitPage()

      val nationalityAndResidencyPage = page goToPage new GNationalityAndResidencyPage(context)
      nationalityAndResidencyPage fillPageWith claim
      nationalityAndResidencyPage submitPage()

      val eeaPage = nationalityAndResidencyPage goToPage new GPaymentsFromAbroadPage(context)
      eeaPage fillPageWith claim
      eeaPage submitPage()

      val nextPage = eeaPage goToPage(new GHowWePayYouPage(context), throwException = false)

      nextPage
    }
}
