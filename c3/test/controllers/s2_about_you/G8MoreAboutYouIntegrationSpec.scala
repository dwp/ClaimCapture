package controllers.s2_about_you

import play.api.test.WithBrowser
import org.specs2.mutable.{Tags, Specification}
import controllers.{BrowserMatchers, ClaimScenarioFactory, Formulate}
import utils.pageobjects.s2_about_you._
import utils.pageobjects.PageObjects
import utils.pageobjects.s3_your_partner.G1YourPartnerPersonalDetailsPage

class G8MoreAboutYouIntegrationSpec extends Specification with Tags {
  "More About You" should {

    "contain the completed forms" in new WithBrowser with PageObjects{
			val page =  G1YourDetailsPage(context)
      val claim = ClaimScenarioFactory.s2AboutYouWithTimeOutside
      page goToThePage()

      page runClaimWith (claim, G8MoreAboutYouPage.title)
      page numberSectionsCompleted() mustEqual 5
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.abroadForMoreThan52Weeks(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      browser.goTo("/about-you/more-about-you")
      titleMustEqual("More about you - About you - the carer")
      browser.submit("button[type='submit']")

      browser.find("p[class=error]").size mustEqual 3
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects{
			val page =  G1YourDetailsPage(context)
      val claim = ClaimScenarioFactory.s2AboutYouWithTimeOutside
      page goToThePage()
      page runClaimWith (claim, G1YourPartnerPersonalDetailsPage.title)
    }

    "have you had a spouse must be visble when you navigate back" in new WithBrowser with PageObjects{
      val page =  G1YourDetailsPage(context)
      val claim = ClaimScenarioFactory.s2AboutYouWithTimeOutside
      page goToThePage()
      page runClaimWith (claim, G1YourPartnerPersonalDetailsPage.title)
      page goBack()
      browser.find("#hadPartnerSinceClaimDate").size() mustEqual 1
    }


  } section("integration", models.domain.AboutYou.id)
}