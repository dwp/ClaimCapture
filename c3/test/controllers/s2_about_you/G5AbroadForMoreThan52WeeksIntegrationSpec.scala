package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s2_about_you._
import utils.pageobjects.PageObjects

class G5AbroadForMoreThan52WeeksIntegrationSpec extends Specification with Tags {
  "Abroad for more that 52 weeks" should {
    "present" in new WithBrowser with PageObjects{
			val page =  G5AbroadForMoreThan52WeeksPage(context)
      page goToThePage()
    }

    "provide for trip entry" in new WithBrowser with PageObjects{
			val page =  G5AbroadForMoreThan52WeeksPage(context)
      val claim = ClaimScenarioFactory abroadForMoreThan52WeeksConfirmationYes()
      page goToThePage()
      page fillPageWith claim
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G7OtherEEAStateOrSwitzerlandPage]
    }

    """go back to "Nationality and Residency".""" in new WithBrowser with PageObjects{
			val page =  G4NationalityAndResidencyPage(context)
      val claim = ClaimScenarioFactory yourNationalityAndResidencyResident()
      page goToThePage()

      page fillPageWith claim
      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[G5AbroadForMoreThan52WeeksPage]

      val backPage = nextPage goBack()
      backPage must beAnInstanceOf[G4NationalityAndResidencyPage]
    }

    """remember "no more 52 weeks trips" upon stating "52 weeks trips" and returning""" in new WithBrowser with PageObjects{
			val page =  G5AbroadForMoreThan52WeeksPage(context)
      val claim = ClaimScenarioFactory abroadForMoreThan52WeeksConfirmationNo()
      page goToThePage()

      page fillPageWith claim
      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[G7OtherEEAStateOrSwitzerlandPage]

      val backPage = nextPage goBack()
      backPage must beAnInstanceOf[G5AbroadForMoreThan52WeeksPage]

      backPage.ctx.browser.findFirst("#anyTrips_yes").isSelected should beFalse
      backPage.ctx.browser.findFirst("#anyTrips_no").isSelected should beTrue
    }

    "Trip details must not be visible when time abroad page is displayed" in new WithBrowser with PageObjects{
      val page =  G5AbroadForMoreThan52WeeksPage(context)
      page goToThePage()
      page.ctx.browser.findFirst("#anyTrips_yes").isSelected should beFalse
      page.ctx.browser.findFirst("#anyTrips_no").isSelected should beFalse
      page.ctx.browser.findFirst("#tripDetails").isDisplayed should beFalse
    }

    "Trip details must be visible when returning back to the time abroad page" in new WithBrowser with PageObjects{
      val page =  G5AbroadForMoreThan52WeeksPage(context)
      val claim = ClaimScenarioFactory abroadForMoreThan52WeeksConfirmationYes()
      page goToThePage()

      page fillPageWith claim
      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[G7OtherEEAStateOrSwitzerlandPage]

      val backPage = nextPage goBack()
      backPage must beAnInstanceOf[G5AbroadForMoreThan52WeeksPage]

      backPage.ctx.browser.findFirst("#anyTrips_yes").isSelected should beTrue
      backPage.ctx.browser.findFirst("#anyTrips_no").isSelected should beFalse
      backPage.ctx.browser.findFirst("#tripDetails").isDisplayed should beTrue
    }

  } section("integration", models.domain.AboutYou.id)
}
