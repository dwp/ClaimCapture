package controllers.circs.consent_and_declaration

import utils.WithJsBrowser
import controllers.CircumstancesScenarioFactory
import org.specs2.mutable._
import utils.pageobjects.circumstances.consent_and_declaration.GCircsDeclarationPage
import utils.pageobjects.circumstances.start_of_process.GCircsYourDetailsPage
import utils.pageobjects.{PageObjects, TestData}

class GCircsDeclarationIntegrationSpec extends Specification {
  section("integration", models.domain.CircumstancesConsentAndDeclaration.id)
  "Declaration" should {
    val obtainInfoAgreement = "no"
    val obtainInfoWhy = "Cause I want"
    val someOneElse = "Yes"

    "be presented" in new WithJsBrowser  with PageObjects{
			val page =  GCircsDeclarationPage(context)
      page goToThePage()
    }

    "navigate to previous page" in new WithJsBrowser  with PageObjects{
      val page=GCircsYourDetailsPage(context)
      page goToThePage()
      val claim = CircumstancesScenarioFactory.aboutDetails
      val declarePage=page runClaimWith(claim,GCircsDeclarationPage.url)
      declarePage must beAnInstanceOf[GCircsDeclarationPage]
      val prevPage=declarePage.goBack()
      prevPage must beAnInstanceOf[GCircsYourDetailsPage]
    }

    "navigate to next page" in new WithJsBrowser  with PageObjects{
			val page =  GCircsDeclarationPage(context)

      val claim = CircumstancesScenarioFactory.otherChangeInfo
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage ()
      nextPage must beAnInstanceOf[GCircsDeclarationPage]
    }

    "missing obtainInfoAgreement field" in new WithJsBrowser  with PageObjects{
      val page =  GCircsDeclarationPage(context)
      val claim = new TestData

      claim.CircumstancesDeclarationWhy = obtainInfoWhy

      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Do you agree to the Carer's Allowance Unit contacting anyone mentioned in this form? - You must complete this section")
    }

    "given obtainInfoAgreement is set to 'no' missing obtainInfoWhy field" in new WithJsBrowser  with PageObjects{
      val page =  GCircsDeclarationPage(context)
      val claim = new TestData
      claim.CircumstancesDeclarationInfoAgreement = obtainInfoAgreement

      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("List anyone you don't want to be contacted and say why. - You must complete this section")
    }

    "given circsSomeOneElse checked and missing name or organisation field" in new WithJsBrowser  with PageObjects{
      val page =  GCircsDeclarationPage(context)
      val claim = new TestData
      claim.CircumstancesDeclarationInfoAgreement = obtainInfoAgreement
      claim.CircumstancesDeclarationWhyNot = obtainInfoWhy
      claim.CircumstancesSomeOneElseConfirmation = someOneElse

      page goToThePage()
      page fillPageWith claim

      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
      errors(0) must contain("Your name or organisation - You must complete this section")
    }

    "not have name or organisation field with optional text" in new WithJsBrowser  with PageObjects{
      val page =  GCircsDeclarationPage(context)
      val claim = new TestData
      claim.CircumstancesSomeOneElseConfirmation = someOneElse

      page goToThePage()
      page fillPageWith claim

      page.readLabel("nameOrOrganisation") mustEqual("Your name or organisation")
    }

    "page contains JS enabled check" in new WithJsBrowser  with PageObjects {
      val page = GCircsDeclarationPage(context)
      page goToThePage()
      page.jsCheckEnabled must beTrue
    }
  }
  section("integration", models.domain.CircumstancesConsentAndDeclaration.id)
}
