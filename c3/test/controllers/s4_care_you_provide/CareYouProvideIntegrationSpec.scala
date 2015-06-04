package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.PageObjects
import utils.pageobjects.s4_care_you_provide.{G10BreaksInCarePage, G1TheirPersonalDetailsPage}
import utils.pageobjects.s6_education.G1YourCourseDetailsPage

class CareYouProvideIntegrationSpec extends Specification with Tags {

  "Care you provide" should {
    """navigate to page personal details""" in new WithBrowser with PageObjects {
      val page = G1TheirPersonalDetailsPage(context)
      page goToThePage()
    }

    "navigate to education if all details provided and no breaks" in new WithBrowser with PageObjects {
      val claim = ClaimScenarioFactory.s4CareYouProvide(true,true)
      val page = G1TheirPersonalDetailsPage(context)
      page goToThePage()
      val breakPage = page runClaimWith(claim,G10BreaksInCarePage.url)
      breakPage.fillYesNo("#answer","no")
      breakPage.submitPage().url mustEqual G1YourCourseDetailsPage.url
    }

  } section("integration", models.domain.CareYouProvide.id)
}