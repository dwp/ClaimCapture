package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{PreviewTestUtils, ClaimScenarioFactory, BrowserMatchers, Formulate}
import utils.pageobjects._
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage
import utils.pageobjects.s4_care_you_provide.{G10BreaksInCarePage, G7MoreAboutTheCarePage, G1TheirPersonalDetailsPage, G2TheirContactDetailsPage}
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s2_about_you.G2ContactDetailsPage

class G2TheirContactDetailsIntegrationSpec extends Specification with Tags {

  "Their Contact Details" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = G2TheirContactDetailsPage(context)
      page goToThePage()
      page.pageTitle mustEqual "Contact details of the person you care for - About the care you provide".toLowerCase
    }

    "contain errors on empty submission" in new WithBrowser with PageObjects {
      val page = G2TheirContactDetailsPage(context)
      page goToThePage()
      page submitPage()

      page.listErrors.size mustEqual 1
    }

    "be jumped if they live at same address" in new WithBrowser with PageObjects {
      val thierContactDetailsPage = goToTheirContactDetailsPage(context, ClaimScenarioFactory.s4CareYouProvide(hours35 = true,liveSameAddress = true))

      thierContactDetailsPage must beAnInstanceOf[G7MoreAboutTheCarePage]
    }

    "be blank if they live at different address" in new WithBrowser with PageObjects {

      val theirPersonalData = ClaimScenarioFactory.s4CareYouProvide(hours35 = true)
      theirPersonalData.AboutTheCareYouProvideDoTheyLiveAtTheSameAddressAsYou = "No"

      val thierContactDetailsPage = goToTheirContactDetailsPage(context, theirPersonalData)
      thierContactDetailsPage must beAnInstanceOf[G2TheirContactDetailsPage]

      val addressLineOne = thierContactDetailsPage readInput("#address_lineOne")
      val postCode = thierContactDetailsPage readInput("#postcode")
      addressLineOne.get mustEqual ""
      postCode.get mustEqual ""
    }

    "navigate back to Their Personal Details" in new WithBrowser with PageObjects {
      val thierContactDetailsPage = goToTheirContactDetailsPage(context, ClaimScenarioFactory.s4CareYouProvide(hours35 = true,liveSameAddress = true))
      thierContactDetailsPage must beAnInstanceOf[G7MoreAboutTheCarePage]
      thierContactDetailsPage goBack() must beAnInstanceOf[G1TheirPersonalDetailsPage]
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects {
      val thierContactDetailsPage = goToTheirContactDetailsPage(context, ClaimScenarioFactory.s4CareYouProvide(hours35 = true,liveSameAddress = true))
      thierContactDetailsPage fillPageWith ClaimScenarioFactory.s4CareYouProvide(hours35 = true)
      thierContactDetailsPage submitPage() must beAnInstanceOf[G10BreaksInCarePage]
    }

    "Modify address from preview page" in new WithBrowser with PageObjects{
      val modifiedData = new TestData
      modifiedData.AboutTheCareYouProvideAddressPersonCareFor = "123 Colne Street&Line 3"
      modifiedData.AboutTheCareYouProvidePostcodePersonCareFor = "BB6 2AD"

      verifyPreviewData(context, "care_you_provide_address", "123 Colne Street, Line 2 BB9 2AD", modifiedData, "123 Colne Street, Line 3 BB6 2AD")
    }

  } section("integration", models.domain.CareYouProvide.id)

  def goToTheirContactDetailsPage (context:PageObjectsContext, testData:TestData) = {
    val yourContactDetailsPage = G2ContactDetailsPage(context)
    yourContactDetailsPage goToThePage()
    yourContactDetailsPage fillPageWith ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
    yourContactDetailsPage submitPage()

    val theirPersonalDetailsPage = G1TheirPersonalDetailsPage(context)
    theirPersonalDetailsPage goToThePage()
    theirPersonalDetailsPage fillPageWith testData
    theirPersonalDetailsPage submitPage()
  }

  def goToPreviewPage(context:PageObjectsContext):Page = {
    val claimDatePage = G1ClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val thierContactDetailsPage = G2TheirContactDetailsPage(context)
    thierContactDetailsPage goToThePage()
    thierContactDetailsPage fillPageWith ClaimScenarioFactory.s4CareYouProvide(hours35 = true)
    thierContactDetailsPage submitPage()

    val previewPage = PreviewPage(context)
    previewPage goToThePage()
  }

  def verifyPreviewData(context:PageObjectsContext, id:String, initialData:String, modifiedTestData:TestData, modifiedData:String) = {
    val previewPage = goToPreviewPage(context)
    val answerText = PreviewTestUtils.answerText(id, _:Page)

    answerText(previewPage) mustEqual initialData
    val theirContactDetailsPage = ClaimPageFactory.buildPageFromFluent(previewPage.click(s"#$id"))

    theirContactDetailsPage must beAnInstanceOf[G2TheirContactDetailsPage]

    theirContactDetailsPage fillPageWith modifiedTestData
    val previewPageModified = theirContactDetailsPage submitPage()

    previewPageModified must beAnInstanceOf[PreviewPage]
    answerText(previewPageModified) mustEqual modifiedData
  }

}