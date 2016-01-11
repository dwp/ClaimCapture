package controllers.save_for_later

import controllers.ClaimScenarioFactory
import models.domain.{Claiming, SaveForLater, Claim, YourDetails}
import models.view.cache.{CacheHandling, EncryptedCacheHandling}
import models.view.{CachedClaim, ClaimHandling}
import models.{DayMonthYear, NationalInsuranceNumber}
import org.specs2.mutable._
import play.api.i18n.Lang
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.save_for_later.GSaveForLaterResumePage
import utils.pageobjects.{Page, PageObjects, PageObjectsContext}
import utils.{LightFakeApplication, WithApplication, SaveForLaterEncryption, WithJsBrowser}
import views.html.helper
import scala.concurrent.duration._

class GSaveForLaterResumeIntegrationSpec extends Specification {
  // Output from C3EncryptionSpec.scala ..... to create a set of xor pairs and decrypt key
  // With key of:88a976e1-e926-4bb4-9322-15aabc6d0516 created xor pair of:0bcd1234-0000-0000-0000-abcd1234cdef and:174650142322392746796619227917559908601
  val encryptkey = "88a976e1-e926-4bb4-9322-15aabc6d0516"
  val uuid = "0bcd1234-0000-0000-0000-abcd1234cdef"
  val decodeint = "174650142322392746796619227917559908601"

