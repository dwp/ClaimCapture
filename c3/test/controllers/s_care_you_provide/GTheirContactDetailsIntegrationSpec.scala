package controllers.s_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import controllers.{PreviewTestUtils, ClaimScenarioFactory, BrowserMatchers, Formulate}
import utils.pageobjects._
import utils.pageobjects.s_breaks.GBreaksInCarePage
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.s_care_you_provide._
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_about_you.GContactDetailsPage
import utils.helpers.PreviewField._

class GTheirContactDetailsIntegrationSpec extends Specification with Tags {

  "Their Contact Details" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = GTheirContactDetailsPage(context)
      page goToThePage()
      page.url mustEqual GTheirContactDetailsPage.url
    }

    "contain errors on empty submission" in new WithBrowser with PageObjects {
      val page = GTheirContactDetailsPage(context)
      page goToThePage()
      page submitPage()

      page.listErrors.size mustEqual 1
    }

    "be jumped if they live at same address" in new WithBrowser with PageObjects {
      val thierContactDetailsPage = goToTheirContactDetailsPage(context, ClaimScenarioFactory.s4CareYouProvide(hours35 = true,liveSameAddress = true))

      thierContactDetailsPage must beAnInstanceOf[GMoreAboutTheCarePage]
    }

    "be blank if they live at different address" in new WithBrowser with PageObjects {

      val theirPersonalData = ClaimScenarioFactory.s4CareYouProvide(hours35 = true)
      theirPersonalData.AboutTheCareYouProvideDoTheyLiveAtTheSameAddressAsYou = "No"

      val thierContactDetailsPage = goToTheirContactDetailsPage(context, theirPersonalData)
      thierContactDetailsPage must beAnInstanceOf[GTheirContactDetailsPage]

      val addressLineOne = thierContactDetailsPage readInput("#address_lineOne")
      val postCode = thierContactDetailsPage readInput("#postcode")
      addressLineOne.get mustEqual ""
      postCode.get mustEqual ""
    }

    "navigate back to Their Personal Details" in new WithBrowser with PageObjects {
      val thierContactDetailsPage = goToTheirContactDetailsPage(context, ClaimScenarioFactory.s4CareYouProvide(hours35 = true,liveSameAddress = true))
      thierContactDetailsPage must beAnInstanceOf[GMoreAboutTheCarePage]
      thierContactDetailsPage goBack() must beAnInstanceOf[GTheirPersonalDetailsPage]
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects {
      val thierContactDetailsPage = goToTheirContactDetailsPage(context, ClaimScenarioFactory.s4CareYouProvide(hours35 = true,liveSameAddress = true))
      thierContactDetailsPage fillPageWith ClaimScenarioFactory.s4CareYouProvide(hours35 = true)
      thierContactDetailsPage submitPage() must beAnInstanceOf[GBreaksInCarePage]
    }

  } section("integration", models.domain.CareYouProvide.id)

  def goToTheirContactDetailsPage (context:PageObjectsContext, testData:TestData) = {
    val yourContactDetailsPage = GContactDetailsPage(context)
    yourContactDetailsPage goToThePage()
    yourContactDetailsPage fillPageWith ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
    yourContactDetailsPage submitPage()

    val theirPersonalDetailsPage = GTheirPersonalDetailsPage(context)
    theirPersonalDetailsPage goToThePage()
    theirPersonalDetailsPage fillPageWith testData
    theirPersonalDetailsPage submitPage()
  }

}