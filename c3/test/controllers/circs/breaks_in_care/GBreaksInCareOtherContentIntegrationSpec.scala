package controllers.circs.breaks_in_care

import controllers.WithBrowserHelper
import org.specs2.mutable._
import utils.pageobjects._
import utils.pageobjects.circumstances.breaks_in_care.GCircsBreaksInCareOtherPage
import utils.pageobjects.circumstances.start_of_process.GCircsYourDetailsPage
import utils.{WithJsBrowser}

class GBreaksInCareOtherContentIntegrationSpec extends Specification {
  section("integration", models.domain.Breaks.id)
  "Circs Breaks in care Other page" should {
    "When restarted caring=No then Correctly hide / show Expect to restart caring question" in new WithJsBrowser with PageObjects with WithBrowserHelper {
      GCircsYourDetailsPage.fillYourDetails(context, testData => {})
      val page = GCircsBreaksInCareOtherPage(context)
      page goToThePage()
      page visible ("#caringStartedWrap") must beFalse
      page visible ("#caringNotStartedWrap") must beFalse
      page.clickLinkOrButton("#caringStarted_answer_no")
      page visible ("#caringStartedWrap") must beFalse
      page visible ("#caringNotStartedWrap") must beTrue
    }

    "When Expect to restart caring=Yes then display what What date will you restart care date inputs" in new WithJsBrowser with PageObjects with WithBrowserHelper {
      GCircsYourDetailsPage.fillYourDetails(context, testData => {})
      val page = GCircsBreaksInCareOtherPage(context)
      page goToThePage()
      page.clickLinkOrButton("#caringStarted_answer_no")
      page visible ("#yesdateWrap") must beFalse
      page.clickLinkOrButton("#expectCareAgain_answer_yes")
      page visible ("#yesdateWrap") must beTrue
    }

    "When Expect to restart caring=No then display What date will you restart care date inputs" in new WithJsBrowser with PageObjects with WithBrowserHelper {
      GCircsYourDetailsPage.fillYourDetails(context, testData => {})
      val page = GCircsBreaksInCareOtherPage(context)
      page goToThePage()
      page.clickLinkOrButton("#caringStarted_answer_no")
      page visible ("#nodateWrap") must beFalse
      page.clickLinkOrButton("#expectCareAgain_answer_no")
      page visible ("#nodateWrap") must beTrue
    }

    "When Expect to restart caring=Dont know then display you must tell us warning text" in new WithJsBrowser with PageObjects with WithBrowserHelper {
      GCircsYourDetailsPage.fillYourDetails(context, testData => {})
      val page = GCircsBreaksInCareOtherPage(context)
      page goToThePage()
      page.clickLinkOrButton("#caringStarted_answer_no")
      page visible ("#dontknowWrap") must beFalse
      page.clickLinkOrButton("#expectCareAgain_answer_dontknow")
      page visible ("#dontknowWrap") must beTrue
    }
  }
  section("integration", models.domain.Breaks.id)
}
