package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.{TestData, PageObjects}
import utils.pageobjects.s1_carers_allowance.{G5CarersResponsePage, G1BenefitsPage, G6ApprovePage}
import utils.pageobjects.s2_about_you.G1YourDetailsPage
import utils.pageobjects.circumstances.s1_about_you.G1ReportAChangeInYourCircumstancesPage

class G5CarersResponseIntegrationSpec extends Specification with Tags {
  "Response" should {
    "page contains JS enabled check" in new WithBrowser with PageObjects {
      val page = G5CarersResponsePage(context)
      page goToThePage()
      page jsCheckEnabled() must beTrue
      page submitPage()
    }
  } section("integration",models.domain.CarersAllowance.id)
}