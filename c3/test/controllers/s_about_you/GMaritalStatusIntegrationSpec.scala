package controllers.s_about_you

import app.MaritalStatus
import controllers.{ClaimScenarioFactory, PreviewTestUtils}
import org.specs2.mutable._
import utils.WithBrowser
import utils.pageobjects._
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.s_about_you.{GContactDetailsPage, GMaritalStatusPage, GNationalityAndResidencyPage}
import utils.helpers.PreviewField._

class GMaritalStatusIntegrationSpec extends Specification {
  sequential

  section("integration", models.domain.AboutYou.id)
  "Status" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  GMaritalStatusPage(context)
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithBrowser with PageObjects{
			val page =  GMaritalStatusPage(context)
      page goToThePage()
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GMaritalStatusPage]
    }

    "navigate to next page on valid Status submission" in new WithBrowser with PageObjects{
			val page =  GMaritalStatusPage(context)
      val claim = ClaimScenarioFactory.maritalStatus()
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[GContactDetailsPage]
    }

    "Modify Status from preview page" in new WithBrowser with PageObjects{
      val previewPage = goToPreviewPage(context)
      val id = "about_you_marital_status"
      val answerText = PreviewTestUtils.answerText(id, _:Page)

      answerText(previewPage) mustEqual MaritalStatus.Single
      val maritalStatusPage = previewPage.clickLinkOrButton(getLinkId(id))

      maritalStatusPage must beAnInstanceOf[GMaritalStatusPage]
      val modifiedData = new TestData
      modifiedData.AboutYouWhatIsYourMaritalOrCivilPartnershipStatus = MaritalStatus.Married

      maritalStatusPage fillPageWith modifiedData
      val previewPageModified = maritalStatusPage submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual MaritalStatus.Married
    }
  }
  section("integration", models.domain.AboutYou.id)

  def goToPreviewPage(context:PageObjectsContext):Page = {
    val claimDatePage = GClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val maritalStatusPage = GMaritalStatusPage(context)
    maritalStatusPage goToThePage()
    maritalStatusPage fillPageWith ClaimScenarioFactory.maritalStatus()
    maritalStatusPage.submitPage()

    val nationalityPage = GNationalityAndResidencyPage(context)
    val claim = ClaimScenarioFactory.yourNationalityAndResidencyNonResident
    nationalityPage goToThePage()
    nationalityPage fillPageWith claim
    nationalityPage submitPage()

    val previewPage = PreviewPage(context)
    previewPage goToThePage()
  }
}
