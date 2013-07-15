package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance.{BenefitsPageContext, HoursPage}
import utils.pageobjects.ClaimScenario

class G1BenefitsIntegrationSpec extends Specification with Tags {
  sequential

  "Benefits" should {

    "be presented" in new WithBrowser with BenefitsPageContext {
      page.goToThePage()
      page.hasQ1 must beTrue
    }

    "allow changing answer" in new WithBrowser with  BenefitsPageContext {
      page.goToThePage()
      page clickPersonGetsBenefits()
      val nextPage = page submitPage()
      browser.goTo("/allowance/benefits?changing=true")
      page.doesPersonGetBenefit must beSome(true)
    }
  } section "integration"

  "Does the person being cared for get one of required benefits" should {

    "acknowledge yes" in new WithBrowser with BenefitsPageContext {
      page goToThePage()
      page clickPersonGetsBenefits()
      val nextPage = page submitPage()
      nextPage match {
        case p: HoursPage => p.isQ1Yes must beTrue
        case _ => false must beTrue
      }
    }

    "acknowledge yes 2" in new WithBrowser with  BenefitsPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowance_DoesPpersonYouCareForGetOneOfTheseBenefits = "Yes"
      page goToThePage()
      page fillPageWith(claim)
      val nextPage = page submitPage()
      nextPage match {
        case p: HoursPage => p.isQ1Yes must beTrue
        case _ => false must beTrue
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