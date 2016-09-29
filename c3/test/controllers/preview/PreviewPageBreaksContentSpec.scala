package controllers.preview

import controllers.ClaimScenarioFactory
import org.specs2.mutable._
import utils.WithJsBrowser
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.s_education.GYourCourseDetailsPage
import utils.pageobjects.PageObjects

class PreviewPageBreaksContentSpec extends Specification {
  section("preview")
  "Preview Page" should {
    "display breaks data" in new WithJsBrowser  with PageObjects {
      private val data = ClaimScenarioFactory.s4CareYouProvideWithBreaksInCare(true)
      data.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_2 = "no"

      val benefits = new GClaimDatePage(context).goToThePage().runClaimWith(data,GYourCourseDetailsPage.url)

      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source
      source must contain("any times you or Tom Wilson have been in hospital")
      source must contain("any other times you've not provided care for Tom Wilson")
    }
  }
  section("preview")
}
