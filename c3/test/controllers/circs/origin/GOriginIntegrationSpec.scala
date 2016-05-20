package controllers.circs.origin

import org.specs2.mutable._
import utils.pageobjects.circumstances.start_of_process.GReportChangesPage
import utils.{LightFakeApplication, WithBrowser}
import utils.pageobjects.circumstances.origin.GOriginPage
import utils.pageobjects.PageObjects

class GOriginIntegrationSpec extends Specification {
  section("integration", models.domain.CircumstancesReportChanges.id)
  "Origin page on GB site" should {
    "present GB Circs page" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("origin.tag" -> "GB"))) with PageObjects {
      browser.goTo(GOriginPage.url)
      browser.url() mustEqual GReportChangesPage.url
    }
  }

  "Origin page on NISSA site" should {
    "display select country page with 3 country options" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("origin.tag" -> "GB-NIR"))) with PageObjects {
      browser.goTo(GOriginPage.url)
      browser.url() mustEqual GOriginPage.url
      browser.pageSource must contain("Which country do you live in?")
      browser.find("#origin_GB").getAttribute("value") mustEqual ("GB")
      browser.find("#origin_NI").getAttribute("value") mustEqual ("NI")
      browser.find("#origin_OTHER").getAttribute("value") mustEqual ("OTHER")
    }

    "force selection of country" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("origin.tag" -> "GB-NIR"))) with PageObjects {
      browser.goTo(GOriginPage.url)
      browser.pageSource must contain("Which country do you live in?")
      browser.find("#submit").click()

      println(browser.pageSource)
      browser.url() mustEqual GOriginPage.url
      browser.pageSource must contain("Which country do you live in")
      browser.pageSource must contain("You must complete this section")
    }

    "redirect to Circs start page if NI selected" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("origin.tag" -> "GB-NIR"))) with PageObjects {
      browser.goTo(GOriginPage.url)
      browser.pageSource must contain("Which country do you live in?")
      browser.find("#origin_NI").click()
      browser.find("#submit").click()
      browser.url() mustEqual GReportChangesPage.url
    }

    "redirect to Origin error page if GB selected" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("origin.tag" -> "GB-NIR"))) with PageObjects {
      browser.goTo(GOriginPage.url)
      browser.pageSource must contain("Which country do you live in?")
      browser.find("#origin_GB").click()
      browser.find("#submit").click()
      browser.url() mustEqual GOriginPage.url
      browser.pageSource must contain ("you must use the service for England")
    }

    "redirect to Origin error page if OTHER selected" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("origin.tag" -> "GB-NIR"))) with PageObjects {
      browser.goTo(GOriginPage.url)
      browser.pageSource must contain("Which country do you live in?")
      browser.find("#origin_OTHER").click()
      browser.find("#submit").click()
      browser.url() mustEqual GOriginPage.url
      browser.pageSource must contain ("you must use the service for England")
    }

    "contains english link in footer when welsh lang selected" in new WithBrowser with PageObjects {
      browser.goTo(GOriginPage.url+"?lang=cy")
      (browser.getCookie("PLAY_LANG").getValue == "cy") must beTrue
      browser.pageSource() must contain("English")
    }

    "contains welsh link in footer when english lang is default" in new WithBrowser with PageObjects {
      browser.goTo(GOriginPage.url)
      (browser.getCookie("PLAY_LANG").getValue == "") must beTrue
      browser.pageSource() must contain("Cymraeg")
    }

    "contains no welsh or english link in footer when GB-NIR is default" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("origin.tag" -> "GB-NIR"))) with PageObjects {
      browser.goTo(GOriginPage.url)
      (browser.getCookie("PLAY_LANG").getValue == "") must beTrue
      browser.pageSource() must not contain("Cymraeg")
      browser.pageSource() must not contain("English")
    }

    "contains no welsh or english link in footer when GB-NIR when welsh selected" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("origin.tag" -> "GB-NIR"))) with PageObjects {
      browser.goTo(GOriginPage.url+"?lang=cy")
      (browser.getCookie("PLAY_LANG").getValue == "") must beTrue
      browser.pageSource() must not contain("Cymraeg")
      browser.pageSource() must not contain("English")
    }
  }
  section("integration", models.domain.CircumstancesReportChanges.id)
}
