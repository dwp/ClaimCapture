package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s2_about_you._
import utils.pageobjects.{TestData, ClaimPageFactory, PageObjects}

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

      nextPage must beAnInstanceOf[G6TripPage]
    }

    """present "completed" when no more 52 week trips are required""" in new WithBrowser with PageObjects{
			val page =  G5AbroadForMoreThan52WeeksPage(context)
      val claim = ClaimScenarioFactory abroadForMoreThan52WeeksConfirmationNo()
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

    "Modify time outside from preview page" in new WithBrowser with PageObjects{

      val page =  G5AbroadForMoreThan52WeeksPage(context)
      val claim = ClaimScenarioFactory.abroadForMoreThan52WeeksConfirmationNo()
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      val id = "about_you_abroad"
      val previewPage = PreviewPage(context)
      previewPage goToThePage()
      previewPage.xpath(s"//dt[./a[@id='$id']]/following-sibling::dd").getText mustEqual "No"
      val contactDetails = ClaimPageFactory.buildPageFromFluent(previewPage.click(s"#$id"))

      contactDetails must beAnInstanceOf[G5AbroadForMoreThan52WeeksPage]
      val modifiedData = new TestData
      modifiedData.AboutYouMoreTripsOutOfGBforMoreThan52WeeksAtATime_1 = "Yes"

      contactDetails fillPageWith modifiedData
      val trips = contactDetails submitPage()

      trips must beAnInstanceOf[G6TripPage]
      val tripsData = ClaimScenarioFactory.abroadForMoreThan52WeeksTrip1()

      trips.fillPageWith(tripsData)
      val modifiedAbroad = trips submitPage()

      val newData = new TestData
      newData.AboutYouMoreTripsOutOfGBforMoreThan52WeeksAtATime_2 = "No"
      modifiedAbroad.fillPageWith(newData)
      val previewModifiedPage = modifiedAbroad submitPage()

      previewModifiedPage.xpath(s"//dt[./a[@id='$id']]/following-sibling::dd").getText mustEqual "Yes"

    }


  } section("integration", models.domain.AboutYou.id)
}
