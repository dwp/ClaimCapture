package controllers.save_for_later

import controllers.ClaimScenarioFactory
import models.domain.{Claim, Claiming, YourDetails}
import models.view.cache.EncryptedCacheHandling
import models.view.{CachedClaim, ClaimHandling}
import models.{DayMonthYear, NationalInsuranceNumber}
import org.specs2.mutable._
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.save_for_later.{GSaveForLaterResumePage}
import utils.pageobjects.{Page, PageObjects, PageObjectsContext}
import utils.{WithJsBrowser}

class GSaveForLaterResumeIntegrationSpec extends Specification {

  "Save for later resume page" should {
    "be shown after opening resume link with ok claim uuid" in new WithJsBrowser with PageObjects {
      // Inject the saved claim directly to cache
      var claim = new Claim(CachedClaim.key, uuid="123456")
      val details = new YourDetails("",None, "John",None, "Green",NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(1, 1, 1970))
      claim=claim+details
      val encryptedCacheHandling = new EncryptedCacheHandling() { val cacheKey = "123456" }
      encryptedCacheHandling.saveForLaterInCache(claim, "/lastlocation")

      browser.goTo("/resume?savekey=123456")
      browser.pageSource must contain( "Enter your details to resume your application")
      browser.pageSource must contain("surname")
      browser.pageSource must contain("firstName")
      browser.pageSource must contain("nationalInsuranceNumber_nino")
      browser.pageSource must contain("dateOfBirth")
      println("browser source:"+browser.pageSource())
    }

    "successfully resume if enter the correct details" in new WithJsBrowser with PageObjects {
      // Inject the saved claim directly to cache
      var claim = new Claim(CachedClaim.key, uuid="123456")
      val details = new YourDetails("",None, "John",None, "Green",NationalInsuranceNumber(Some("AB123456D")), DayMonthYear(1, 1, 1970))
      claim=claim+details
      val encryptedCacheHandling = new EncryptedCacheHandling() { val cacheKey = "123456" }
      encryptedCacheHandling.saveForLaterInCache(claim, "/about-you/nationality-and-residency")

      val page = GSaveForLaterResumePage(context)
      page goToThePage()
      page fillPageWith SaveForLaterScenarioFactory.ResumePageData()
      val resumed=page.clickLinkOrButton("#resume")
      resumed.getUrl mustEqual("/about-you/nationality-and-residency")
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