package controllers.circs.address_change

import controllers.{CircumstancesScenarioFactory}
import org.specs2.mutable._
import utils.{WithJsBrowser, WithBrowser}
import utils.pageobjects.PageObjects
import utils.pageobjects.circumstances.report_changes.GAddressChangePage

class GAddressChangeErrorSpec extends Specification {
  section("integration", models.domain.CircumstancesIdentification.id)
  "Address change" should {
    "show 3 errors on submitting without any mandatory data" in new WithBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.addressChange
      page goToThePage()
      page fillPageWith claim

      page submitPage()
      page.listErrors.size must beEqualTo(3)
    }

    "show error on submitting without mandatory 'person has changed address' when still caring" in new WithBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangeAddressMissingPersonChangedAddress
      page goToThePage()
      page fillPageWith claim

      page submitPage()
      page.listErrors.size must beEqualTo(1)
    }

    "show error on submitting without mandatory 'date stopped caring' when not still caring" in new WithBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangeAddressMissingDateStoppedCaring
      page goToThePage()
      page fillPageWith claim

      page submitPage()
      page.listErrors.size must beEqualTo(1)
    }

    "show error on submitting without mandatory 'new address' when not still caring" in new WithBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangeAddressMissingNewAddress
      page goToThePage()
      page fillPageWith claim

      page submitPage()
      page.listErrors.size must beEqualTo(1)
    }

    "show errors on submitting without mandatory 'new address' and 'date stopped caring' when not still caring" in new WithBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangeAddressMissingNewAddressAndDate

      page goToThePage()
      page fillPageWith claim

      page submitPage()
      page.listErrors.size must beEqualTo(2)
    }

    "show error on submitting without mandatory 'same address' when person has changed address" in new WithBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesAddressMissingSameAddress
      page goToThePage()
      page fillPageWith claim

      page submitPage()
      page.listErrors.size must beEqualTo(1)
    }
  }

  "Address change for carers previous address" should {
    "contain complete-address error if address not filled in" in new WithJsBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesAddressChangeYes
      claim.CircumstancesAddressChangePreviousAddress = ""
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter your previous address. You must complete the first two lines")
    }

    "contain complete-invalid-address error if address has bad chars line1 and empty line2" in new WithJsBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesAddressChangeYes
      claim.CircumstancesAddressChangePreviousAddress = "Dollar bad char $ in line1&"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter your previous address. You must complete the first two lines and you must only use letters and numbers")
    }

    "contain invalid-address error if address has bad char line1 and line2" in new WithJsBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesAddressChangeYes
      claim.CircumstancesAddressChangePreviousAddress = "Dollar bad char $ in line1&Dollar bad char $ in line2"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter a valid previous address for you, only using letters and numbers")
    }

    "contain invalid-address error if address has bad char line3" in new WithJsBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesAddressChangeYes
      claim.CircumstancesAddressChangePreviousAddress = "Good line1&Good line2&Bad $ line3"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter a valid previous address for you, only using letters and numbers")
    }
  }
  "Address change for carers new address" should {
    "contain complete-address error if address not filled in" in new WithJsBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesAddressChangeYes
      claim.CircumstancesAddressChangeNewAddress = ""
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter your new address. You must complete the first two lines")
    }

    "contain complete-invalid-address error if address has bad chars line1 and empty line2" in new WithJsBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesAddressChangeYes
      claim.CircumstancesAddressChangeNewAddress = "Dollar bad char $ in line1&"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter your new address. You must complete the first two lines and you must only use letters and numbers")
    }

    "contain invalid-address error if address has bad char line1 and line2" in new WithJsBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesAddressChangeYes
      claim.CircumstancesAddressChangeNewAddress = "Dollar bad char $ in line1&Dollar bad char $ in line2"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter a valid new address for you, only using letters and numbers")
    }

    "contain invalid-address error if address has bad char line3" in new WithJsBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesAddressChangeYes
      claim.CircumstancesAddressChangeNewAddress = "Good line1&Good line2&Bad $ line3"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter a valid new address for you, only using letters and numbers")
    }
  }

  "Address change for carees new address" should {
    "contain complete-address error if address not filled in" in new WithJsBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesAddressChangeYes
      claim.CircumstancesAddressChangeSameAddressTheirAddress = ""
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter the new address of the person you care for. You must complete the first two lines")
    }

    "contain complete-invalid-address error if address has bad chars line1 and empty line2" in new WithJsBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesAddressChangeYes
      claim.CircumstancesAddressChangeSameAddressTheirAddress = "Dollar bad char $ in line1&"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter the new address of the person you care for. You must complete the first two lines and you must only use letters and numbers")
    }

    "contain invalid-address error if address has bad char line1 and line2" in new WithJsBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesAddressChangeYes
      claim.CircumstancesAddressChangeSameAddressTheirAddress = "Dollar bad char $ in line1&Dollar bad char $ in line2"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter a valid new address for the person you care for, only using letters and numbers")
    }

    "contain invalid-address error if address has bad char line3" in new WithJsBrowser with PageObjects {
      val page = GAddressChangePage(context)
      val claim = CircumstancesScenarioFactory.reportChangesAddressChangeYes
      claim.CircumstancesAddressChangeSameAddressTheirAddress = "Good line1&Good line2&Bad $ line3"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter a valid new address for the person you care for, only using letters and numbers")
    }
  }
  section("integration", models.domain.CircumstancesIdentification.id)
}
