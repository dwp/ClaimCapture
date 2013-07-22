package controllers.s2_about_you

import play.api.test.WithBrowser
import org.specs2.mutable.{Tags, Specification}
import controllers.{ClaimScenarioFactory, Formulate}
import utils.pageobjects.s2_about_you.{G6EmploymentPage, G5MoreAboutYouPage, G1YourDetailsPageContext, G5MoreAboutYouPageContext}
import utils.pageobjects.s1_carers_allowance.G1BenefitsPage

class G5MoreAboutYouIntegrationSpec extends Specification with Tags {

  "More About You" should {
    "present Benefits when there is no claim date" in new WithBrowser with G5MoreAboutYouPageContext {
      val landingPage = page goToThePage(throwException=false)
      landingPage must beAnInstanceOf[G1BenefitsPage]
    }

    "be presented when there is a claim date" in new WithBrowser {
      Formulate.claimDate(browser)
      browser.title mustEqual "More About You - About You"
    }

    "contain the completed forms" in new WithBrowser with G1YourDetailsPageContext {
      val claim = ClaimScenarioFactory.s2AboutYouWithTimeOutside
      page goToThePage()
      page runClaimWith (claim, G5MoreAboutYouPage.title)
      page numberSectionsCompleted() mustEqual 4
    }

    "contain questions with claim dates" in new WithBrowser {
      val dateString = "03/04/1950"
      Formulate.claimDate(browser)
      val h3 = browser.find("div[class=completed] ul li h3")
      h3.getText.contains(dateString) mustEqual true

      val questionLabels = browser.find("fieldset[class=form-elements] ul[class=group] > li > p")
      questionLabels.get(0).getText must contain(dateString)
      questionLabels.get(1).getText must contain(dateString)
      questionLabels.get(2).getText must contain(dateString)
    }

    "contain errors on invalid submission" in new WithBrowser {
      Formulate.claimDate(browser)
      browser.goTo("/aboutyou/moreAboutYou")
      browser.title mustEqual "More About You - About You"
      browser.submit("button[type='submit']")

      browser.find("p[class=error]").size mustEqual 4
    }

    "navigate to next page on valid submission" in new WithBrowser with G1YourDetailsPageContext {
      val claim = ClaimScenarioFactory.s2AboutYouWithTimeOutside
      page goToThePage()
      page runClaimWith (claim, G6EmploymentPage.title)
    }
  } section "integration"
}