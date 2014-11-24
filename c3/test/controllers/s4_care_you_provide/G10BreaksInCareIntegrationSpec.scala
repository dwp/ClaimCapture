package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers._
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage
import utils.pageobjects._
import utils.pageobjects.s4_care_you_provide.{G7MoreAboutTheCarePage, G11BreakPage, G1TheirPersonalDetailsPage, G10BreaksInCarePage}
import utils.pageobjects.preview.PreviewPage

class G10BreaksInCareIntegrationSpec extends Specification with Tags {
  "Breaks from care" should {
    "present" in new WithBrowser with PageObjects {
      G10BreaksInCarePage(context) goToThePage() must beAnInstanceOf[G10BreaksInCarePage]
    }

    """present "Their personal details" when no more breaks are required""" in new WithBrowser with PageObjects {
      val breaksInCare = G10BreaksInCarePage(context) goToThePage()
      val data = new TestData
      data.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_1 = "no"

      val next = breaksInCare fillPageWith data submitPage()
      next must beAnInstanceOf[G1TheirPersonalDetailsPage]
    }
    
    "display dynamic question text if user answered that they care for this person for 35 hours or more each week before your claim date" in new WithBrowser with PageObjects{
      val breaksInCare = G1ClaimDatePage(context) goToThePage() runClaimWith(ClaimScenarioFactory.s4CareYouProvide(true),G10BreaksInCarePage.title)

      breaksInCare.source() contains "Have you had any breaks from caring for this person since 10 April 2014?" should beTrue

    }
    
    "display dynamic question text if user answered that they did NOT care for this person for 35 hours or more each week before your claim date" in new WithBrowser with PageObjects{
      val breaksInCare = G1ClaimDatePage(context) goToThePage() runClaimWith(ClaimScenarioFactory.s4CareYouProvide(false),G10BreaksInCarePage.title)

      breaksInCare.source() contains "Have you had any breaks from caring for this person since 10 October 2014?" should beTrue
    }

    """not record the "yes/no" answer upon starting to add a new break but "cancel".""" in new WithBrowser with PageObjects {
      val breaksInCare = G10BreaksInCarePage(context) goToThePage()
      val data = new TestData
      data.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_1 = "yes"

      val next = breaksInCare fillPageWith data submitPage()
      next must beAnInstanceOf[G11BreakPage]

      val back = next.goBack()

      back must beAnInstanceOf[G10BreaksInCarePage]

      back.isElemSelected("#answer_yes") should beFalse
      back.isElemSelected("#answer_no") should beFalse
    }

    """allow a new break to be added but not record the "yes/no" answer""" in new WithBrowser with PageObjects {
      val breaksInCare = G1ClaimDatePage(context) goToThePage() runClaimWith(ClaimScenarioFactory.s4CareYouProvideWithBreaksInCare(),G10BreaksInCarePage.title,upToIteration = 2)

      breaksInCare.isElemSelected("#answer_yes") should beFalse
      breaksInCare.isElemSelected("#answer_no") should beFalse
    }

    """remember "no more breaks" upon stating "no more breaks" and returning to "breaks in care".""" in new WithBrowser with PageObjects {
      val breaksInCare = G10BreaksInCarePage(context) goToThePage()
      val data = new TestData
      data.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_1 = "no"

      val next = breaksInCare fillPageWith data submitPage()
      next must beAnInstanceOf[G1TheirPersonalDetailsPage]

      val back = next.goBack()
      back must beAnInstanceOf[G10BreaksInCarePage]

      back.isElemSelected("#answer_yes") should beFalse
      back.isElemSelected("#answer_no") should beTrue
    }

    "Modify 'breaks in care' answer from preview page" in new WithBrowser with PageObjects{
      val previewPage = goToPreviewPage(context)
      val id = "care_you_provide_anyBreaks"
      val answerText = PreviewTestUtils.answerText(s"$id", _:Page)

      answerText(previewPage) mustEqual "No"

      val breaksInCarePage = ClaimPageFactory.buildPageFromFluent(previewPage.click(s"#$id"))

      breaksInCarePage must beAnInstanceOf[G10BreaksInCarePage]

      breaksInCarePage fillPageWith ClaimScenarioFactory.s4CareYouProvideWithBreaksInCare()
      val breakPage = breaksInCarePage submitPage()
      breakPage must beAnInstanceOf[G11BreakPage]

      breakPage fillPageWith ClaimScenarioFactory.s4CareYouProvideWithBreaksInCare()
      val breaksInCarePageModified = breakPage submitPage()
      val testData = new TestData
      testData.AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare_2 = "no"
      breaksInCarePageModified fillPageWith testData

      val previewPageModified = breaksInCarePageModified submitPage()
      previewPageModified must beAnInstanceOf[PreviewPage]
      answerText(previewPageModified) mustEqual "Yes- Details provided for 1 break(s)"
    }

    "Modify 'breaks in care', back button should take you back to the preview page" in new WithBrowser with PageObjects{
      val previewPage = goToPreviewPage(context)
      val id = "care_you_provide_anyBreaks"
      val answerText = PreviewTestUtils.answerText(s"$id", _:Page)

      answerText(previewPage) mustEqual "No"

      val breaksInCarePage = ClaimPageFactory.buildPageFromFluent(previewPage.click(s"#$id"))

      breaksInCarePage must beAnInstanceOf[G10BreaksInCarePage]

      val previewPageModified = breaksInCarePage goBack()
      previewPageModified must beAnInstanceOf[PreviewPage]
    }

    "Modify 'breaks in care', back button on the break page should take you back to breaks in care page" in new WithBrowser with PageObjects{
      val previewPage = goToPreviewPage(context)
      val id = "care_you_provide_anyBreaks"
      val answerText = PreviewTestUtils.answerText(s"$id", _:Page)

      answerText(previewPage) mustEqual "No"

      val breaksInCarePage = ClaimPageFactory.buildPageFromFluent(previewPage.click(s"#$id"))

      breaksInCarePage must beAnInstanceOf[G10BreaksInCarePage]

      breaksInCarePage fillPageWith ClaimScenarioFactory.s4CareYouProvideWithBreaksInCare()
      val breakPage = breaksInCarePage submitPage()
      breakPage must beAnInstanceOf[G11BreakPage]
      breakPage goBack() must beAnInstanceOf[G10BreaksInCarePage]
    }

  } section("integration", models.domain.CareYouProvide.id)

  def goToPreviewPage(context:PageObjectsContext):Page = {
    val claimDatePage = G1ClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val breaksInCarePage = G10BreaksInCarePage(context)
    breaksInCarePage goToThePage()
    breaksInCarePage fillPageWith ClaimScenarioFactory.s4CareYouProvideWithNoBreaksInCare()
    breaksInCarePage submitPage()

    val previewPage = PreviewPage(context)
    previewPage goToThePage()
  }

}