package controllers.circs.employment_change

import controllers.CircumstancesScenarioFactory
import org.specs2.mutable._
import utils.{WithJsBrowser, WithBrowser}
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.origin.GOriginPage
import utils.pageobjects.circumstances.report_changes.{GAddressChangePage, GEmploymentChangePage}

class GEmploymentChangeErrorSpec extends Specification {
  section("integration", models.domain.CircumstancesIdentification.id)
  "Report a change in your circumstance - Employment" should {
    "contain complete-address error if address not filled in" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesEmploymentChangeEmploymentPresent
      claim.CircumstancesEmploymentChangeEmployerNameAndAddress = ""
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter your employer’s address. You must complete the first two lines")
    }

    "contain complete-invalid-address error if address has bad chars line1 and empty line2" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesEmploymentChangeEmploymentPresent
      claim.CircumstancesEmploymentChangeEmployerNameAndAddress = "Dollar bad char $ in line1&"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter your employer’s address. You must complete the first two lines and you must only use letters and numbers")
    }

    "contain invalid-address error if address has bad char line1 and line2" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesEmploymentChangeEmploymentPresent
      claim.CircumstancesEmploymentChangeEmployerNameAndAddress = "Dollar bad char $ in line1&Dollar bad char $ in line2"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter a valid address for your employer, only using letters and numbers")
    }

    "contain invalid-address error if address has bad char line3" in new WithJsBrowser with PageObjects {
      val page = GEmploymentChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesEmploymentChangeEmploymentPresent
      claim.CircumstancesEmploymentChangeEmployerNameAndAddress = "Good line1&Good line2&Bad $ line3"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter a valid address for your employer, only using letters and numbers")
    }

  }
  section("integration", models.domain.CircumstancesIdentification.id)
}
