package controllers.preview

import org.specs2.mutable.{Tags, Specification}
import utils.WithBrowser
import utils.pageobjects.{TestData, PageObjectsContext, PageObjects}
import utils.pageobjects.preview.PreviewPage
import controllers.ClaimScenarioFactory
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.s_your_partner.GYourPartnerPersonalDetailsPage


class PreviewPagePartnerContentSpec extends Specification with Tags {

  "Preview Page" should {
    "display partner data - when you have a partner" in new WithBrowser with PageObjects{
      fillPartnerSection(context)
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source

      source must contain("About your partner")
      source must contain("Name")
      source must contain("Mrs Cloe Scott Smith")
      source must contain("Date of birth")
      source must contain("12 July, 1990")
      source must contain("Have you separated since your claim date?")
      source must contain("Yes")
      source must contain("Is this the person you care for?")
      source must contain("Yes")
    }

    "display partner data - when you have a partner and you have a different title" in new WithBrowser with PageObjects{
      val aboutYouClaim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      aboutYouClaim.AboutYouTitle = "Other"
      aboutYouClaim.AboutYouTitleOther = "Lord"
      val partnerClaim = ClaimScenarioFactory.s2ands3WithTimeOUtsideUKAndProperty()
      fillPartnerSection(context, partnerClaim, aboutYouClaim)
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source

      source must contain("Lord John Appleseed")
      source must contain("About your partner")
      source must contain("Name")
      source must contain("Mrs Cloe Scott Smith")
      source must contain("Date of birth")
      source must contain("12 July, 1990")
      source must contain("Have you separated since your claim date?")
      source must contain("Yes")
      source must contain("Is this the person you care for?")
      source must contain("Yes")
    }

    "display partner data - when you have a partner and your partner has a different title" in new WithBrowser with PageObjects{
      val aboutYouClaim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      val partnerClaim = ClaimScenarioFactory.s2ands3WithTimeOUtsideUKAndProperty()
      partnerClaim.AboutYourPartnerTitle = "Other"
      partnerClaim.AboutYourPartnerTitleOther = "Lady"

      fillPartnerSection(context, partnerClaim, aboutYouClaim)
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source

      source must contain("Mr John Appleseed")
      source must contain("About your partner")
      source must contain("Name")
      source must contain("Lady Cloe Scott Smith")
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
      val source = page.source

      source must contain ("About your partner")
      source must contain ("Have you lived with a partner at any time since your claim date?")
      source must contain ("No")

      source must not contain "Mrs Cloe Scott Smith"
      source must not contain "12 July, 1990"
      source must not contain "Have you separated since your claim date?"
    }
  } section "preview"

  def fillPartnerSection(context:PageObjectsContext, partnerClaim:TestData = ClaimScenarioFactory.s2ands3WithTimeOUtsideUKAndProperty(),
                          aboutYouClaim:TestData = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()) = {
    val claimDatePage = GClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate

    val aboutYouPage =  claimDatePage submitPage()
    aboutYouPage goToThePage()
    aboutYouPage fillPageWith aboutYouClaim
    aboutYouPage submitPage()

    val partnerPage =  GYourPartnerPersonalDetailsPage(context)
    partnerPage goToThePage ()
    partnerPage fillPageWith partnerClaim
    partnerPage submitPage()

  }

}
