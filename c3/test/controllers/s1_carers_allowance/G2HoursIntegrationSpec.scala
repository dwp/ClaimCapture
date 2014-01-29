package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.{PageObjects, TestData}
import utils.pageobjects.s1_carers_allowance._

class G2HoursIntegrationSpec extends Specification with Tags {
  "Carer's Allowance - Benefits - Integration" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G2HoursPage(context)
      page goToThePage ()
    }

    "contain errors on invalid submission" in {
      "missing mandatory field" in new WithBrowser with PageObjects{
			val page =  G2HoursPage(context)
        val claim = new TestData
        claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = ""
        page goToThePage()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 1
      }
    }
    
    "accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects{
			val page =  G2HoursPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "yes"
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }
    
    "navigate to next page on valid submission" in new WithBrowser with PageObjects{
			val page =  G2HoursPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceDoYouSpend35HoursorMoreEachWeekCaring = "yes"
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G3Over16Page]
    }
    
    "contain the completed forms" in new WithBrowser with PageObjects{
			val page =  G1BenefitsPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceDoesthePersonYouCareforGetOneofTheseBenefits = "yes"
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage() 

      nextPage match {
        case p: G2HoursPage => {
          p numberSectionsCompleted() mustEqual 1
          val completed = p.listCompletedForms
          completed(0) must contain("Yes")
        }
        case _ => ko("Next Page is not of the right type.")
      }
    }
  } section("integration", models.domain.CarersAllowance.id)
}