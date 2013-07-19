package controllers.s8_other_money

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s8_other_money.{G4PersonContactDetailsPage, G4PersonContactDetailsPageContext}
import utils.pageobjects.ClaimScenario
import utils.pageobjects.s2_about_you.{ClaimDatePageContext, MoreAboutYouPageContext}
import controllers.ClaimScenarioFactory
import utils.pageobjects.s8_other_money.G1AboutOtherMoneyPageContext


class G4PersonContactDetailsIntegrationSpec extends Specification with Tags {

  "Person Contact Details" should {
    "be presented" in new WithBrowser with G4PersonContactDetailsPageContext {
      page goToThePage()
    }

    "not be presented if not claimed benefits" in new WithBrowser with ClaimDatePageContext {
      val claim = ClaimScenarioFactory.s2AboutYouWithTimeOutside()
      claim.AboutYouHaveYouOrYourPartnerSpouseClaimedorReceivedAnyOtherBenefits = "No"

      page goToThePage()
      page fillPageWith claim
      val moreAboutYouPage = page.submitPage()

      moreAboutYouPage fillPageWith claim
      val nextPage = moreAboutYouPage.submitPage()

      nextPage.goToPage(new G4PersonContactDetailsPage(browser))

    }

    "contain errors on invalid submission" in new WithBrowser with G4PersonContactDetailsPageContext {
      val claim = new ClaimScenario
      claim.OtherMoneyPostCode = "INVALID"
      page goToThePage()
      page fillPageWith claim
      val pageWithErrors = page.submitPage()
      pageWithErrors.listErrors.size mustEqual 1
    }

    "contain the completed forms" in new WithBrowser with G1AboutOtherMoneyPageContext {
      val claim = ClaimScenarioFactory.s8otherMoneyG1AboutOtherMoney()
      page goToThePage()
      page fillPageWith claim
      val moneyPaidPage = page submitPage()
      val personContactPage = moneyPaidPage.goToPage(new G4PersonContactDetailsPage(browser))
      personContactPage.listCompletedForms.size mustEqual 1
    }

//    "navigate back to Your Partner Personal Details" in new WithBrowser {
//      //      browser.goTo("/yourPartner/contactDetails")
//      //      browser.click("#backButton")
//      //      browser.title mustEqual "Personal Details - Your Partner"
//      pending
//    }
//
//    "navigate to next page on valid submission" in new WithBrowser {
//      pending
//    }

  } section "integration"
}
