package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.{BenefitsPageContext, HoursPage}

class G1BenefitsIntegrationSpec extends Specification with Tags {

  "Benefits" should {

    "be presented" in new WithBrowser with BenefitsPageContext {
      page.goToThePage()
      page.hasQ1 must beTrue
    }

    "allow changing answer" in new WithBrowser with BenefitsPageContext {
      page.goToThePage()
      page clickPersonGetsBenefits()
      val nextPage = page submitPage()
      browser.goTo("/allowance/benefits?changing=true")
      page.doesPersonGetBenefit must beSome(true)
    }

  } section "integration"


  "Does the person being cared for get one of required benefits" should {

    "acknowledge yes" in new WithBrowser with BenefitsPageContext {
      page.goToThePage()
      page clickPersonGetsBenefits()
      val nextPage = page.submitPage()
      nextPage match {
        case p: HoursPage => p.isQ1Yes must beTrue
        case _ => failure
      }
    }

    "acknowledge no" in new WithBrowser with BenefitsPageContext {
      page.goToThePage()
      page clickPersonDoesNotGetBenefits()
      val nextPage = page submitPage()
      nextPage match {
        case p: HoursPage => p.isQ1No() must beTrue
        case _ => failure
      }
    }

  } section "integration"
}
