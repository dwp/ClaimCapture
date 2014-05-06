package controllers.circs.s1_identification

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.circumstances.s1_about_you.G1ReportAChangeInYourCircumstancesPage
import controllers.CircumstancesScenarioFactory
import utils.pageobjects.{PageObjects, TestData}


class G1ReportAChangeInYourCircumstancesIntegrationSpec extends Specification with Tags {

  "About You" should {
    val fullName = "Mr John Joe Smith"
    val nino = "ab123456c"
    val dateOfBirth = "05/12/1990"
    val theirFullName = "Mrs Jane Smith"
    val theirRelationshipToYou = "Wife"

    "be presented" in new WithBrowser with PageObjects {
      val page = G1ReportAChangeInYourCircumstancesPage(context)
      page goToThePage()
    }

    "present errors if mandatory fields are not populated" in new WithBrowser with PageObjects {
      val page = G1ReportAChangeInYourCircumstancesPage(context)
      page goToThePage()
      page.submitPage().listErrors.size mustEqual 6
    }

    "Accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects {
      val page = G1ReportAChangeInYourCircumstancesPage(context)
      val claim = CircumstancesScenarioFactory.aboutDetails
      page goToThePage()
      page fillPageWith claim

      page submitPage()
    }

    "contain errors on invalid submission" in {
      "missing fullName field" in new WithBrowser with PageObjects {
        val page = G1ReportAChangeInYourCircumstancesPage(context)
        val claim = new TestData
        claim.CircumstancesAboutYouNationalInsuranceNumber = nino
        claim.CircumstancesAboutYouDateOfBirth = dateOfBirth
        claim.CircumstancesAboutYouTheirFullName = theirFullName
        claim.CircumstancesAboutYouTheirRelationshipToYou = theirRelationshipToYou

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Full name - This field is required")
      }

      "missing nino field" in new WithBrowser with PageObjects {
        val page = G1ReportAChangeInYourCircumstancesPage(context)
        val claim = new TestData
        claim.CircumstancesAboutYouFullName = fullName
        claim.CircumstancesAboutYouDateOfBirth = dateOfBirth
        claim.CircumstancesAboutYouTheirFullName = theirFullName
        claim.CircumstancesAboutYouTheirRelationshipToYou = theirRelationshipToYou

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 2
        errors(0) must contain("National Insurance number - This field is required")
      }

      "invalid nino containing numbers" in new WithBrowser with PageObjects {
        val page = G1ReportAChangeInYourCircumstancesPage(context)
        val claim = new TestData
        claim.CircumstancesAboutYouFullName = fullName
        claim.CircumstancesAboutYouNationalInsuranceNumber = "11abcdef1"
        claim.CircumstancesAboutYouDateOfBirth = dateOfBirth
        claim.CircumstancesAboutYouTheirFullName = theirFullName
        claim.CircumstancesAboutYouTheirRelationshipToYou = theirRelationshipToYou

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("National Insurance number - A National insurance number must be in the format VO 12 34 56 D")
      }

      "missing dateOfBirth field" in new WithBrowser with PageObjects {
        val page = G1ReportAChangeInYourCircumstancesPage(context)
        val claim = new TestData
        claim.CircumstancesAboutYouFullName = fullName
        claim.CircumstancesAboutYouNationalInsuranceNumber = nino
        claim.CircumstancesAboutYouTheirFullName = theirFullName
        claim.CircumstancesAboutYouTheirRelationshipToYou = theirRelationshipToYou

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Date of birth - This field is required")
      }

      "missing theirFullName field" in new WithBrowser with PageObjects {
        val page = G1ReportAChangeInYourCircumstancesPage(context)
        val claim = new TestData
        claim.CircumstancesAboutYouFullName = fullName
        claim.CircumstancesAboutYouNationalInsuranceNumber = nino
        claim.CircumstancesAboutYouDateOfBirth = dateOfBirth
        claim.CircumstancesAboutYouTheirRelationshipToYou = theirRelationshipToYou

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Their full name - This field is required")
      }

      "missing fullName field" in new WithBrowser with PageObjects {
        val page = G1ReportAChangeInYourCircumstancesPage(context)
        val claim = new TestData
        claim.CircumstancesAboutYouFullName = fullName
        claim.CircumstancesAboutYouNationalInsuranceNumber = nino
        claim.CircumstancesAboutYouDateOfBirth = dateOfBirth
        claim.CircumstancesAboutYouTheirFullName = theirFullName

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Their relationship to you - This field is required")
      }

      "page contains JS enabled check" in new WithBrowser with PageObjects {
        val page = G1ReportAChangeInYourCircumstancesPage(context)
        page goToThePage()
        page jsCheckEnabled() must beTrue
      }

    }
  } section("integration", models.domain.CircumstancesIdentification.id)

}
