package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{ClaimScenarioFactory, Formulate}
import utils.pageobjects.s2_about_you.{G3TimeOutsideUKPage, G4ClaimDatePage, G3TimeOutsideUKPageContext}
import utils.pageobjects.{PageObjectException, ClaimScenario}

class G3TimeOutsideUKIntegrationSpec extends Specification with Tags {
  "Time outside UK" should {
    "accept the minimal mandatory data" in new WithBrowser with G3TimeOutsideUKPageContext {
      val claim = new ClaimScenario
      claim.AboutYouAreYouCurrentlyLivingintheUk = "No"
      page goToThePage()
      page fillPageWith claim
      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[G4ClaimDatePage]
    }

    "have a valid date when currently living in UK" in new WithBrowser with G3TimeOutsideUKPageContext {
      val claim = new ClaimScenario
      claim.AboutYouAreYouCurrentlyLivingintheUk = "Yes"
      page goToThePage()
      page fillPageWith claim
      page.submitPage().listErrors.isEmpty must beFalse
    }

    "accept a valid date when currently living in UK" in new WithBrowser with G3TimeOutsideUKPageContext {
      val claim = new ClaimScenario
      claim.AboutYouAreYouCurrentlyLivingintheUk = "Yes"
      claim.AboutYouWhenDidYouArriveInYheUK = "01/11/2003"
      claim.AboutYouDoYouPlantoGoBacktoThatCountry = "No"
      page goToThePage()
      page fillPageWith claim
      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[G4ClaimDatePage]
    }

  } section "integration"
}