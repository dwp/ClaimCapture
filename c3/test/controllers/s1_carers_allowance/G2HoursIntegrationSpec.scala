package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance.{Over16Page, HoursPage, BenefitsPageContext, HoursPageContext}
import utils.pageobjects.ClaimScenario

class G2HoursIntegrationSpec extends Specification with Tags {

  "Hours" should {
    "be presented" in new WithBrowser with HoursPageContext {
      page goToThePage()
    }
  } section "integration"

  "Do you spend 35 hours or more each week caring" should {
    "acknowledge yes" in new WithBrowser with BenefitsPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "Yes"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "Yes"
      page goToThePage()
      page fillPageWith(claim)
      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[HoursPage]
      nextPage.previousPage mustEqual Some(page)
      nextPage fillPageWith(claim)
      val nextPage2 = nextPage submitPage()
      nextPage2 match {
        case p: Over16Page => p isQ2Yes() must beTrue
        case _ => false must beTrue
      }
    }

    "acknowledge no" in new WithBrowser with BenefitsPageContext {
      val claim = new ClaimScenario
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "Yes"
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "No"
      page goToThePage()
      page fillPageWith(claim)
      val nextPage = page submitPage()
      nextPage must beAnInstanceOf[HoursPage]
      nextPage fillPageWith(claim)
      val nextPage2 = nextPage submitPage()
      nextPage2 match {
        case p: Over16Page => p isQ2No() must beTrue
        case _ => false must beTrue
      }
    }
  } section "integration"
}