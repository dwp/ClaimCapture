package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.{PageObjects, TestData}
import utils.pageobjects.s1_carers_allowance._

class G4LivesInGBIntegrationSpec extends Specification with Tags {
  "Carer's Allowance - Benefits - Integration" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G4LivesInGBPage(context)
      page goToThePage ()
    }

    "contain errors on invalid submission" in {
      "missing mandatory field" in new WithBrowser with PageObjects{
			val page =  G4LivesInGBPage(context)
        val claim = new TestData
        page goToThePage()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 1
      }
    }
    
    "accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects{
			val page =  G4LivesInGBPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "yes"
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects{
			val page =  G4LivesInGBPage(context)
      val claim = new TestData
      claim.CanYouGetCarersAllowanceDoYouNormallyLiveinGb = "yes"
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must beAnInstanceOf[G6ApprovePage]
    }


  } section("integration", models.domain.CarersAllowance.id)
}