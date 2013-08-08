package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{WithBrowser, FakeRequest, WithApplication}
import models.domain._
import play.api.test.Helpers._
import utils.pageobjects.s2_about_you.{G7PropertyAndRentPage, G4ClaimDatePageContext}
import controllers.ClaimScenarioFactory
import utils.pageobjects.s8_self_employment.{G9CompletedPage, G8CareProvidersContactDetailsPage}
import utils.pageobjects.s9_other_money.G1AboutOtherMoneyPage

class G9CompletedSpec extends Specification with Tags {
  
  val selfEmploymentInput = Seq("" -> "")
    
  "Self Employment - Controller" should {
    "present 'Completed'" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.s8_self_employment.SelfEmployment.completed(request)
      status(result) mustEqual OK
    }

    "not present 'Completed' if section not visible" in new WithBrowser with G4ClaimDatePageContext {
      val claim = ClaimScenarioFactory.s2AnsweringNoToQuestions()
      page goToThePage()
      page runClaimWith (claim, G7PropertyAndRentPage.title, waitForPage = true, waitDuration = 500)

      val nextPage = page goToPage( throwException = false, page = new G9CompletedPage(browser))
      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }
    
    "redirect to the next page on clicking continue" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(selfEmploymentInput: _*)

      val result = controllers.s8_self_employment.SelfEmployment.completedSubmit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section("unit",models.domain.SelfEmployment.id)
}