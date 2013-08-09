package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s2_about_you.{G4ClaimDatePage, G3TimeOutsideUKPageContext}
import utils.pageobjects.ClaimScenario

class G3TimeOutsideUKIntegrationSpec extends Specification with Tags {
  "About Your Time Outside The UK" should {
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
  } section("integration", models.domain.AboutYou.id)
}