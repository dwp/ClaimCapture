package controllers.circs.s1_identification

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.circumstances.s1_about_you.G1AboutYouPage
import controllers.CircumstancesScenarioFactory
import utils.pageobjects.{PageObjects, TestData}


class G1AboutYouIntegrationSpec extends Specification with Tags {

  "About You" should {
    val title = "Mr"
    val firstName = "John"
    val middelName = "Joe"
    val lastName = "Smith"
    val nino = "ab123456c"
    val dateOfBirth = "05/12/1990"

    "be presented" in new WithBrowser with PageObjects {
      val page = G1AboutYouPage(context)
      page goToThePage()
    }

    "present errors if mandatory fields are not populated" in new WithBrowser with PageObjects {
      val page = G1AboutYouPage(context)
      page goToThePage()
      page.submitPage().listErrors.size mustEqual 6
    }

    "Accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects {
      val page = G1AboutYouPage(context)
      val claim = CircumstancesScenarioFactory.aboutDetails
      page goToThePage()
      page fillPageWith claim

      page submitPage()
    }

    "contain errors on invalid submission" in {
      "missing title field" in new WithBrowser with PageObjects {
        val page = G1AboutYouPage(context)
        val claim = new TestData
        claim.CircumstancesAboutYouFirstName = firstName
        claim.CircumstancesAboutYouMiddleName = middelName
        claim.CircumstancesAboutYouLastName = lastName
        claim.CircumstancesAboutYouNationalInsuranceNumber = nino
        claim.CircumstancesAboutYouDateOfBirth = dateOfBirth

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Title - This is required")
      }

      "missing firstName field" in new WithBrowser with PageObjects {
        val page = G1AboutYouPage(context)
        val claim = new TestData
        claim.CircumstancesAboutYouTitle = title
        claim.CircumstancesAboutYouMiddleName = middelName
        claim.CircumstancesAboutYouLastName = lastName
        claim.CircumstancesAboutYouNationalInsuranceNumber = nino
        claim.CircumstancesAboutYouDateOfBirth = dateOfBirth

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("First name - This is required")
      }

      "missing lastName field" in new WithBrowser with PageObjects {
        val page = G1AboutYouPage(context)
        val claim = new TestData
        claim.CircumstancesAboutYouTitle = title
        claim.CircumstancesAboutYouFirstName = firstName
        claim.CircumstancesAboutYouMiddleName = middelName
        claim.CircumstancesAboutYouNationalInsuranceNumber = nino
        claim.CircumstancesAboutYouDateOfBirth = dateOfBirth

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Last name - This is required")
      }

      "missing nino field" in new WithBrowser with PageObjects {
        val page = G1AboutYouPage(context)
        val claim = new TestData
        claim.CircumstancesAboutYouTitle = title
        claim.CircumstancesAboutYouFirstName = firstName
        claim.CircumstancesAboutYouMiddleName = middelName
        claim.CircumstancesAboutYouLastName = lastName
        claim.CircumstancesAboutYouDateOfBirth = dateOfBirth

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 2
        errors(0) must contain("National Insurance number - This is required")
      }

      "invalid nino containing numbers" in new WithBrowser with PageObjects {
        val page = G1AboutYouPage(context)
        val claim = new TestData
        claim.CircumstancesAboutYouTitle = title
        claim.CircumstancesAboutYouFirstName = firstName
        claim.CircumstancesAboutYouMiddleName = middelName
        claim.CircumstancesAboutYouLastName = lastName
        claim.CircumstancesAboutYouNationalInsuranceNumber = "11abcdef1"
        claim.CircumstancesAboutYouDateOfBirth = dateOfBirth

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("National Insurance number - A National insurance number must be in the format VO 12 34 56 D")
      }

      "missing dateOfBirth field" in new WithBrowser with PageObjects {
        val page = G1AboutYouPage(context)
        val claim = new TestData
        claim.CircumstancesAboutYouTitle = title
        claim.CircumstancesAboutYouFirstName = firstName
        claim.CircumstancesAboutYouMiddleName = middelName
        claim.CircumstancesAboutYouLastName = lastName
        claim.CircumstancesAboutYouNationalInsuranceNumber = nino

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Date of birth - This is required")
      }
    }
  } section("integration", models.domain.CircumstancesIdentification.id)

}
