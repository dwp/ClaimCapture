package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{TestBrowser, WithBrowser}
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s2_about_you._
import utils.pageobjects.s1_carers_allowance.G6ApprovePage
import controllers.ClaimScenarioFactory
import utils.pageobjects.{TestData, PageObjectsContext, ClaimPageFactory, PageObjects}
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage

class G1YourDetailsIntegrationSpec extends Specification with Tags {
  "Your Details" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G1YourDetailsPage(context)
      page goToThePage()
    }

    "navigate back to approve page" in new WithBrowser with PageObjects{
			val page =  G1YourDetailsPage(context)
      browser goTo "/allowance/approve"

      page goToThePage()
      val backPage = page goBack()
      backPage must beAnInstanceOf[G6ApprovePage]
    }

    "present errors if mandatory fields are not populated" in new WithBrowser with PageObjects{
			val page =  G1YourDetailsPage(context)
      page goToThePage()
      page.submitPage().listErrors.size mustEqual 6
    }

    "Accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects{
      val claimDatePage = G1ClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate

			val page =  claimDatePage submitPage()
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim

      val g2 = page submitPage()
      
      g2 must beAnInstanceOf[G2ContactDetailsPage]
    }

    "Modify title, name, middlename and last name on preview page" in new WithBrowser with PageObjects{

      val claimDatePage = G1ClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate

      val page =  claimDatePage submitPage()
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      page goToThePage()
      page fillPageWith claim
      page submitPage()

      val previewPage = PreviewPage(context)
      previewPage goToThePage()
      previewPage source() must contain("Mr John Appleseed")
      val aboutYou = ClaimPageFactory.buildPageFromFluent(previewPage.click("#about_you_full_name"))

      aboutYou must beAnInstanceOf[G1YourDetailsPage]
      val modifiedData = new TestData
      modifiedData.AboutYouTitle = "Mrs"
      modifiedData.AboutYouFirstName = "Jane"
      modifiedData.AboutYouSurname = "Pearson"

      aboutYou fillPageWith modifiedData
      val previewPageModified = aboutYou submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      previewPageModified source() must contain("Mrs Jane Pearson")

    }
  } section("integration", models.domain.AboutYou.id)
}