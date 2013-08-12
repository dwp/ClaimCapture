package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s7_employment._

class G12PersonYouCareForExpensesIntegrationSpec extends Specification with Tags {
  "Person you care for expenses" should {
    "be presented" in new WithBrowser with G8AboutExpensesPageContext {
      val claim = ClaimScenarioFactory s7Employment()
      claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1="no"
      claim.EmploymentDoYouPayAnyoneLookAfterYourChild_1="no"
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }

    "contain 1 completed form" in new WithBrowser with G8AboutExpensesPageContext {
      val claim = ClaimScenarioFactory s7Employment()
      claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1="no"
      claim.EmploymentDoYouPayAnyoneLookAfterYourChild_1="no"
      page goToThePage()
      page fillPageWith claim
      val p = page submitPage()
      p fillPageWith claim
      p submitPage() match {
        case p: G13CareProviderPage => p numberSectionsCompleted()  mustEqual 2
        case _ => ko("Next Page is not of the right type.")
      }
    }

    "be able to navigate back to a completed form" in new WithBrowser  with G8AboutExpensesPageContext {
      val claim = ClaimScenarioFactory s7Employment()
      claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1="no"
      claim.EmploymentDoYouPayAnyoneLookAfterYourChild_1="no"
      page goToThePage()
      page fillPageWith claim
      val p = page submitPage()
      p match {
        case p: G12PersonYouCareForExpensesPage => p numberSectionsCompleted()  mustEqual 1
        case _ => ko("Next Page is not of the right type.")
      }
      p goBack() must beAnInstanceOf[G8AboutExpensesPage]
    }
  } section("integration",models.domain.Employed.id)
}