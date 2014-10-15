package controllers.preview

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.{TestData, PageObjectsContext, PageObjects}
import utils.pageobjects.preview.PreviewPage
import controllers.ClaimScenarioFactory
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage
import utils.pageobjects.s3_your_partner.G1YourPartnerPersonalDetailsPage


class PreviewPagePartnerContentSpec extends Specification with Tags {

  "Preview Page" should {
    "display partner data - when you have a partner" in new WithBrowser with PageObjects{
      fillPartnerSection(context)
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source()

      source.contains("About your partner") must beTrue
      source.contains("Name") must beTrue
      source.contains("Mrs Cloe Scott Smith") must beTrue
      source.contains("National Insurance number") must beTrue
      source.contains("AB 12 34 56 A") must beTrue
      source.contains("Date of birth") must beTrue
      source.contains("12 July, 1990") must beTrue
      source.contains("Have you separated since your claim date?") must beTrue
      source.contains("Yes") must beTrue
      source.contains("Is this the person you care for?") must beTrue
      source.contains("Yes") must beTrue
    }

    "display no data - when no partner" in new WithBrowser with PageObjects{
      val partnerData = new TestData
      partnerData.AboutYourPartnerHadPartnerSinceClaimDate = "No"
      fillPartnerSection(context, partnerData)
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source()

      source.contains("About your partner/spouse") must beFalse
      source.contains("Mrs Cloe Scott Smith") must beFalse
      source.contains("AB 12 34 56 A") must beFalse
      source.contains("12 July, 1990") must beFalse
      source.contains("Have you separated since your claim date?") must beFalse
    }
  }

  def fillPartnerSection(context:PageObjectsContext, partnerClaim:TestData = ClaimScenarioFactory.s2ands3WithTimeOUtsideUKAndProperty) = {
    val claimDatePage = G1ClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate

    val aboutYouPage =  claimDatePage submitPage()
    val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
    claim.AboutYouMiddleName = "partnername"
    aboutYouPage goToThePage()
    aboutYouPage fillPageWith claim
    aboutYouPage submitPage()

    val partnerPage =  G1YourPartnerPersonalDetailsPage(context)
    partnerPage goToThePage ()
    partnerPage fillPageWith partnerClaim
    partnerPage submitPage()

  }

}
