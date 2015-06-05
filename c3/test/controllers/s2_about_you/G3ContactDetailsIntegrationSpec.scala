package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s2_about_you._
import utils.pageobjects.{TestData, ClaimPageFactory, PageObjects}
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage
import org.openqa.selenium.By

class G3ContactDetailsIntegrationSpec extends Specification with Tags {
  "Contact Details" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G3ContactDetailsPage(context)
      page goToThePage()
    }

    "contain error if address not filled in" in new WithBrowser with PageObjects{
      val page =  G3ContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      claim.AboutYouAddress = ""
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Address")

    }

    "contain error if 'Contact phone or mobile number' not filled in" in new WithBrowser with PageObjects{
      val page =  G3ContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      claim.HowWeContactYou = ""
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Contact phone or mobile number - This field is required")
    }

    "valid submission if 'Contact phone or mobile number' is filled in with number" in new WithBrowser with PageObjects{
      val page =  G3ContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G4NationalityAndResidencyPage]
    }

    "valid submission if 'Contact phone or mobile number' is filled in with text" in new WithBrowser with PageObjects{
      val page =  G3ContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      claim.HowWeContactYou = "I do not have contact number"
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G4NationalityAndResidencyPage]
    }

    
    "navigate to next page on valid submission" in new WithBrowser with PageObjects{
			val page =  G3ContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()
      
      nextPage must beAnInstanceOf[G4NationalityAndResidencyPage]
    }

    "be able to navigate back " in new WithBrowser  with PageObjects{
      val claimDatePage = G1ClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate

      val page =  claimDatePage submitPage()
      val claim = ClaimScenarioFactory yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim
      val contactDetailsPage = page submitPage(waitForPage = true)
      val completedPage = contactDetailsPage goBack ()
      completedPage must beAnInstanceOf[G1YourDetailsPage]
    }


    "Modify address from preview page" in new WithBrowser with PageObjects{

      val page =  G3ContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      val id = "about_you_address"
      val previewPage = PreviewPage(context)
      previewPage goToThePage()
      previewPage.xpath(s"//dt[./a[@id='$id']]/following-sibling::dd").getText mustEqual "101 Clifton Street, Blackpool FY1 2RW"
      val contactDetails = previewPage.clickLinkOrButton(s"#$id")

      contactDetails must beAnInstanceOf[G3ContactDetailsPage]
      val modifiedData = new TestData
      modifiedData.AboutYouAddress = "10 someplace&Wherever"
      modifiedData.AboutYouPostcode = "M4 4TJ"

      contactDetails fillPageWith modifiedData
      val previewPageModified = contactDetails submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      previewPageModified.xpath(s"//dt[./a[@id='$id']]/following-sibling::dd").getText mustEqual "10 someplace, Wherever M4 4TJ"

    }

    "Modify contact number from preview page" in new WithBrowser with PageObjects{

      val page =  G3ContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      val id = "about_you_contact"
      val previewPage = PreviewPage(context)
      previewPage goToThePage()
      previewPage.xpath(s"//dt[./a[@id='$id']]/following-sibling::dd").getText mustEqual "01772 888901"
      val contactDetails = previewPage.clickLinkOrButton(s"#$id")

      contactDetails must beAnInstanceOf[G3ContactDetailsPage]
      val modifiedData = new TestData
      modifiedData.HowWeContactYou = "0123456789"

      contactDetails fillPageWith modifiedData
      val previewPageModified = contactDetails submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      previewPage.xpath(s"//dt[./a[@id='$id']]/following-sibling::dd").getText mustEqual "0123456789"

    }
  } section("integration", models.domain.AboutYou.id)
}