package controllers.s_care_you_provide

import org.specs2.mutable._
import utils.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.PageObjects
import utils.pageobjects.s_breaks.GBreaksInCarePage
import utils.pageobjects.s_care_you_provide.GTheirPersonalDetailsPage
import utils.pageobjects.s_education.GYourCourseDetailsPage

class CareYouProvideIntegrationSpec extends Specification {

  "Care you provide" should {
    """navigate to page personal details""" in new WithBrowser with PageObjects {
      val page = GTheirPersonalDetailsPage(context)
      page goToThePage()
    }

    "navigate to education if all details provided and no breaks" in new WithBrowser with PageObjects {
      val claim = ClaimScenarioFactory.s4CareYouProvide(true,true)
      val page = GTheirPersonalDetailsPage(context)
      page goToThePage()
      val breakPage = page runClaimWith(claim,GBreaksInCarePage.url)
      breakPage.fillYesNo("#answer","no")
      breakPage.submitPage().url mustEqual GYourCourseDetailsPage.url
    }

  }
  section("integration", models.domain.CareYouProvide.id)
}
