package controllers.circs.s1_start_of_process

import utils.WithApplication
import org.specs2.mutable._
import utils.{WithJsBrowser, LightFakeApplication, WithBrowser}
import controllers.CircumstancesScenarioFactory
import utils.pageobjects.circumstances.s1_start_of_process.{G1ReportChangesPage, G2ReportAChangeInYourCircumstancesPage}
import utils.pageobjects.circumstances.s2_report_changes.{G7BreaksInCarePage, G3PermanentlyStoppedCaringPage, G2SelfEmploymentPage, G4OtherChangeInfoPage}
import utils.pageobjects.{PageObjects, TestData}


class G2ReportAChangeInYourCircumstancesIntegrationSpec extends Specification {

  "About You" should {
    val fullName = "Mr John Joe Smith"
    val nino = "ab123456c"
    val dateOfBirth = "05/12/1990"
    val theirFullName = "Mrs Jane Smith"
    val theirRelationshipToYou = "Wife"

    val byPost = "01254 123456"
    val wantsEmailContact = "No"


    "be presented" in new WithBrowser with PageObjects {
      val page = G2ReportAChangeInYourCircumstancesPage(context)
      page goToThePage()
    }

    "navigate to previous page" in new WithBrowser with PageObjects{
      val page =  G1ReportChangesPage(context)
      val claim = CircumstancesScenarioFactory.reportChangesStoppedCaring
      page goToThePage()
      page fillPageWith claim

      val reportAChange = page submitPage()

      reportAChange must beAnInstanceOf[G2ReportAChangeInYourCircumstancesPage]

      reportAChange.goBack() must beAnInstanceOf[G1ReportChangesPage]
    }

    "present errors if mandatory fields are not populated" in new WithBrowser with PageObjects {
      val page = G2ReportAChangeInYourCircumstancesPage(context)
      page goToThePage()
      page.submitPage().listErrors.size mustEqual 6
    }

    "Accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects {
      val page = G2ReportAChangeInYourCircumstancesPage(context)
      val claim = CircumstancesScenarioFactory.aboutDetails
      page goToThePage()
      page fillPageWith claim

      page submitPage()
    }

