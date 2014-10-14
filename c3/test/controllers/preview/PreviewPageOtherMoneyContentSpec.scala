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
      val source = page.source()

      source.contains("Other money") must beTrue
      source.contains("Have you received any payments for the person you care for or any other person since your claim date?") must beTrue
      source.contains("Yes - Details provided") must beTrue
      source.contains("Have you had any Statutory Sick Pay") must beTrue
      source.contains("Yes - Details provided") must beTrue
      source.contains("Have you had any SMP, SPP or SAP since your claim date") must beTrue
      source.contains("Yes - Details provided") must beTrue
    }
  }

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
