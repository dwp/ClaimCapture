package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s2_about_you.{G8MoreAboutYouPage, G7OtherEEAStateOrSwitzerlandPage, G7OtherEEAStateOrSwitzerlandPageContext}

class G7OtherEEAStateOrSwitzerlandIntegrationSpec extends Specification with Tags {
  sequential

  val urlUnderTest = "/about-you/other-eea-state-or-switzerland"
  val submitButton = "button[type='submit']"
  val errorDiv = "div[class=validation-summary] ol li"

  "Money you get from other European Economic Area (EEA) state or Switzerland" should {
    "be presented" in new WithBrowser with G7OtherEEAStateOrSwitzerlandPageContext {
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithBrowser with G7OtherEEAStateOrSwitzerlandPageContext {
      page goToThePage()
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G7OtherEEAStateOrSwitzerlandPage]
    }

    "navigate to next page on valid resident submission" in new WithBrowser with G7OtherEEAStateOrSwitzerlandPageContext {
      val claim = ClaimScenarioFactory.otherEuropeanEconomicArea
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G8MoreAboutYouPage]
    }

    "navigate to next page on valid non resident submission" in new WithBrowser with G7OtherEEAStateOrSwitzerlandPageContext {
      val claim = ClaimScenarioFactory.otherEuropeanEconomicArea
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G8MoreAboutYouPage]
    }
  } section("integration", models.domain.AboutYou.id)
}
