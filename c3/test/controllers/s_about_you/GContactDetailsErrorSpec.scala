package controllers.s_about_you

import controllers.{ClaimScenarioFactory, PreviewTestUtils}
import org.specs2.mutable._
import utils.helpers.PreviewField._
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_about_you._
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.{PageObjects, TestData, _}
import utils.{LightFakeApplication, WithJsBrowser}

class GContactDetailsErrorSpec extends Specification {
  section("integration", models.domain.AboutYou.id)
  "Contact Details" should {
    "contain complete-address error if address not filled in" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      claim.AboutYouAddress = ""
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter your address. You must complete the first two lines")
    }

    "contain complete-invalid-address error if address has bad chars line1 and empty line2" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      claim.AboutYouAddress = "Dollar bad char $ in line1&"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter your address. You must complete the first two lines and you must only use letters and numbers")
    }

    "contain invalid-address error if address has bad char line1 and line2" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      claim.AboutYouAddress = "Dollar bad char $ in line1&Dollar bad char $ in line2"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter a valid address for you, only using letters and numbers")
    }

    "contain invalid-address error if address has bad char line3" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      claim.AboutYouAddress = "Good line1&Good line2&Bad $ line3"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Enter a valid address for you, only using letters and numbers")
    }

    "contain error if 'Contact number' is filled in with text" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      claim.HowWeContactYou = "I do not have contact number"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Contact number - Invalid value")
    }

    "contain error if 'Contact number' is field length less than min length" in new WithJsBrowser with PageObjects {
      val page = GContactDetailsPage(context)
      val claim = ClaimScenarioFactory.yourDetailsWithNotTimeOutside()
      claim.HowWeContactYou = "012345"
      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Contact number - Invalid value")
    }
  }
  section("integration", models.domain.AboutYou.id)
}
