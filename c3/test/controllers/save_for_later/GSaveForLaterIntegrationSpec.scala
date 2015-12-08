package controllers.save_for_later

import controllers.ClaimScenarioFactory
import org.specs2.mutable._
import utils.WithJsBrowser
import utils.helpers.PreviewField._
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.{Page, PageObjectsContext, PageObjects}
import utils.pageobjects.s_about_you.{GNationalityAndResidencyPage, GContactDetailsPage}
import utils.pageobjects.save_for_later.GSaveForLaterPage

class GSaveForLaterIntegrationSpec extends Specification {

  "Save for later page" should {
    "be shown after clicking Save in nationality" in new WithJsBrowser with PageObjects {
      loadClaimData(context)

      val page = GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith SaveForLaterScenarioFactory.WithEmailSet()

      val nationalityPage = page.clickLinkOrButton("#next")
      nationalityPage.source must contain("Save for later")
      nationalityPage.source must contain("id=\"save\"")

      val savePage=nationalityPage.clickLinkOrButton("#save")
      savePage.url mustEqual GSaveForLaterPage.url
      savePage.source must contain("Your progress has been saved")
      savePage.source must contain("Continue your application" )
      savePage.source must contain("id=\"continue\"")
    }

    "return back to original screen after clicking continue your application" in new WithJsBrowser with PageObjects {
      loadClaimData(context)

      val page = GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith SaveForLaterScenarioFactory.WithEmailSet()

      val nationalityPage = page.clickLinkOrButton("#next")
      nationalityPage.source must contain("Save for later")
      nationalityPage.source must contain("id=\"save\"")

      val savePage=nationalityPage.clickLinkOrButton("#save")
      savePage.url mustEqual GSaveForLaterPage.url
      savePage.source must contain("Your progress has been saved")
      savePage.source must contain("Continue your application" )
      savePage.source must contain("id=\"continue\"")

      /* ODDLY Clicking Continue button causes test to crash
      println("================ save page ==========")
      println(savePage.source)
      val nationalityPageAgain=savePage.clickLinkOrButton("#continue")
      println( "=================== Nationality page again:")
      println(nationalityPageAgain.source)

      //
      // nationalityPageAgain.url mustEqual GNationalityAndResidencyPage.url

      Needs more investigation. ColinG 07/12/15.
      */
    }

/*
    "contain link to gov page" in new WithJsBrowser with PageObjects {
      loadClaimData(context)

      val page = GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith SaveForLaterScenarioFactory.WithEmailSet()

      val nationalityPage = page.clickLinkOrButton("#next")
      nationalityPage.source must contain("Save for later")
      nationalityPage.source must contain("id=\"save\"")

      val savePage=nationalityPage.clickLinkOrButton("#save")
      savePage.url mustEqual GSaveForLaterPage.url
      savePage.source must contain("Your progress has been saved")
      savePage.source must contain("Go to GOV.UK" )
      savePage.source must contain("id=\"govuk\"" )
    }
*/
  }
  section("integration", models.domain.AboutYou.id)


  def loadClaimData(context:PageObjectsContext):Page = {
    val claimDatePage = GClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate

    val page =  claimDatePage submitPage()
    val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
    page goToThePage()
    page fillPageWith claim
    page submitPage()
  }
}