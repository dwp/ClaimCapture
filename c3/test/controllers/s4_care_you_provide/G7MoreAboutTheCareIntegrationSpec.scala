package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import controllers.{PreviewTestUtils, ClaimScenarioFactory, BrowserMatchers, Formulate}
import utils.pageobjects._
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.s4_care_you_provide.{G7MoreAboutTheCarePage, G2TheirContactDetailsPage}
import utils.pageobjects.preview.PreviewPage

class G7MoreAboutTheCareIntegrationSpec extends Specification with Tags {
  sequential

  "Representatives For The Person" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = G7MoreAboutTheCarePage(context)
      page goToThePage()
    }

    "contain errors on invalid submission" in new WithBrowser with PageObjects {
      val page = G7MoreAboutTheCarePage(context)
      page goToThePage()
      page submitPage()
      page.listErrors.size mustEqual 1
    }

    "navigate back" in new WithBrowser with PageObjects {
      val theirContactDetailsPage = G2TheirContactDetailsPage(context)
      theirContactDetailsPage goToThePage()
      theirContactDetailsPage fillPageWith ClaimScenarioFactory.s4CareYouProvide(hours35 = true)
      val moreAboutTheCarePage = theirContactDetailsPage submitPage()
      moreAboutTheCarePage goBack() must beAnInstanceOf[G2TheirContactDetailsPage]
    }

    "Modify 'spent 35 hours caring' answer from preview page" in new WithBrowser with PageObjects{
      val previewPage = goToPreviewPage(context)
      val id = "care_you_provide_spent35HoursCaring"
      val answerText = PreviewTestUtils.answerText(s"$id", _:Page)

      answerText(previewPage) mustEqual "No"

      val moreAboutTheCarePage = previewPage.clickLinkOrButton(s"#$id")

      moreAboutTheCarePage must beAnInstanceOf[G7MoreAboutTheCarePage]

      moreAboutTheCarePage fillPageWith ClaimScenarioFactory.s4CareYouProvide(hours35 = true)
      val previewPageModified = moreAboutTheCarePage submitPage()

      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "Yes"
    }

  } section ("integration", models.domain.CareYouProvide.id)

  def goToPreviewPage(context:PageObjectsContext):Page = {
    val claimDatePage = GClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val moreAboutTheCarePage = G7MoreAboutTheCarePage(context)
    moreAboutTheCarePage goToThePage()
    moreAboutTheCarePage fillPageWith ClaimScenarioFactory.s4CareYouProvide(hours35 = false)
    moreAboutTheCarePage submitPage()

    val previewPage = PreviewPage(context)
    previewPage goToThePage()
  }
}