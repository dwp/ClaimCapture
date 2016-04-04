package controllers.preview

import org.specs2.mutable._
import utils.WithBrowser
import utils.pageobjects.your_income.GStatutorySickPayPage
import utils.pageobjects.{PageObjectsContext, PageObjects}
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import controllers.ClaimScenarioFactory

class PreviewPageYourIncomeOtherPaymentsContentSpec extends Specification {
  section("preview")
  "Preview Page" should {
    "display other money data" in new WithBrowser with PageObjects {
      fillStatutorySickPaySection(context)
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source.toLowerCase

      source must contain("other payments")
      source must contain("Have you received any payments for the person you care for or any other person since your claim date?".toLowerCase)
      source must contain("Yes - Details provided".toLowerCase)
      source must contain("Have you had any Statutory Sick Pay".toLowerCase)
      source must contain("Yes - Details provided".toLowerCase)
      source must contain("Have you had any Statutory Maternity Pay, Statutory Paternity Pay or Statutory Adoption Pay".toLowerCase)
      source must contain("Yes - Details provided".toLowerCase)
    }
  }
  section("preview")

  def fillStatutorySickPaySection (context:PageObjectsContext) = {
    val claimDatePage = GClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val statutorySickPayPage = GStatutorySickPayPage(context)
    statutorySickPayPage goToThePage ()
    statutorySickPayPage fillPageWith ClaimScenarioFactory.s9OtherIncome
    statutorySickPayPage submitPage()
  }
}
