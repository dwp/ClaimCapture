package controllers.s_employment

import controllers.ClaimScenarioFactory
import org.specs2.mutable._
import utils.WithJsBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.s_employment.GJobDetailsPage

class GJobDetailsErrorSpec extends Specification {
  section("integration", models.domain.AboutYou.id)
  "Contact Details" should {
    "contain 5 errors on invalid blank submission" in new WithJsBrowser with PageObjects {
      val page = GJobDetailsPage(context)
      page goToThePage()
      val submittedPage = page submitPage()
      submittedPage must beAnInstanceOf[GJobDetailsPage]
      submittedPage.listErrors.size mustEqual 5
    }

    "contain complete-address error if address not filled in" in new WithJsBrowser with PageObjects {
      val page = GJobDetailsPage(context)
      val claim = ClaimScenarioFactory.s7MandatoryJobDetails
      claim.EmploymentEmployerAddress_1 = ""
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter your employer’s address. You must complete the first two lines")
    }

    "contain complete-invalid-address error if address has bad chars line1 and empty line2" in new WithJsBrowser with PageObjects {
      val page = GJobDetailsPage(context)
      val claim = ClaimScenarioFactory.s7MandatoryJobDetails
      claim.EmploymentEmployerAddress_1 = "Dollar bad char $ in line1&"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter your employer’s address. You must complete the first two lines and you must only use letters and numbers")
    }

    "contain invalid-address error if address has bad char line1 and line2" in new WithJsBrowser with PageObjects {
      val page = GJobDetailsPage(context)
      val claim = ClaimScenarioFactory.s7MandatoryJobDetails
      claim.EmploymentEmployerAddress_1 = "Dollar bad char $ in line1&Dollar bad char $ in line2"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter a valid address for your employer, only using letters and numbers")
    }

    "contain invalid-address error if address has bad char line3" in new WithJsBrowser with PageObjects {
      val page = GJobDetailsPage(context)
      val claim = ClaimScenarioFactory.s7MandatoryJobDetails
      claim.EmploymentEmployerAddress_1 = "Good line1&Good line2&Bad $ line3"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter a valid address for your employer, only using letters and numbers")
    }
  }
  section("integration", models.domain.AboutYou.id)
}
