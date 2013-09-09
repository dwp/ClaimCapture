package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s8_self_employment._
import controllers.ClaimScenarioFactory
import utils.pageobjects.s2_about_you.G4ClaimDatePage
import utils.pageobjects.s3_your_partner.G4PersonYouCareForPage

class G9CompletedIntegrationSpec extends Specification with Tags {

  "Self Employment" should {
    "be presented" in new WithBrowser with G9CompletedPageContext {
      page goToThePage ()
    }

    "contain the completed forms" in new WithBrowser with G1AboutSelfEmploymentPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmployment
      page goToThePage()
      page fillPageWith claim
      val g2 = page submitPage()
      val g9 = g2 goToPage(new G9CompletedPage(browser))
      g9.listCompletedForms.size mustEqual 1
    }

    "navigate back to previous page" in new WithBrowser with G4SelfEmploymentPensionsAndExpensesPageContext  {
      val pageClaimDate = new G4ClaimDatePage(browser)
      pageClaimDate goToThePage()
      pageClaimDate fillPageWith ClaimScenarioFactory.s2AboutYouWithTimeOutside
      val pageMoreAboutYou = pageClaimDate.submitPage(throwException = true)
      pageMoreAboutYou fillPageWith ClaimScenarioFactory.s2AboutYouWithTimeOutside
      pageMoreAboutYou.submitPage(throwException = true)

      val pageAboutYourPartner = new G4PersonYouCareForPage(browser)
      pageAboutYourPartner goToThePage()
      pageAboutYourPartner fillPageWith ClaimScenarioFactory.s3YourPartnerNotThePersonYouCareFor
      pageAboutYourPartner.submitPage(throwException = true)

      val g4 = new G4SelfEmploymentPensionsAndExpensesPage(browser)
      g4 goToThePage()
      g4 fillPageWith ClaimScenarioFactory.s9SelfEmploymentExpensesRelatedToPersonYouCareFor
      val g5 = g4.submitPage(throwException = true)
      g5 fillPageWith ClaimScenarioFactory.s9SelfEmploymentExpensesRelatedToPersonYouCareFor
      val g7 = g5.submitPage(throwException = true)
      g7 fillPageWith ClaimScenarioFactory.s9SelfEmploymentExpensesRelatedToPersonYouCareFor

      val nextPage = g7.submitPage(throwException = true)
      nextPage must beAnInstanceOf[G9CompletedPage]

      nextPage.goBack() must beAnInstanceOf[G7ExpensesWhileAtWorkPage]
    }

    "navigate to next page on valid submission" in new WithBrowser with G9CompletedPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmploymentExpensesRelatedToPersonYouCareFor
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must not(beAnInstanceOf[G9CompletedPage])
    }
  } section("integration", models.domain.SelfEmployment.id)
}