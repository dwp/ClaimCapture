package controllers.save_for_later

import controllers.ClaimScenarioFactory
import models.view.cache.EncryptedCacheHandling
import models.{DayMonthYear, NationalInsuranceNumber}
import models.domain.{YourDetails, Claim, Claiming}
import models.view.CachedClaim
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.{LightFakeApplication, WithApplication, WithJsBrowser}
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.{Page, PageObjectsContext}
import utils.pageobjects.s_about_you.{GNationalityAndResidencyPage, GContactDetailsPage}
import utils.pageobjects.save_for_later.GSaveForLaterSavePage
import org.specs2.mutable._
import utils.pageobjects.PageObjects

class GSaveForLaterSaveIntegrationSpec extends Specification {
  // Output from C3EncryptionSpec.scala ..... to create a set of xor pairs and decrypt key
  // With key of:88a976e1-e926-4bb4-9322-15aabc6d0516 created xor pair of:0bcd1234-0000-0000-0000-abcd1234cdef and:174650142322392746796619227917559908601
  val encryptkey = "88a976e1-e926-4bb4-9322-15aabc6d0516"
  val uuid = "0bcd1234-0000-0000-0000-abcd1234cdef"
  val decodeint = "174650142322392746796619227917559908601"

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
      savePage.url mustEqual GSaveForLaterSavePage.url
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

      val savePage = nationalityPage.clickLinkOrButton("#save")
      savePage.url mustEqual GSaveForLaterSavePage.url
      savePage.source must contain("Your progress has been saved")
      savePage.source must contain("Continue your application" )
      savePage.source must contain("id=\"continue\"")
      val nationalityPageAgain = savePage.clickLinkOrButton("#continue")
      nationalityPageAgain.url mustEqual GNationalityAndResidencyPage.url
    }

    "contain link to gov page" in new WithJsBrowser with PageObjects {
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
      var claim = new Claim(CachedClaim.key, uuid=uuid)
      val details = new YourDetails("",None, "",None, "green",NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(None, None, None))
      claim=claim+details
      val encryptedCacheHandling = new EncryptedCacheHandling() { val cacheKey = uuid }
      encryptedCacheHandling.saveForLaterInCache(claim, "/lastlocation")

      val request=FakeRequest(GET, "?x="+decodeint)
      val result = GResume.present(request)
      val bodyText: String = contentAsString(result)
      status(result) mustEqual OK
      bodyText must contain( "Enter your details to resume your application")
    }
  }
  section("integration", "SaveForLater")

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