    "contain errors on invalid submission" in new WithApplication {
      "missing fullName field" in new WithBrowser with PageObjects {
        val page = G2ReportAChangeInYourCircumstancesPage(context)
        val claim = new TestData
        claim.CircumstancesAboutYouNationalInsuranceNumber = nino
        claim.CircumstancesAboutYouDateOfBirth = dateOfBirth
        claim.CircumstancesAboutYouTheirFullName = theirFullName
        claim.CircumstancesAboutYouTheirRelationshipToYou = theirRelationshipToYou
        claim.FurtherInfoContact = "01254 785678"
        claim.CircumstancesDeclarationWantsEmailContact = "no"


        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Full name - You must complete this section")
      }

      "missing nino field" in new WithBrowser with PageObjects {
        val page = G2ReportAChangeInYourCircumstancesPage(context)
        val claim = new TestData
        claim.CircumstancesAboutYouFullName = fullName
        claim.CircumstancesAboutYouDateOfBirth = dateOfBirth
        claim.CircumstancesAboutYouTheirFullName = theirFullName
        claim.CircumstancesAboutYouTheirRelationshipToYou = theirRelationshipToYou
        claim.FurtherInfoContact = "01254 785678"
        claim.CircumstancesDeclarationWantsEmailContact = "no"

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 2
        errors(0) must contain("National Insurance number - You must complete this section")
      }

      "invalid nino containing numbers" in new WithBrowser with PageObjects {
        val page = G2ReportAChangeInYourCircumstancesPage(context)
        val claim = new TestData
        claim.CircumstancesAboutYouFullName = fullName
        claim.CircumstancesAboutYouNationalInsuranceNumber = "11abcdef1"
        claim.CircumstancesAboutYouDateOfBirth = dateOfBirth
        claim.CircumstancesAboutYouTheirFullName = theirFullName
        claim.CircumstancesAboutYouTheirRelationshipToYou = theirRelationshipToYou
        claim.FurtherInfoContact = "01254 785678"
        claim.CircumstancesDeclarationWantsEmailContact = "no"

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("National Insurance number - A National insurance number must be in the format VO123456D")
      }

      "missing dateOfBirth field" in new WithBrowser with PageObjects {
        val page = G2ReportAChangeInYourCircumstancesPage(context)
        val claim = new TestData
        claim.CircumstancesAboutYouFullName = fullName
        claim.CircumstancesAboutYouNationalInsuranceNumber = nino
        claim.CircumstancesAboutYouTheirFullName = theirFullName
        claim.CircumstancesAboutYouTheirRelationshipToYou = theirRelationshipToYou
        claim.FurtherInfoContact = "01254 785678"
        claim.CircumstancesDeclarationWantsEmailContact = "no"

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Date of birth - You must complete this section")
      }

      "missing theirFullName field" in new WithBrowser with PageObjects {
        val page = G2ReportAChangeInYourCircumstancesPage(context)
        val claim = new TestData
        claim.CircumstancesAboutYouFullName = fullName
        claim.CircumstancesAboutYouNationalInsuranceNumber = nino
        claim.CircumstancesAboutYouDateOfBirth = dateOfBirth
        claim.CircumstancesAboutYouTheirRelationshipToYou = theirRelationshipToYou
        claim.FurtherInfoContact = "01254 785678"
        claim.CircumstancesDeclarationWantsEmailContact = "no"

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Their full name - You must complete this section")
      }

      "missing fullName field" in new WithBrowser with PageObjects {
        val page = G2ReportAChangeInYourCircumstancesPage(context)
        val claim = new TestData
        claim.CircumstancesAboutYouFullName = fullName
        claim.CircumstancesAboutYouNationalInsuranceNumber = nino
        claim.CircumstancesAboutYouDateOfBirth = dateOfBirth
        claim.CircumstancesAboutYouTheirFullName = theirFullName
        claim.FurtherInfoContact = "01254 785678"
        claim.CircumstancesDeclarationWantsEmailContact = "no"

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Their relationship to you - You must complete this section")
      }

      "navigate to next page when addition info selected" in new WithBrowser with PageObjects{
        val page =  G1ReportChangesPage(context)
        val claim = CircumstancesScenarioFactory.reportChangesOtherChangeInfo
        page goToThePage()
        page fillPageWith claim

        val nextPage = page submitPage ()
        nextPage must beAnInstanceOf[G2ReportAChangeInYourCircumstancesPage]
        nextPage fillPageWith claim
        val lastPage = nextPage submitPage ()
        lastPage must beAnInstanceOf[G4OtherChangeInfoPage]
      }

      "navigate to next page when self employment selected" in new WithBrowser(app = LightFakeApplication(additionalConfiguration = Map("circs.employment.active" -> "false"))) with PageObjects{
        val page =  G1ReportChangesPage(context)
        val claim = CircumstancesScenarioFactory.reportChangesSelfEmployment
        page goToThePage()
        page fillPageWith claim

        val nextPage = page submitPage ()
        nextPage must beAnInstanceOf[G2ReportAChangeInYourCircumstancesPage]
        nextPage fillPageWith claim
        val lastPage = nextPage submitPage ()
        lastPage must beAnInstanceOf[G2SelfEmploymentPage]
      }

      "navigate to next page when stopped caring selected" in new WithBrowser with PageObjects{
        val page =  G1ReportChangesPage(context)
        val claim = CircumstancesScenarioFactory.reportChangesStoppedCaring
        page goToThePage()
        page fillPageWith claim

        val nextPage = page submitPage ()
        nextPage must beAnInstanceOf[G2ReportAChangeInYourCircumstancesPage]
        nextPage fillPageWith claim
        val lastPage = nextPage submitPage ()
        lastPage must beAnInstanceOf[G3PermanentlyStoppedCaringPage]
      }

      "navigate to next page when break from caring selected" in new WithBrowser with PageObjects{
        val page =  G1ReportChangesPage(context)
        val claim = CircumstancesScenarioFactory.reportBreakFromCaring
        page goToThePage()
        page fillPageWith claim

        val nextPage = page submitPage ()
        nextPage must beAnInstanceOf[G2ReportAChangeInYourCircumstancesPage]
        nextPage fillPageWith claim
        val lastPage = nextPage submitPage ()
        lastPage must beAnInstanceOf[G7BreaksInCarePage]
      }

      "valid submission if 'Contact number' not filled in" in new WithBrowser with PageObjects{
        val page =  G2ReportAChangeInYourCircumstancesPage(context)
        val claim = CircumstancesScenarioFactory.reportChangesOtherChangeInfo
        claim.FurtherInfoContact = ""
        page goToThePage()
        page fillPageWith claim

        val nextPage = page submitPage()
        nextPage must beAnInstanceOf[G4OtherChangeInfoPage]
      }

      "valid submission if 'Contact number' is filled in with number" in new WithBrowser with PageObjects{
        val page =  G2ReportAChangeInYourCircumstancesPage(context)
        val claim = CircumstancesScenarioFactory.reportChangesOtherChangeInfo
        page goToThePage()
        page fillPageWith claim

        val nextPage = page submitPage()
        nextPage must beAnInstanceOf[G4OtherChangeInfoPage]
      }

      "contain error if 'Contact number' is filled in with text" in new WithBrowser with PageObjects{
        val page =  G2ReportAChangeInYourCircumstancesPage(context)
        val claim = CircumstancesScenarioFactory.reportBreakFromCaring
        claim.FurtherInfoContact = "hjhjsddh"
        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Contact number - Invalid value")
      }

      "contain error if 'Contact number' is field length less than min length" in new WithBrowser with PageObjects{
        val page =  G2ReportAChangeInYourCircumstancesPage(context)
        val claim = CircumstancesScenarioFactory.reportBreakFromCaring
        claim.FurtherInfoContact = "012345"
        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Contact number - Invalid value")
      }

      "contain error if 'Contact number' is field length greater than max length" in new WithBrowser with PageObjects{
        val page =  G2ReportAChangeInYourCircumstancesPage(context)
        val claim = CircumstancesScenarioFactory.reportBreakFromCaring
        claim.FurtherInfoContact = "012345678901234567890"
        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Contact number - Invalid value")
      }

    }
  }
  section("integration", models.domain.CircumstancesIdentification.id)

}
