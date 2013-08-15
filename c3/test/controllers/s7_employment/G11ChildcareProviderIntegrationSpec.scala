package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s7_employment._

class G11ChildcareProviderIntegrationSpec extends Specification with Tags with AboutYouAndYourPartner{

  "Childcare provider's contact details - Integration" should {
    "be presented" in new WithBrowser with G8AboutExpensesPageContext {

      aboutYouAndPartner (browser)

      val claim = ClaimScenarioFactory s7Employment()
      claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1="no"
      page goToThePage()
      page fillPageWith claim

      val p = page submitPage()
      p fillPageWith claim
      p submitPage()
    }



    "contain 1 completed form" in new WithBrowser with G8AboutExpensesPageContext {

      aboutYouAndPartner (browser)

      val claim = ClaimScenarioFactory s7Employment()
      claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1="no"
      page goToThePage()
      page fillPageWith claim
      val p = page submitPage(true)
      p fillPageWith claim
      val p2 = p submitPage(true)
      p2 fillPageWith claim
      val submitted = p2 submitPage(true)
      submitted must beAnInstanceOf[G12PersonYouCareForExpensesPage]
      submitted match {
        case p: G12PersonYouCareForExpensesPage => p numberSectionsCompleted()  mustEqual 3
        case _ => ko("Next Page is not of the right type.")
      }
    }

    "be able to navigate back to a completed form" in new WithBrowser  with G8AboutExpensesPageContext {

      aboutYouAndPartner (browser)

      val claim = ClaimScenarioFactory s7Employment()
      claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1="no"
      page goToThePage()
      page fillPageWith claim
      val p = page submitPage()
      p fillPageWith claim
      val p2 = p submitPage()
      p2 goBack() must beAnInstanceOf[G10ChildcareExpensesPage]
    }
  } section("integration",models.domain.Employed.id)
}