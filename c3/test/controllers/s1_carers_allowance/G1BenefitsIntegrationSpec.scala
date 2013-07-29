package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance.{G1BenefitsPageContext, G2HoursPage}
import utils.pageobjects.ClaimScenario

class G1BenefitsIntegrationSpec extends Specification with Tags {
  "Benefits" should {

    "be presented" in new WithBrowser with G1BenefitsPageContext {
      page goToThePage()
      page.previousPage mustEqual None
      page.hasQ1 must beTrue
    }

    "allow changing answer" in new WithBrowser with  G1BenefitsPageContext {
      page.goToThePage()
      page clickPersonGetsBenefits()
      val nextPage = page submitPage()
      browser.goTo("/allowance/benefits?changing=true")
      page.doesPersonGetBenefit must beSome(true)
    }
  } section "integration"

  "Does the person being cared for get one of required benefits" should {

    val notRightPage: String = "Next Page is not of the right type."

    "acknowledge yes" in new WithBrowser with G1BenefitsPageContext {
      page goToThePage()
      page clickPersonGetsBenefits()
      val nextPage = page submitPage()
      nextPage match {
        case p: G2HoursPage => p.isQ1Yes must beTrue
        case _ => ko(notRightPage)

      }
    }

    "acknowledge yes 2" in new WithBrowser with  G1BenefitsPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "Yes"
      page goToThePage()
      page fillPageWith claim
      val nextPage = page submitPage()
      nextPage match {
        case p: G2HoursPage => p.isQ1Yes must beTrue
        case _ =>  ko(notRightPage)
      }
    }

    "acknowledge no" in new WithBrowser with G1BenefitsPageContext {
      page.goToThePage()
      page clickPersonDoesNotGetBenefits()
      val nextPage = page submitPage()
      nextPage match {
        case p: G2HoursPage => p.isQ1No must beTrue
        case _ =>  ko(notRightPage)
      }
    }

  } section "integration"
}
