package controllers.circs.s3_consent_and_declaration

import utils.WithJsBrowser
import controllers.CircumstancesScenarioFactory
import org.specs2.mutable.{Tags, Specification}
import utils.pageobjects.circumstances.s3_consent_and_declaration.G1DeclarationPage
import utils.pageobjects.{PageObjects, TestData}
import utils.pageobjects.circumstances.s2_report_changes.G4OtherChangeInfoPage
import utils.pageobjects.circumstances.s1_start_of_process.G2ReportAChangeInYourCircumstancesPage$


class G1DeclarationIntegrationSpec extends Specification with Tags {

  "Declaration" should {
    val obtainInfoAgreement = "no"
    val obtainInfoWhy = "Cause I want"
    val someOneElse = "Yes"

    "be presented" in new WithJsBrowser  with PageObjects{
			val page =  G1DeclarationPage(context)
      page goToThePage()
    }

    "navigate to previous page" in new WithJsBrowser  with PageObjects{
			val page =  G4OtherChangeInfoPage(context)
      page goToThePage()

      val claim = CircumstancesScenarioFactory.declaration
      val otherChangeInfoPage = page runClaimWith (claim, G1DeclarationPage.url)

      otherChangeInfoPage must beAnInstanceOf[G1DeclarationPage]

      val prevPage = otherChangeInfoPage.goBack()

      prevPage must beAnInstanceOf[G4OtherChangeInfoPage]
    }

    "navigate to next page" in new WithJsBrowser  with PageObjects{
			val page =  G1DeclarationPage(context)

      val claim = CircumstancesScenarioFactory.otherChangeInfo
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[G1DeclarationPage]
    }

    "contain errors on invalid submission" in {
//      "missing furtherInfoContact field" in new WithJsBrowser  with PageObjects{
//        val page =  G1DeclarationPage(context)
//        val claim = new TestData
//
//        claim.CircumstancesDeclarationInfoAgreement = obtainInfoAgreement
//        claim.CircumstancesDeclarationWhy = obtainInfoWhy
//
//        page goToThePage()
//        page fillPageWith claim
//
//        val errors = page.submitPage().listErrors
//        errors.size mustEqual 1
//        errors(0) must contain("Contact phone or mobile number - You must complete this section")
//      }

      "missing obtainInfoAgreement field" in new WithJsBrowser  with PageObjects{
        val page =  G1DeclarationPage(context)
        val claim = new TestData

        claim.CircumstancesDeclarationWhy = obtainInfoWhy

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Do you agree to the Carer's Allowance Unit contacting anyone mentioned in this form? - You must complete this section")
      }

      "given obtainInfoAgreement is set to 'no' missing obtainInfoWhy field" in new WithJsBrowser  with PageObjects{
			val page =  G1DeclarationPage(context)
        val claim = new TestData
        claim.CircumstancesDeclarationInfoAgreement = obtainInfoAgreement

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("List anyone you don't want to be contacted and say why. - You must complete this section")
      }

      "given circsSomeOneElse checked and missing name or organisation field" in new WithJsBrowser  with PageObjects{
        val page =  G1DeclarationPage(context)
        val claim = new TestData
        claim.CircumstancesDeclarationInfoAgreement = obtainInfoAgreement
        claim.CircumstancesDeclarationWhyNot = obtainInfoWhy
        claim.CircumstancesSomeOneElseConfirmation = someOneElse

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
        errors.size mustEqual 1
        errors(0) must contain("Your name and/or organisation - You must complete this section")
      }
//
//      "missing wants email contact field" in new WithJsBrowser  with PageObjects{
//        val page =  G1DeclarationPage(context)
//        val claim = new TestData
//        claim.CircumstancesDeclarationInfoAgreement = obtainInfoAgreement
//        claim.CircumstancesDeclarationWhy = obtainInfoWhy
//
//        page goToThePage()
//        page fillPageWith claim
//
//        val errors = page.submitPage().listErrors
//        errors.size mustEqual 2
//
//      }

      "not have name or organisation field with optional text" in new WithJsBrowser  with PageObjects{
        val page =  G1DeclarationPage(context)
        val claim = new TestData
        claim.CircumstancesSomeOneElseConfirmation = someOneElse

        page goToThePage()
        page fillPageWith claim

        page.readLabel("nameOrOrganisation") mustEqual("Your name and/or organisation")
      }

      "page contains JS enabled check" in new WithJsBrowser  with PageObjects {
        val page = G1DeclarationPage(context)
        page goToThePage()
        page.jsCheckEnabled must beTrue
      }

/*      "'Please List anyone you don't want to be contacted and say why.' field should not be visible when answered 'yes' to obtainInfoAgreement" in new WithJsBrowser  with PageObjects{
        val page =  G1DeclarationPage(context)
        val claim = new TestData
        claim.CircumstancesDeclarationInfoAgreement = "yes"
        claim.CircumstancesDeclarationWantsEmailContact = wantsEmailContact

        page goToThePage()
        page fillPageWith claim

        val errors = page.submitPage().listErrors
//        errors.size mustEqual 1
//        errors(0) must contain("Contact phone or mobile number - You must complete this section")
        page.ctx.browser.findFirst("#obtainInfoWhy").isDisplayed should beFalse
      }
*/
    }
  } section("integration", models.domain.CircumstancesConsentAndDeclaration.id)

}
