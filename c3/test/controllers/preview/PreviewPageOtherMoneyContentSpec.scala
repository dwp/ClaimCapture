package controllers.preview

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.{PageObjectsContext, PageObjects}
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage
import controllers.ClaimScenarioFactory
import utils.pageobjects.s9_other_money.G1AboutOtherMoneyPage


class PreviewPageOtherMoneyContentSpec extends Specification with Tags {

  "Preview Page" should {
    "display other money data" in new WithBrowser with PageObjects{

      fillOtherMoneySection(context)
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source().toLowerCase

      source must contain("other payments")
      source must contain("Have you received any payments for the person you care for or any other person since your claim date?".toLowerCase)
      source must contain("Yes - Details provided".toLowerCase)
      source must contain("Have you had any Statutory Sick Pay".toLowerCase)
      source must contain("Yes - Details provided".toLowerCase)
      source must contain("Have you had any SMP, SPP or SAP since your claim date".toLowerCase)
      source must contain("Yes - Details provided".toLowerCase)
    }
  } section "preview"

  def fillOtherMoneySection (context:PageObjectsContext) = {
    val claimDatePage = G1ClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val otherMoneyPage = G1AboutOtherMoneyPage(context)
    otherMoneyPage goToThePage ()
    otherMoneyPage fillPageWith ClaimScenarioFactory.s9otherMoney
    otherMoneyPage submitPage()
  }

}
