package controllers.s2_about_you

import play.api.test.WithBrowser
import org.specs2.mutable.{Tags, Specification}
import controllers.{BrowserMatchers, ClaimScenarioFactory, Formulate}
import utils.pageobjects.s2_about_you.{G9EmploymentPage, G8MoreAboutYouPage, G1YourDetailsPageContext, G8MoreAboutYouPageContext}
import utils.pageobjects.s1_carers_allowance.G1BenefitsPage

class G8MoreAboutYouIntegrationSpec extends Specification with Tags {
  "More About You" should {
    "present Benefits when there is no claim date" in new WithBrowser with G8MoreAboutYouPageContext {
      val landingPage = page goToThePage(throwException = false)
      landingPage must beAnInstanceOf[G1BenefitsPage]
    }

    "be presented when there is a claim date" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      titleMustEqual("Your nationality and residency - About you - the carer")
    }

    "contain the completed forms" in new WithBrowser with G1YourDetailsPageContext {
      val claim = ClaimScenarioFactory.s2AboutYouWithTimeOutside
      page goToThePage()

      page runClaimWith (claim, G8MoreAboutYouPage.title)
      page numberSectionsCompleted() mustEqual 5
    }

    "contain questions with claim dates" in new WithBrowser {
      val dateString = "03/04/1950"
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      val h3 = browser.find("div[class=completed] ul li h3")
      h3.getText.contains(dateString) mustEqual true

      val questionLabels = browser.find("fieldset[class=form-elements] ul > li > p")
      questionLabels.get(0).getText must contain(dateString)
    }

    "contain errors on invalid submission" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      browser.goTo("/about-you/more-about-you")
      titleMustEqual("More about you - About you - the carer")
      browser.submit("button[type='submit']")

      browser.find("p[class=error]").size mustEqual 3
    }

    "navigate to next page on valid submission" in new WithBrowser with G1YourDetailsPageContext {
      println("1b")
      val claim = ClaimScenarioFactory.s2AboutYouWithTimeOutside
      println("2b")
      page goToThePage()
      println("3b")
      page runClaimWith (claim, G9EmploymentPage.title)
    }
  } section("integration", models.domain.AboutYou.id)
}