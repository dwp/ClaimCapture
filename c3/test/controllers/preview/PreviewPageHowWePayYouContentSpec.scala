package controllers.preview

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.{PageObjectsContext, PageObjects}
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage
import controllers.ClaimScenarioFactory
import utils.pageobjects.s11_pay_details.G1HowWePayYouPage


class PreviewPageHowWePayYouContentSpec extends Specification with Tags {

  "Preview Page" should {
    "display payment data" in new WithBrowser with PageObjects{

      fillHowWePayYouPage(context)
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source()
      source.contains("Payment method") must beTrue
      source.contains("You dont have an account but intend to open one - Weekly") must beTrue
    }
  }

  def fillHowWePayYouPage(context:PageObjectsContext) = {
    val claimDatePage = G1ClaimDatePage(context)
    claimDatePage goToThePage()
    val claimDate = ClaimScenarioFactory.s12ClaimDate()
    claimDatePage fillPageWith claimDate
    claimDatePage submitPage()

    val howWePayYouPage = G1HowWePayYouPage(context)
    howWePayYouPage goToThePage()
    howWePayYouPage fillPageWith ClaimScenarioFactory.s6PayDetails()
    howWePayYouPage submitPage()

  }
}
