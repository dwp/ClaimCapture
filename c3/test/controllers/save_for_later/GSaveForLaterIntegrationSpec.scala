package controllers.save_for_later

import controllers.ClaimScenarioFactory
import models.view.cache.EncryptedCacheHandling
import models.{DayMonthYear, NationalInsuranceNumber}
import models.domain.{YourDetails, Claim, Claiming}
import models.view.{CachedClaim}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.{LightFakeApplication, WithApplication, WithJsBrowser}
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.{Page, PageObjectsContext}
import utils.pageobjects.s_about_you.{GContactDetailsPage}
import utils.pageobjects.save_for_later.GSaveForLaterSavePage
import org.specs2.mutable._
import utils.pageobjects.PageObjects


class GSaveForLaterIntegrationSpec extends Specification {

  "Save for later page" should {
    "be shown after clicking Save in nationality" in new WithJsBrowser with PageObjects {
      pending("Colin looking to fix")
      loadClaimData(context)

      val page = GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith SaveForLaterScenarioFactory.WithEmailSet()

      val nationalityPage = page.clickLinkOrButton("#next")
      nationalityPage.source must contain("Save for later")
      nationalityPage.source must contain("id=\"save\"")

      val savePage=nationalityPage.clickLinkOrButton("#save")
      savePage.url mustEqual GSaveForLaterSavePage.url
      savePage.source must contain("Your progress has been saved")
      savePage.source must contain("Continue your application" )
      savePage.source must contain("id=\"continue\"")
    }

    "return back to original screen after clicking continue your application" in new WithJsBrowser with PageObjects {
      pending("Colin looking to fix")
      loadClaimData(context)

      val page = GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith SaveForLaterScenarioFactory.WithEmailSet()

      val nationalityPage = page.clickLinkOrButton("#next")
      nationalityPage.source must contain("Save for later")
      nationalityPage.source must contain("id=\"save\"")

      val savePage=nationalityPage.clickLinkOrButton("#save")
      savePage.url mustEqual GSaveForLaterSavePage.url
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


    "contain link to gov page" in new WithJsBrowser with PageObjects {
      pending("Colin looking to fix")
      loadClaimData(context)

      val page = GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith SaveForLaterScenarioFactory.WithEmailSet()

      val nationalityPage = page.clickLinkOrButton("#next")
      nationalityPage.source must contain("Save for later")
      nationalityPage.source must contain("id=\"save\"")

      val savePage=nationalityPage.clickLinkOrButton("#save")
      savePage.url mustEqual GSaveForLaterSavePage.url
      savePage.source must contain("Your progress has been saved")
      savePage.source must contain("Go to GOV.UK" )
      savePage.source must contain("id=\"govuk\"" )
    }

    "return ok resume screen when claim is found in sfl cache" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true"))) with Claiming {
      var claim = new Claim(CachedClaim.key, uuid="123456")
      val details = new YourDetails("",None, "",None, "green",NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(None, None, None))
      claim=claim+details
      val encryptedCacheHandling = new EncryptedCacheHandling() { val cacheKey = "123456" }
      encryptedCacheHandling.saveForLaterInCache(claim, "/lastlocation")

      val request=FakeRequest(GET, "?savekey=123456")
      val result = GResume.present(request)
      val bodyText: String = contentAsString(result)
      status(result) mustEqual OK
      bodyText must contain( "Enter your details to resume your application")
    }


    "contain cookie for saved application version when resumed" in new WithJsBrowser  with PageObjects{
      loadClaimData(context)
/*
      val page = GContactDetailsPage(context)
      page goToThePage()
      page fillPageWith SaveForLaterScenarioFactory.WithEmailSet()

      val nationalityPage = page.clickLinkOrButton("#next")
      nationalityPage.source must contain("Save for later")
      nationalityPage.source must contain("id=\"save\"")

      val savePage=nationalityPage.clickLinkOrButton("#save")
      savePage.url mustEqual GSaveForLaterSavePage.url
      savePage.source must contain("Your progress has been saved")
      savePage.source must contain("Continue your application" )
      savePage.source must contain("id=\"continue\"")

      val nationalityPageAgain=savePage.clickLinkOrButton("#continue")

      browser.goTo("/resume")
      println("browser source:"+browser.pageSource())
      println("app version is:"+browser.getCookie(ClaimHandling.C3VERSION).getValue)
      ( browser.getCookie(ClaimHandling.C3VERSION).getValue == ClaimHandling.C3VERSION_VALUE ) must beTrue
      */
    }
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