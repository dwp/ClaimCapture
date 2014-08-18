package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s2_about_you.{G8MoreAboutYouPage, G7OtherEEAStateOrSwitzerlandPage}
import utils.pageobjects.PageObjects
import utils.pageobjects.s3_your_partner.G1YourPartnerPersonalDetailsPage

class G7OtherEEAStateOrSwitzerlandIntegrationSpec extends Specification with Tags {
  sequential

  val urlUnderTest = "/about-you/other-eea-state-or-switzerland"
  val submitButton = "button[type='submit']"
  val errorDiv = "div[class=validation-summary] ol li"

  "Other European Economic Area (EEA) states or Switzerland" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G7OtherEEAStateOrSwitzerlandPage(context)
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithBrowser with PageObjects{
			val page =  G7OtherEEAStateOrSwitzerlandPage(context)
      page goToThePage()
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G7OtherEEAStateOrSwitzerlandPage]
    }

    "navigate to next page on valid resident submission" in new WithBrowser with PageObjects{
			val page =  G7OtherEEAStateOrSwitzerlandPage(context)
      val claim = ClaimScenarioFactory.otherEuropeanEconomicArea
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G1YourPartnerPersonalDetailsPage]
    }

    "navigate to next page on valid non resident submission" in new WithBrowser with PageObjects{
			val page =  G7OtherEEAStateOrSwitzerlandPage(context)
      val claim = ClaimScenarioFactory.otherEuropeanEconomicArea
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G1YourPartnerPersonalDetailsPage]
    }
  } section("integration", models.domain.AboutYou.id)
}
