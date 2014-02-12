package controllers.circs.s1_identification

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.circumstances.s1_about_you.{G3DetailsOfThePersonYouCareForPage, G3DetailsOfThePersonYouCareForPageContext}
import controllers.CircumstancesScenarioFactory
import utils.pageobjects.{PageObjects, TestData}


class G3DetailsOfThePersonYouCareForIntegrationSpec extends Specification with Tags {

  "DetailsOfThePersonYouCareFor - Integration" should {
    val firstName = "John"
    val middelName = "Joe"
    val lastName = "Smith"
    val nino = "ab123456c"
    val dateOfBirth = "05/12/1990"

    "be presented" in new WithBrowser with PageObjects{
			val page =  G3DetailsOfThePersonYouCareForPage(context)
      page goToThePage()
    }

    "present errors if mandatory fields are not populated" in new WithBrowser with PageObjects{
			val page =  G3DetailsOfThePersonYouCareForPage(context)
      page goToThePage()

      page.submitPage().listErrors.size mustEqual 5
    }

    "Accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects{
			val page =  G3DetailsOfThePersonYouCareForPage(context)
      val claim = CircumstancesScenarioFactory.detailsOfThePersonYouCareFor
      page goToThePage()
      page fillPageWith claim

      page submitPage()
    }

    "contain errors on invalid submission" in {
      "missing firstName field" in new WithBrowser with PageObjects{
			val page =  G3DetailsOfThePersonYouCareForPage(context)
        val claim = new TestData
        claim.CircumstancesDetailsOfThePersonYouCareForMiddleName = middelName
        claim.CircumstancesDetailsOfThePersonYouCareForLastName = lastName
        claim.CircumstancesDetailsOfThePersonYouCareForNationalInsuranceNumber = nino
        claim.CircumstancesDetailsOfThePersonYouCareForDateOfBirth = dateOfBirth

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("First name - This is required")
      }

      "missing lastName field" in new WithBrowser with PageObjects{
			val page =  G3DetailsOfThePersonYouCareForPage(context)
        val claim = new TestData
        claim.CircumstancesDetailsOfThePersonYouCareForFirstName = firstName
        claim.CircumstancesDetailsOfThePersonYouCareForMiddleName = middelName
        claim.CircumstancesDetailsOfThePersonYouCareForNationalInsuranceNumber = nino
        claim.CircumstancesDetailsOfThePersonYouCareForDateOfBirth = dateOfBirth

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Last name - This is required")
      }

      "missing nino field" in new WithBrowser with PageObjects{
			val page =  G3DetailsOfThePersonYouCareForPage(context)
        val claim = new TestData
        claim.CircumstancesDetailsOfThePersonYouCareForFirstName = firstName
        claim.CircumstancesDetailsOfThePersonYouCareForMiddleName = middelName
        claim.CircumstancesDetailsOfThePersonYouCareForLastName = lastName
        claim.CircumstancesDetailsOfThePersonYouCareForDateOfBirth = dateOfBirth

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 2
        errors(0) must contain("National Insurance number - This is required")
      }

      "invalid nino containing numbers" in new WithBrowser with PageObjects{
			val page =  G3DetailsOfThePersonYouCareForPage(context)
        val claim = new TestData
        claim.CircumstancesDetailsOfThePersonYouCareForFirstName = firstName
        claim.CircumstancesDetailsOfThePersonYouCareForMiddleName = middelName
        claim.CircumstancesDetailsOfThePersonYouCareForLastName = lastName
        claim.CircumstancesDetailsOfThePersonYouCareForNationalInsuranceNumber = "11abcdef1"
        claim.CircumstancesDetailsOfThePersonYouCareForDateOfBirth = dateOfBirth

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("National Insurance number - A National insurance number must be in the format VO 12 34 56 D")
      }

      "missing dateOfBirth field" in new WithBrowser with PageObjects{
			val page =  G3DetailsOfThePersonYouCareForPage(context)
        val claim = new TestData
        claim.CircumstancesDetailsOfThePersonYouCareForFirstName = firstName
        claim.CircumstancesDetailsOfThePersonYouCareForMiddleName = middelName
        claim.CircumstancesDetailsOfThePersonYouCareForLastName = lastName
        claim.CircumstancesDetailsOfThePersonYouCareForNationalInsuranceNumber = nino

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Date of birth - This is required")
      }
    }
  } section("integration", models.domain.CircumstancesIdentification.id)

}
