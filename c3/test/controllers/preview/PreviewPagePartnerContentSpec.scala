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

      source must contain("About your partner")
      source must contain("Name")
      source must contain("Mrs Cloe Scott Smith")
      source must contain("National Insurance number")
      source must contain("AB 12 34 56 A")
      source must contain("Date of birth")
      source must contain("12 July, 1990")
      source must contain("Have you separated since your claim date?")
      source must contain("Yes")
      source must contain("Is this the person you care for?")
      source must contain("Yes")
    }

    "display no data - when no partner" in new WithBrowser with PageObjects{
      val partnerData = new TestData
      partnerData.AboutYourPartnerHadPartnerSinceClaimDate = "No"
      fillPartnerSection(context, partnerData)
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source()

      source must not contain "About your partner/spouse"
      source must not contain "Mrs Cloe Scott Smith"
      source must not contain "AB 12 34 56 A"
      source must not contain "12 July, 1990"
      source must not contain "Have you separated since your claim date?"
    }
  } section "preview"

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
