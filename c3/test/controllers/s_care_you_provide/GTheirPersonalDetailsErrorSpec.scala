package controllers.s_care_you_provide

import controllers.ClaimScenarioFactory
import org.specs2.mutable._
import utils.WithJsBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.s_care_you_provide.GTheirPersonalDetailsPage

class GTheirPersonalDetailsErrorSpec extends Specification {
  section("integration", models.domain.AboutYou.id)
  "Contact Details" should {
    "contain 6 errors on invalid blank submission" in new WithJsBrowser with PageObjects {
      val theirDetailsPage = GTheirPersonalDetailsPage(context)
      theirDetailsPage goToThePage()
      val submittedPage = theirDetailsPage submitPage()
      submittedPage must beAnInstanceOf[GTheirPersonalDetailsPage]
      submittedPage.listErrors.size mustEqual 6
    }

    "contain complete-address error if address not filled in" in new WithJsBrowser with PageObjects {
      val page = GTheirPersonalDetailsPage(context)
      val claim = ClaimScenarioFactory.s4CareYouProvide(hours35 = false)
      claim.AboutTheCareYouProvideAddressPersonCareFor = ""
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter the address of the person you care for. You must complete the first two lines")
    }

    "contain complete-invalid-address error if address has bad chars line1 and empty line2" in new WithJsBrowser with PageObjects {
      val page = GTheirPersonalDetailsPage(context)
      val claim = ClaimScenarioFactory.s4CareYouProvide(hours35 = false)
      claim.AboutTheCareYouProvideAddressPersonCareFor = "Dollar bad char $ in line1&"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter the address of the person you care for. You must complete the first two lines and you must only use letters and numbers")
    }

    "contain invalid-address error if address has bad char line1 and line2" in new WithJsBrowser with PageObjects {
      val page = GTheirPersonalDetailsPage(context)
      val claim = ClaimScenarioFactory.s4CareYouProvide(hours35 = false)
      claim.AboutTheCareYouProvideAddressPersonCareFor = "Dollar bad char $ in line1&Dollar bad char $ in line2"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter a valid address for the person you care for, only using letters and numbers")
    }

    "contain invalid-address error if address has bad char line3" in new WithJsBrowser with PageObjects {
      val page = GTheirPersonalDetailsPage(context)
      val claim = ClaimScenarioFactory.s4CareYouProvide(hours35 = false)
      claim.AboutTheCareYouProvideAddressPersonCareFor = "Good line1&Good line2&Bad $ line3"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter a valid address for the person you care for, only using letters and numbers")
    }
  }
  section("integration", models.domain.AboutYou.id)
}
