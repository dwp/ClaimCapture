package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s7_employment._

class G13CareProviderIntegrationSpec extends Specification with Tags {
  "Childcare Provider" should {
    "be presented" in new WithBrowser with G8AboutExpensesPageContext {
      val claim = ClaimScenarioFactory s7Employment()
      claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1="no"
      claim.EmploymentDoYouPayAnyoneLookAfterYourChild_1="no"
      page goToThePage()
      page fillPageWith claim
      val p = page submitPage()
      p fillPageWith claim
      p submitPage()
    }

    "contain 1 completed form" in new WithBrowser with G8AboutExpensesPageContext {
      val claim = ClaimScenarioFactory s7Employment()
      claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1="no"
      claim.EmploymentDoYouPayAnyoneLookAfterYourChild_1="no"
      page goToThePage()
      page fillPageWith claim
      val p = page submitPage true
      p fillPageWith claim
      val p2 = p submitPage true
      p2 fillPageWith claim
      val submitted = p2 submitPage true

      submitted should beLike { case p: G14JobCompletionPage => p numberSectionsCompleted() shouldEqual 3 }
    }

    "be able to navigate back to a completed form" in new WithBrowser  with G8AboutExpensesPageContext {
      val claim = ClaimScenarioFactory s7Employment()
      claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1="no"
      claim.EmploymentDoYouPayAnyoneLookAfterYourChild_1="no"
      page goToThePage()
      page fillPageWith claim
      val p = page submitPage()
      p fillPageWith claim
      val p2 = p submitPage()
      p2 goBack() must beAnInstanceOf[G12PersonYouCareForExpensesPage]
    }
  } section("integration", models.domain.Employed.id)
}