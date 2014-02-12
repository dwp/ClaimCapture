package controllers.circs.s1_identification

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.circumstances.s1_about_you.{G2YourContactDetailsPage, G2YourContactDetailsPageContext}
import controllers.CircumstancesScenarioFactory
import utils.pageobjects.{PageObjects, TestData}


class G2YourContactDetailsIntegrationSpec extends Specification with Tags {

  "Your contact details" should {
    val address = "101 Clifton Street&Blackpool"
    val postCode = "PR2 8AE"
    val phoneNumber = "01772700806"
    val mobileNumber = "444444"

    "be presented" in new WithBrowser with PageObjects{
			val page =  G2YourContactDetailsPage(context)
      page goToThePage()
    }


    "present errors if mandatory fields are not populated" in new WithBrowser with PageObjects{
			val page =  G2YourContactDetailsPage(context)
      page goToThePage()
      page.submitPage().listErrors.size mustEqual 3
    }


    "Accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects{
			val page =  G2YourContactDetailsPage(context)
      val claim = CircumstancesScenarioFactory.yourContactDetails
      page goToThePage()
      page fillPageWith claim

      page submitPage()
    }

    "contain errors on invalid submission" in {
      "missing address field" in new WithBrowser with PageObjects{
			val page =  G2YourContactDetailsPage(context)
        val claim = new TestData
        claim.CircumstancesYourContactDetailsPostcode = postCode
        claim.CircumstancesYourContactDetailsPhoneNumber = phoneNumber
        claim.CircumstancesYourContactDetailsMobileNumber = mobileNumber

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Address - This is required")
      }

      "invalid postcode field" in new WithBrowser with PageObjects{
			val page =  G2YourContactDetailsPage(context)
        val claim = new TestData
        claim.CircumstancesYourContactDetailsAddress = address
        claim.CircumstancesYourContactDetailsPostcode = "INVALID"
        claim.CircumstancesYourContactDetailsPhoneNumber = phoneNumber
        claim.CircumstancesYourContactDetailsMobileNumber = mobileNumber

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Postcode - A post code must be in the format PR2 8AE")
      }

      "invalid phoneNumber field" in new WithBrowser with PageObjects{
			val page =  G2YourContactDetailsPage(context)
        val claim = new TestData
        claim.CircumstancesYourContactDetailsAddress = address
        claim.CircumstancesYourContactDetailsPostcode = postCode
        claim.CircumstancesYourContactDetailsPhoneNumber = "INVALID"
        claim.CircumstancesYourContactDetailsMobileNumber = mobileNumber

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Phone number - Invalid value")
      }

      "invalid mobileNumber field" in new WithBrowser with PageObjects{
			val page =  G2YourContactDetailsPage(context)
        val claim = new TestData
        claim.CircumstancesYourContactDetailsAddress = address
        claim.CircumstancesYourContactDetailsPostcode = postCode
        claim.CircumstancesYourContactDetailsPhoneNumber = phoneNumber
        claim.CircumstancesYourContactDetailsMobileNumber = "INVALID"

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Mobile number - Invalid value")
      }

    }
  } section("integration", models.domain.CircumstancesYourContactDetails.id)

}
