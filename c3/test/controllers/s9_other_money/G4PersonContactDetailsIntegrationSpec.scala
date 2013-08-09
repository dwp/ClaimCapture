package controllers.s9_other_money

import org.specs2.mutable.Specification
import org.specs2.mutable.Tags

import controllers.ClaimScenarioFactory
import utils.pageobjects.ClaimScenario
import utils.pageobjects.PageObjectException
import utils.pageobjects.s2_about_you.G4ClaimDatePageContext
import utils.pageobjects.s9_other_money._
import play.api.test.WithBrowser

class G4PersonContactDetailsIntegrationSpec extends Specification with Tags {

  "Person Contact Details" should {
    "be presented" in new WithBrowser with G4PersonContactDetailsPageContext {
      page goToThePage()
    }

    "be skipped if nobody had any money added to a benefit for you" in new WithBrowser with G2MoneyPaidToSomeoneElseForYouPageContext {
      val claim = ClaimScenarioFactory.s8otherMoney
      claim.OtherMoneyHasAnyoneHadMoneyForBenefitYouClaim = "no"
      page goToThePage()
      page fillPageWith claim
      page submitPage()
      val g4Page = page goToPage(new G4PersonContactDetailsPage(browser)) must throwA[PageObjectException]
    }

    "contain errors on invalid submission" in new WithBrowser with G4PersonContactDetailsPageContext {
      val claim = new ClaimScenario
      claim.OtherMoneyOtherPersonPostcode = "INVALID"
      page goToThePage()
      page fillPageWith claim
      val pageWithErrors = page.submitPage()
      pageWithErrors.listErrors.size mustEqual 1
    }

    "contain the completed forms" in new WithBrowser with G1AboutOtherMoneyPageContext {
      val claim = ClaimScenarioFactory.s8otherMoney
      page goToThePage()
      page fillPageWith claim
      val moneyPaidPage = page submitPage()
      val personContactPage = moneyPaidPage.goToPage(new G4PersonContactDetailsPage(browser))
      personContactPage.listCompletedForms.size mustEqual 1
    }

    "navigate back to Person Who Gets This Money" in new WithBrowser with G4ClaimDatePageContext {
      val claimS2 = ClaimScenarioFactory.s2AboutYouWithTimeOutside()
      page goToThePage()
      page fillPageWith claimS2
      val moreAboutYouPage = page.submitPage()
      moreAboutYouPage fillPageWith claimS2
      val employmentPage = moreAboutYouPage.submitPage()
      val claimS8 = ClaimScenarioFactory.s8otherMoney
      val s8g3 = employmentPage goToPage (new G3PersonWhoGetsThisMoneyPage(browser))
      s8g3 fillPageWith claimS8
      val s8g4 = s8g3 submitPage()
      val previousPage = s8g4 goBack()
      previousPage.pageTitle mustEqual "Person Who Gets This Money - Other Money"
      previousPage must beAnInstanceOf[G3PersonWhoGetsThisMoneyPage]
    }

    "navigate to next page on valid submission" in new WithBrowser with G4PersonContactDetailsPageContext {
      val claim = ClaimScenarioFactory.s8otherMoney
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G5StatutorySickPayPage]
    }
  } section("integration", models.domain.OtherMoney.id)
}