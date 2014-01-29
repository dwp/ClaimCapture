package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s7_employment._
import utils.pageobjects.IterationManager

class G10ChildcareExpensesIntegrationSpec extends Specification with Tags with AboutYouAndYourPartner {
  "Childcare expenses while you are at work - Integration" should {
    "be presented" in new WithBrowser with G8AboutExpensesPageContext {
      val claim = ClaimScenarioFactory s7Employment()
      claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1 = "no"
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }

    "contain 2 completed forms" in new WithBrowser with G8AboutExpensesPageContext {
      IterationManager.init()
      aboutYouAndPartner(browser)

      val claim = ClaimScenarioFactory s7Employment()
      claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1 = "no"
      page goToThePage()
      page fillPageWith claim
      val s7g10 = page submitPage()
      s7g10 fillPageWith claim

      val s7G10CompletedPage = s7g10.submitPage()

      s7G10CompletedPage should beLike {
        case p: G12PersonYouCareForExpensesPage => p numberSectionsCompleted() must equalTo(2)
      }
    }

    "be able to navigate back to a completed form" in new WithBrowser with G8AboutExpensesPageContext {
      val claim = ClaimScenarioFactory s7Employment()
      claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1 = "no"
      page goToThePage()
      page fillPageWith claim
      val p = page submitPage()
      p goBack() must beAnInstanceOf[G8AboutExpensesPage]
    }
  } section("integration", models.domain.Employed.id)
}