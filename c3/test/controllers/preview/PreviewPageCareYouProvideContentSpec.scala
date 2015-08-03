package controllers.preview

import org.openqa.selenium.By
import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import utils.pageobjects.s_about_you.GContactDetailsPage
import utils.pageobjects.{ClaimPageFactory, TestData, PageObjectsContext, PageObjects}
import utils.pageobjects.preview.PreviewPage
import controllers.ClaimScenarioFactory
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.s_your_partner.GYourPartnerPersonalDetailsPage
import utils.pageobjects.s4_care_you_provide.G1TheirPersonalDetailsPage
import utils.WithJsBrowser


class PreviewPageCareYouProvideContentSpec extends Specification with Tags {

  "Preview Page" should {
    "display Care you provide data - when partner is not the person you care for" in new WithJsBrowser  with PageObjects{

      val partnerData = ClaimScenarioFactory.s2ands3WithTimeOUtsideUKAndProperty()
      partnerData.AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor = "No"

      fillCareProvideSection(context,partnerClaim = partnerData)
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source

      source must contain("About the person you care for")
      source must contain("Mr Tom Potter Wilson")
      source must contain("AA123456A")
      source must contain("02 March, 1990")
      source must contain("123 Colne Street, Line 2 BB9 2AD")
      source must contain("Father")
      source must contain("Yes- Details provided for 1 break(s)")
    }

    "display Care you provide data - when partner is the person you care for" in new WithJsBrowser  with PageObjects{

      val partnerData = ClaimScenarioFactory.s2ands3WithTimeOUtsideUKAndProperty()
      partnerData.AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor = "Yes"

      val careYouProvideData = ClaimScenarioFactory.s4CareYouProvideWithNoPersonalDetails

      fillCareProvideSection(context,partnerClaim = partnerData, careYouProvideData)
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source

      source must contain("About the person you care for")
      source must contain("123 Colne Street, Line 2 BB9 2AD")
      source must contain("Father")
      source must contain("Yes- Details provided for 1 break(s)")
    }

    "display Care you provide data - when no partner" in new WithJsBrowser  with PageObjects{

      val partnerData = new TestData
      partnerData.AboutYourPartnerHadPartnerSinceClaimDate = "No"

      fillCareProvideSection(context,partnerClaim = partnerData)
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source

      source must contain("About the person you care for")
      source must contain("Mr Tom Potter Wilson")
      source must contain("AA123456A")
      source must contain("02 March, 1990")
      source must contain("123 Colne Street, Line 2 BB9 2AD")
      source must contain("Father")
      source must contain("Yes- Details provided for 1 break(s)")
    }

    "display Care you provide data without link on caree address" in new WithJsBrowser with PageObjects{
      val partnerData = ClaimScenarioFactory.s2ands3WithTimeOUtsideUKAndProperty()
      partnerData.AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor = "Yes"


      val careYouProvideData = ClaimScenarioFactory.s4CareYouProvideWithNoPersonalDetails
      careYouProvideData.AboutTheCareYouProvideDoTheyLiveAtTheSameAddressAsYou = "Yes"

      fillCareProvideSection(context,partnerClaim = partnerData, careYouProvideData)
      val page =  PreviewPage(context)
      page goToThePage()

      page.ctx.browser.webDriver.findElement(By.id("care_you_provide_address")).getTagName mustEqual "p"
    }

    "display Care you provide data with link on caree address" in  new WithJsBrowser  with PageObjects{
      val partnerData = new TestData
      partnerData.AboutYourPartnerHadPartnerSinceClaimDate = "No"

      fillCareProvideSection(context,partnerClaim = partnerData)
      val page =  PreviewPage(context)
      page goToThePage()
      page.ctx.browser.webDriver.findElement(By.id("care_you_provide_address")).getTagName mustEqual "a"
    }

    "update caree address if modifying carer address when answered caree lives same address" in new WithJsBrowser with PageObjects {
      val partnerData = ClaimScenarioFactory.s2ands3WithTimeOUtsideUKAndProperty()
      partnerData.AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor = "Yes"

      val careYouProvideData = ClaimScenarioFactory.s4CareYouProvideWithNoPersonalDetails
      careYouProvideData.AboutTheCareYouProvideDoTheyLiveAtTheSameAddressAsYou = "Yes"


      fillCareProvideSection(context,partnerClaim = partnerData, careYouProvideData)
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source

      source must contain("About the person you care for")
      source must contain("101 Clifton Street, Blackpool FY1 2RW")
      source must contain("Father")
      source must contain("Yes- Details provided for 1 break(s)")

      val carerAddressPage = page.clickLinkOrButton("#about_you_address")
//      val carerAddressPage = previewPage.clickLinkOrButton(s"#$id")carerAddress)

      carerAddressPage must beAnInstanceOf[GContactDetailsPage]

      val newAddress = new TestData
      newAddress.AboutYouAddress = "Something totally different&Manchester"

      carerAddressPage fillPageWith(newAddress)
      val preview = carerAddressPage.submitPage()

      preview must beAnInstanceOf[PreviewPage]
      val newSource = preview.source

      newSource must contain("Something totally different, Manchester FY1 2RW")
      newSource must not(contain("101 Clifton Street, Blackpool FY1 2RW"))

    }
  }section "preview"

  def fillCareProvideSection(context:PageObjectsContext, partnerClaim:TestData = ClaimScenarioFactory.s2ands3WithTimeOUtsideUKAndProperty(),
                             careYouProvideData:TestData = ClaimScenarioFactory.s4CareYouProvideWithBreaksInCare(true)) = {
    val claimDatePage = GClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate

    val aboutYouPage =  claimDatePage submitPage()
    val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
    claim.AboutYouMiddleName = "careyouprovidemiddlename"
    aboutYouPage goToThePage()
    aboutYouPage fillPageWith claim
    val maritalStatusPage = aboutYouPage submitPage()
    maritalStatusPage fillPageWith claim
    val contactDetails = maritalStatusPage submitPage()

    contactDetails fillPageWith claim
    contactDetails submitPage()

    val partnerPage =  GYourPartnerPersonalDetailsPage(context)
    partnerPage goToThePage ()
    partnerPage fillPageWith partnerClaim
    partnerPage submitPage()

    val careYouProvidePage = G1TheirPersonalDetailsPage(context)
    careYouProvidePage goToThePage()
    careYouProvidePage fillPageWith careYouProvideData

    val thierContactDetailsPage = careYouProvidePage submitPage()
    thierContactDetailsPage fillPageWith careYouProvideData

    val moreAboutTheCarePage = thierContactDetailsPage submitPage()
    moreAboutTheCarePage fillPageWith careYouProvideData

    val breaksIncarePage = moreAboutTheCarePage submitPage()
    breaksIncarePage fillPageWith careYouProvideData

    val breakDetailsPage = breaksIncarePage submitPage()
    breakDetailsPage fillPageWith careYouProvideData
    breakDetailsPage submitPage()

  }
}