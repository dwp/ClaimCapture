package controllers.s2_about_you

import app.MaritalStatus
import controllers.{ClaimScenarioFactory, PreviewTestUtils}
import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import utils.pageobjects._
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage
import utils.pageobjects.s2_about_you.{G3ContactDetailsPage, G2MaritalStatusPage, G4NationalityAndResidencyPage, G5AbroadForMoreThan52WeeksPage}

class G2MaritalStatusIntegrationSpec extends Specification with Tags {
  sequential

  "Marital Status" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G2MaritalStatusPage(context)
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithBrowser with PageObjects{
			val page =  G2MaritalStatusPage(context)
      page goToThePage()
      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G2MaritalStatusPage]
    }

    "navigate to next page on valid marital status submission" in new WithBrowser with PageObjects{
			val page =  G2MaritalStatusPage(context)
      val claim = ClaimScenarioFactory.maritalStatus()
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G3ContactDetailsPage]
    }

    "Modify marital status from preview page" in new WithBrowser with PageObjects{
      val previewPage = goToPreviewPage(context)
      val id = "about_you_marital_status"
      val answerText = PreviewTestUtils.answerText(id, _:Page)

      answerText(previewPage) mustEqual MaritalStatus.Single
      val maritalStatusPage = previewPage.clickLinkOrButton(s"#$id")

      maritalStatusPage must beAnInstanceOf[G2MaritalStatusPage]
      val modifiedData = new TestData
      modifiedData.AboutYouWhatIsYourMaritalOrCivilPartnershipStatus = MaritalStatus.Married

      maritalStatusPage fillPageWith modifiedData
      val previewPageModified = maritalStatusPage submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual MaritalStatus.Married
    }


  } section("integration", models.domain.AboutYou.id)

  def goToPreviewPage(context:PageObjectsContext):Page = {
    val claimDatePage = G1ClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val maritalStatusPage = G2MaritalStatusPage(context)
    maritalStatusPage goToThePage()
    maritalStatusPage fillPageWith ClaimScenarioFactory.maritalStatus()
    maritalStatusPage.submitPage()

    val nationalityPage = G4NationalityAndResidencyPage(context)
    val claim = ClaimScenarioFactory.yourNationalityAndResidencyNonResident
    nationalityPage goToThePage()
    nationalityPage fillPageWith claim
    nationalityPage submitPage()

    val previewPage = PreviewPage(context)
    previewPage goToThePage()
  }

}