  section("integration", "SaveForLater")
  "Save for later resume page" should {
    "be shown after opening resume link with ok claim uuid" in new WithJsBrowser(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true", "saveForLaterShowResumeLink" -> "true", "saveForLaterSaveEnabled" -> "true"))) with PageObjects {
      // Inject the saved claim directly to cache
      var claim = new Claim(CachedClaim.key, uuid=uuid)
      val details = new YourDetails("Mr", "John",None, "Green",NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(1, 1, 1970))
      claim=claim+details
      val encryptedCacheHandling = new EncryptedCacheHandling() { val cacheKey = uuid }
      encryptedCacheHandling.saveForLaterInCache(claim, "/lastlocation")

      browser.goTo("/resume?x="+helper.urlEncode(claim.getEncryptedUuid))
      browser.pageSource must contain( "Enter your details to resume your application")
      browser.pageSource must contain("surname")
      browser.pageSource must contain("firstName")
      browser.pageSource must contain("nationalInsuranceNumber_nino")
      browser.pageSource must contain("dateOfBirth")
    }

    "successfully resume if enter the correct details" in new WithJsBrowser(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true", "saveForLaterShowResumeLink" -> "true", "saveForLaterSaveEnabled" -> "true"))) with PageObjects {
      // Inject the saved claim directly to cache
      var claim = new Claim(CachedClaim.key, uuid=uuid)
      val details = new YourDetails("Mr","John",None, "Green",NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(1, 1, 1970))
      claim=claim+details
      val encryptedCacheHandling = new EncryptedCacheHandling() { val cacheKey = uuid }
      encryptedCacheHandling.saveForLaterInCache(claim, "/about-you/nationality-and-residency")

      val page = GSaveForLaterResumePage(context)
      page goToThePage()
      page fillPageWith SaveForLaterScenarioFactory.ResumePageData()
      val resumed=page.clickLinkOrButton("#resume")
      resumed.getUrl mustEqual("/about-you/nationality-and-residency")
    }

    "restore the app version cookie that the app was saved with" in new WithJsBrowser(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true", "saveForLaterShowResumeLink" -> "true", "saveForLaterSaveEnabled" -> "true"))) with PageObjects {
      // Inject the saved claim directly to cache so we can set the appversion
      var claim = new Claim(CachedClaim.key, uuid=uuid)
      val details = new YourDetails("Mr","John",None, "Green",NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(1, 1, 1970))
      claim=claim+details
      val encryptedCacheHandling = new EncryptedCacheHandling() { val cacheKey = uuid }
      val key=encryptedCacheHandling.createSaveForLaterKey(claim)
      val saveForLater=new SaveForLater(SaveForLaterEncryption.encryptClaim(claim,key), "/about-you/nationality-and-residency", 3, "OK", -1, -1, "V1.00"  )
      encryptedCacheHandling.cache.set("SFL-"+uuid, saveForLater, Duration(CacheHandling.saveForLaterCacheExpiry + CacheHandling.saveForLaterGracePeriod, DAYS))

      val page = GSaveForLaterResumePage(context)
      page goToThePage()
      page fillPageWith SaveForLaterScenarioFactory.ResumePageData()
      val resumed=page.clickLinkOrButton("#resume")
      resumed.getUrl mustEqual("/about-you/nationality-and-residency")

      browser.goTo("/about-you/nationality-and-residency")
      ( browser.getCookie(ClaimHandling.C3VERSION).getValue == "V1.00" ) must beTrue
    }

    "show errors on resume page if no details entered" in new WithJsBrowser(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true", "saveForLaterShowResumeLink" -> "true", "saveForLaterSaveEnabled" -> "true"))) with PageObjects {
      // Inject the saved claim directly to cache
      var claim = new Claim(CachedClaim.key, uuid=uuid)
      val details = new YourDetails("Mr","John",None, "Green",NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(1, 1, 1970))
      claim=claim+details
      val encryptedCacheHandling = new EncryptedCacheHandling() { val cacheKey = uuid }
      encryptedCacheHandling.saveForLaterInCache(claim, "/about-you/nationality-and-residency")

      val page = GSaveForLaterResumePage(context)
      page goToThePage()
      val resumeError=page.clickLinkOrButton("#resume")
      resumeError.source must contain("First name - You must complete this section")
      resumeError.source must contain("Last name - You must complete this section")
      resumeError.source must contain("National Insurance number - You must complete this section")
      resumeError.source must contain("Date of birth - You must complete this section")
    }

    "return sfl claim screen showing retries and final fail when claim tried 3 times" in new WithJsBrowser(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true", "saveForLaterShowResumeLink" -> "true", "saveForLaterSaveEnabled" -> "true"))) with PageObjects  {
      // Inject the saved claim directly to cache
      var claim = new Claim(CachedClaim.key, uuid=uuid)
      val details = new YourDetails("Mr","John",None, "Green",NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(1, 1, 1970))
      claim=claim+details
      val encryptedCacheHandling = new EncryptedCacheHandling() { val cacheKey = uuid }
      encryptedCacheHandling.saveForLaterInCache(claim, "/about-you/nationality-and-residency")

      val page = GSaveForLaterResumePage(context)
      page goToThePage()
      page fillPageWith SaveForLaterScenarioFactory.BadResumePageData()
      val failedResume1=page.clickLinkOrButton("#resume")
      failedResume1.source must contain("Your details don't match this application. You have 2 attempts left")

      val failedResume2=page.clickLinkOrButton("#resume")
      failedResume2.source must contain("Your details don't match this application. You have 1 attempt left")

      val failedResume3=page.clickLinkOrButton("#resume")
      failedResume3.source must contain("Application unavailable")
      failedResume3.source must contain("The application has been deleted for security reasons")
    }

    "return sfl claim screen showing retry count when returning after failed attempt" in new WithJsBrowser(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterResumeEnabled" -> "true", "saveForLaterShowResumeLink" -> "true", "saveForLaterSaveEnabled" -> "true"))) with PageObjects  {
      // Inject the saved claim directly to cache
      var claim = new Claim(CachedClaim.key, uuid=uuid)
      val details = new YourDetails("Mr","John",None, "Green",NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(1, 1, 1970))
      claim=claim+details
      val encryptedCacheHandling = new EncryptedCacheHandling() { val cacheKey = uuid }
      encryptedCacheHandling.saveForLaterInCache(claim, "/about-you/nationality-and-residency")

      val resumepage = GSaveForLaterResumePage(context)
      resumepage goToThePage()
      resumepage fillPageWith SaveForLaterScenarioFactory.BadResumePageData()
      val failedResume1=resumepage.clickLinkOrButton("#resume")
      failedResume1.source must contain("Your details don't match this application. You have 2 attempts left")

      // Come back fresh to resume ... some time in the future
      val resumePageAgain = GSaveForLaterResumePage(context)
      resumePageAgain goToThePage()
      resumePageAgain.source must contain("There's been an unsuccessful attempt to retrieve this application")
      resumePageAgain.source must contain("You have 2 attempts left")
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