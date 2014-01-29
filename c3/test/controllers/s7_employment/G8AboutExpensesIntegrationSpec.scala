package controllers.s7_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.ClaimScenarioFactory
import utils.pageobjects.s7_employment._
import utils.pageobjects.{PageObjects, TestData}

class G8AboutExpensesIntegrationSpec extends Specification with Tags {
  "About expenses" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G8AboutExpensesPage(context)
      page goToThePage()
    }

    "contain 1 completed form" in new WithBrowser with PageObjects{
			val page =  G8AboutExpensesPage(context)
      val claim = ClaimScenarioFactory s7Employment()
      page goToThePage()
      page fillPageWith claim
      page submitPage() match {
        case p: G9NecessaryExpensesPage => p numberSectionsCompleted()  mustEqual 1
        case _ => ko("Next Page is not of the right type.")
      }
    }

    "go to job completion directly" in new WithBrowser with PageObjects{
			val page =  G8AboutExpensesPage(context)
      val claim = new TestData
      claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1= "no"
      claim.EmploymentDoYouPayAnyoneLookAfterYourChild_1= "no"
      claim.EmploymentDoYouPayAnyonetoLookAfterPersonYouCareFor_1= "no"

      page goToThePage()
      page fillPageWith claim
      page submitPage() must beAnInstanceOf[G14JobCompletionPage]
    }

    "go to childcare" in new WithBrowser with PageObjects{
			val page =  G8AboutExpensesPage(context)
      val claim = new TestData
      claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1= "no"
      claim.EmploymentDoYouPayAnyoneLookAfterYourChild_1= "yes"
      claim.EmploymentDoYouPayAnyonetoLookAfterPersonYouCareFor_1= "yes"

      page goToThePage()
      page fillPageWith claim
      page submitPage() must beAnInstanceOf[G10ChildcareExpensesPage]
    }

    "go to care" in new WithBrowser with PageObjects{
			val page =  G8AboutExpensesPage(context)
      val claim = new TestData
      claim.EmploymentDoYouPayforAnythingNecessaryToDoYourJob_1= "no"
      claim.EmploymentDoYouPayAnyoneLookAfterYourChild_1= "no"
      claim.EmploymentDoYouPayAnyonetoLookAfterPersonYouCareFor_1= "yes"

      page goToThePage()
      page fillPageWith claim
      page submitPage() must beAnInstanceOf[G12PersonYouCareForExpensesPage]
    }

    "be able to navigate back to a completed form" in new WithBrowser  with PageObjects{
			val page =  G7PensionSchemesPage(context)
      val claim = ClaimScenarioFactory s7Employment()
      page goToThePage()
      page fillPageWith claim
      val submitted = page submitPage()
      val backPage = submitted goBack ()
      backPage must beAnInstanceOf[G7PensionSchemesPage]
    }
  } section("integration",models.domain.Employed.id)
}