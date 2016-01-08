package controllers.third_party

import controllers.ClaimScenarioFactory
import org.specs2.mutable._
import utils.WithJsBrowser
import utils.pageobjects._
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.third_party.GThirdPartyPage

class GThirdPartyIntegrationSpec extends Specification {
  sequential

  val urlUnderTest = "/third-party/third-party"
  val submitButton = "button[type='submit']"
  val errorDiv = "div[class=validation-summary] ol li"

  section("integration", models.domain.ThirdParty.id)
  "Third Party" should {
    "be presented" in new WithJsBrowser with PageObjects{
			val page =  GThirdPartyPage(context)
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithJsBrowser with PageObjects {
			val page =  GThirdPartyPage(context)
      page goToThePage()
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GThirdPartyPage]
    }

    "navigate to next page on third party submission" in new WithJsBrowser with PageObjects {
			val page =  GThirdPartyPage(context)
      val claim = ClaimScenarioFactory.thirdPartyYesCarer
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GClaimDatePage]
    }

    "navigate to next page on valid not carer submission" in new WithJsBrowser with PageObjects {
			val page =  GThirdPartyPage(context)
      val claim = ClaimScenarioFactory.thirdPartyNotCarer
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GClaimDatePage]
    }

    "contain errors on invalid not carer submission" in new WithJsBrowser with PageObjects {
			val page =  GThirdPartyPage(context)
      val claim = ClaimScenarioFactory.thirdPartyNotCarer
      claim.ThirdPartyNameAndOrganisation = ""
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GThirdPartyPage]
    }

    "name and organisation visible when clicked back" in new WithJsBrowser with PageObjects {
      val page =  GThirdPartyPage(context)
      val claim = ClaimScenarioFactory.thirdPartyNotCarer
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()
      val thirdPartyPage = nextPage.goBack()
      browser.find("#thirdParty_nameAndOrganisation").getValue mustEqual "test and company"
    }
  }
  section("integration", models.domain.ThirdParty.id)
}
