package controllers.preview

import controllers.ClaimScenarioFactory
import org.specs2.mutable._
import utils.WithJsBrowser
import utils.helpers.PreviewField._
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_about_you.GContactDetailsPage
import utils.pageobjects.s_care_you_provide.GTheirPersonalDetailsPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.s_education.GYourCourseDetailsPage
import utils.pageobjects.s_eligibility.GBenefitsPage
import utils.pageobjects.s_your_partner.GYourPartnerPersonalDetailsPage
import utils.pageobjects.{PageObjects, PageObjectsContext, TestData}

class PreviewPageBreaksContentSpec extends Specification {

  "Preview Page" should {
    "display breaks data" in new WithJsBrowser  with PageObjects{

      private val data = ClaimScenarioFactory.s4CareYouProvideWithBreaksInCare(true)
      data.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_2 = "no"

      val benefits = new GClaimDatePage(context).goToThePage().runClaimWith(data,GYourCourseDetailsPage.url)

      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source
      
      source must contain("Yes - Details provided for 1 break(s)")
    }
    
  }
section("preview")


}
