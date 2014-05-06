package controllers.circs.s3_consent_and_declaration

import play.api.test.WithBrowser
import controllers.CircumstancesScenarioFactory
import org.specs2.mutable.{Tags, Specification}
import utils.pageobjects.circumstances.s3_consent_and_declaration.G1DeclarationPage
import utils.pageobjects.{PageObjects, TestData}
import utils.pageobjects.circumstances.s2_report_changes.G4OtherChangeInfoPage
import utils.pageobjects.circumstances.s1_about_you.G1ReportAChangeInYourCircumstancesPage

class G1DeclarationIntegrationSpec extends Specification with Tags {

  "Declaration" should {
    val byPost = "By Post"
    val obtainInfoAgreement = "no"
    val obtainInfoWhy = "Cause I want"
    val confirm = "yes"
    val someOneElse = "Yes"

    "be presented" in new WithBrowser with PageObjects{
			val page =  G1DeclarationPage(context)
      page goToThePage()
    }

    "navigate to previous page" in new WithBrowser with PageObjects{
			val page =  G4OtherChangeInfoPage(context)
      page goToThePage()

      val claim = CircumstancesScenarioFactory.declaration
      val otherChangeInfoPage = page runClaimWith (claim, G1DeclarationPage.title)

      otherChangeInfoPage must beAnInstanceOf[G1DeclarationPage]

      val prevPage = otherChangeInfoPage.goBack()

      prevPage must beAnInstanceOf[G4OtherChangeInfoPage]
    }

    "navigate to next page" in new WithBrowser with PageObjects{
			val page =  G1DeclarationPage(context)

      val claim = CircumstancesScenarioFactory.otherChangeInfo
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[G1DeclarationPage]
    }

    "contain errors on invalid submission" in {
      "missing furtherInfoContact field" in new WithBrowser with PageObjects{
        val page =  G1DeclarationPage(context)
        val claim = new TestData

        claim.CircumstancesDeclarationInfoAgreement = obtainInfoAgreement
        claim.CircumstancesDeclarationWhy = obtainInfoWhy
        claim.CircumstancesDeclarationConfirmation = confirm

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Contact phone or mobile number - This field is required")
      }

      "missing obtainInfoAgreement field" in new WithBrowser with PageObjects{
        val page =  G1DeclarationPage(context)
        val claim = new TestData

        claim.FurtherInfoContact = byPost
        claim.CircumstancesDeclarationWhy = obtainInfoWhy
        claim.CircumstancesDeclarationConfirmation = confirm

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Do you agree to us obtaining information from any other persons or organisations you may have told us about? - This field is required")
      }

      "given obtainInfoAgreement is set to 'no' missing obtainInfoWhy field" in new WithBrowser with PageObjects{
			val page =  G1DeclarationPage(context)
        val claim = new TestData
        claim.FurtherInfoContact = byPost
        claim.CircumstancesDeclarationInfoAgreement = obtainInfoAgreement
        claim.CircumstancesDeclarationConfirmation = confirm

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Please tell us why not - This field is required")
      }

      "given circsSomeOneElse checked and missing name or organisation field" in new WithBrowser with PageObjects{
        val page =  G1DeclarationPage(context)
        val claim = new TestData
        claim.FurtherInfoContact = byPost
        claim.CircumstancesDeclarationInfoAgreement = obtainInfoAgreement
        claim.CircumstancesDeclarationWhyNot = obtainInfoWhy
        claim.CircumstancesDeclarationConfirmation = confirm
        claim.CircumstancesSomeOneElseConfirmation = someOneElse

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Your name and/or organisation - This field is required")
      }

      "missing confirm field" in new WithBrowser with PageObjects{
			val page =  G1DeclarationPage(context)
        val claim = new TestData
        claim.FurtherInfoContact = byPost
        claim.CircumstancesDeclarationInfoAgreement = obtainInfoAgreement
        claim.CircumstancesDeclarationWhy = obtainInfoWhy

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Please tick this box to confirm that you understand and make the declarations above. - This field is required")
      }

      "not have name or organisation field with optional text" in new WithBrowser with PageObjects{
        val page =  G1DeclarationPage(context)
        val claim = new TestData
        claim.CircumstancesSomeOneElseConfirmation = someOneElse

        page goToThePage()
        page fillPageWith claim

        page.readLabel("nameOrOrganisation") mustEqual("Your name and/or organisation")
      }

      "page contains JS enabled check" in new WithBrowser with PageObjects {
        val page = G1DeclarationPage(context)
        page goToThePage()
        page jsCheckEnabled() must beTrue
      }


    }
  } section("integration", models.domain.CircumstancesConsentAndDeclaration.id)

}
