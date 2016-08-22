package controllers.s_care_you_provide

import org.specs2.mutable._
import utils.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects._
import utils.pageobjects.breaks_in_care.GBreaksInCareSummaryPage
import utils.pageobjects.s_care_you_provide.{GTheirPersonalDetailsPage, GMoreAboutTheCarePage}

class GMoreAboutTheCareIntegrationSpec extends Specification {
  sequential

  section("integration", models.domain.CareYouProvide.id)
  "Representatives For The Person" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = GMoreAboutTheCarePage(context)
      page goToThePage()
    }

    "contain dp name in 35 hour and uc questions" in new WithBrowser with PageObjects {
      val claim = ClaimScenarioFactory.s4CareYouProvide(hours35 = false)
      val theirPersonalDetailsPage = GTheirPersonalDetailsPage(context)
      theirPersonalDetailsPage goToThePage()
      theirPersonalDetailsPage fillPageWith claim
      val moreAboutCarePage = theirPersonalDetailsPage submitPage()
      moreAboutCarePage must beAnInstanceOf[GMoreAboutTheCarePage]
      moreAboutCarePage.source must contain("Do you spend 35 hours or more each week providing care for Tom Wilson?")
      moreAboutCarePage.source must contain("Does anyone else spend 35 hours or more each week providing care for Tom Wilson?")
      moreAboutCarePage.source must contain("Are they getting the carer element of Universal Credit for Tom Wilson?")
    }

    "contain 2 errors on invalid submission" in new WithBrowser with PageObjects {
      val page = GMoreAboutTheCarePage(context)
      page goToThePage()
      page submitPage()
      page.listErrors.size mustEqual 2
    }

    "navigate back" in new WithBrowser with PageObjects {
      val theirPersonalDetailsPage = GTheirPersonalDetailsPage(context)
      theirPersonalDetailsPage goToThePage()
      theirPersonalDetailsPage fillPageWith ClaimScenarioFactory.s4CareYouProvide(hours35 = true)
      val moreAboutTheCarePage = theirPersonalDetailsPage submitPage()
      moreAboutTheCarePage goBack() must beAnInstanceOf[GTheirPersonalDetailsPage]
    }

    "navigate forward to Breaks and back again after enter all details including otherCarerUcDetails information" in new WithBrowser with PageObjects {
      val moreAboutPage = GMoreAboutTheCarePage(context)
      val claim = ClaimScenarioFactory.s4CareYouProvideWithOtherCarerUcDetails
      moreAboutPage goToThePage()
      moreAboutPage fillPageWith claim

      val summaryPage = moreAboutPage submitPage()
      summaryPage must beAnInstanceOf[GBreaksInCareSummaryPage]

      val backPage = summaryPage.goBack()
      browser.find("#otherCarer_yes").getAttribute("selected") mustEqual "true"
      browser.find("#otherCarerUc_yes").getAttribute("selected") mustEqual "true"
      browser.find("#otherCarerUcDetails").getValue() must contain("My sister gets uc here ninum is NR121212A")
    }
  }
  section("integration", models.domain.CareYouProvide.id)
}
