package controllers.preview

import controllers.ClaimScenarioFactory
import org.specs2.mutable.{Specification, Tags}
import utils.pageobjects.s_pay_details.GHowWePayYouPage
import utils.{WithBrowser}
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_about_you.{GContactDetailsPage, GOtherEEAStateOrSwitzerlandPage}
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.{PageObjects, PageObjectsContext, TestData}

class PreviewPageBankDetailsSpec extends Specification with Tags {
  "Preview Page" should {
    "display bank data - showing bank details are added" in new WithBrowser with PageObjects {
      val bankData = ClaimScenarioFactory.previewLessThan65WithBankDetails

      fillBankDetailsSection(context, bankData)
      val page = PreviewPage(context)
      page goToThePage()
      val source = page.source
      source must contain("Date of birth")
      source must contain("02 February, 1980")
      source must contain("Bank details")
      source must contain("Yes - details provided")
      source must not contain ("Not given - this may delay any payment you're entitled to.")
    }

    "display bank data - showing bank details are not added" in new WithBrowser with PageObjects {
      val bankData = ClaimScenarioFactory.previewLessThan65WithNoBankDetails

      fillBankDetailsSection(context, bankData)
      val page = PreviewPage(context)
      page goToThePage()
      val source = page.source
      source must contain("Date of birth")
      source must contain("02 February, 1980")
      source must contain("Bank details")
      source must not contain ("Yes - details provided")
      source must contain("Not given - this may delay any payment you're entitled to.")
    }

    "not display bank data - for old person" in new WithBrowser with PageObjects {
      val bankData = ClaimScenarioFactory.previewAboutYouForBankDetails

      fillBankDetailsSection(context, bankData, false)
      val page = PreviewPage(context)
      page goToThePage()
      val source = page.source
      source must contain("Date of birth")
      source must contain("02 February, 1950")
      source must not contain ("Bank details")

    } section "preview"


    def fillBankDetailsSection(context: PageObjectsContext, bankData: TestData, eligibleForBankPage: Boolean = true) = {
      val claimDatePage = GClaimDatePage(context)
      claimDatePage goToThePage()
      val claimDate = ClaimScenarioFactory.s12ClaimDate()
      claimDatePage fillPageWith claimDate
      val aboutYouPage = claimDatePage submitPage()
      aboutYouPage goToThePage()
      aboutYouPage fillPageWith bankData
      aboutYouPage submitPage()

      if (eligibleForBankPage) {
        val bankDetailsPage = GHowWePayYouPage(context)
        bankDetailsPage goToThePage()
        bankDetailsPage fillPageWith bankData
        bankDetailsPage submitPage()
      }
    }
  }
}
