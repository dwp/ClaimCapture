package controllers.breaks_in_care

import controllers.{WithBrowserHelper, ClaimScenarioFactory}
import org.specs2.mutable._
import utils.pageobjects._
import utils.pageobjects.breaks_in_care.GBreaksTypesPage
import utils.pageobjects.s_care_you_provide.GTheirPersonalDetailsPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.your_income.{GStatutorySickPayPage, GYourIncomePage}
import utils.{WithBrowser, WithJsBrowser}

class GBreaksInCareTypeIntegrationSpec extends Specification {
  section("integration", models.domain.YourIncomes.id)
  "Breaks in care type selection / guard question page" should {
    "be presented" in new WithBrowser with PageObjects {
      val page = GBreaksTypesPage(context)
      page goToThePage()
      page must beAnInstanceOf[GBreaksTypesPage]
    }

    "be presented with correct dynamic question labels" in new WithBrowser with PageObjects with WithBrowserHelper {
      GClaimDatePage.fillClaimDate(context, testData => {
        testData.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "01/10/2016"
      })
      GTheirPersonalDetailsPage.fillDpDetails(context, testData => {})
      val page = GBreaksTypesPage(context)
      page goToThePage()
      $("#breaktype_questionLabel").getText() mustEqual("Since 1 October 2016 have you or Albert Johnson been in any of the following for at least a week?")
      $("#breaktype_other_questionLabel").getText() mustEqual("Have there been any other weeks you've not provided care for Albert Johnson for 35 hours a week?")
    }

    "present 2 errors with correct wording if no fields are populated" in new WithJsBrowser with PageObjects {
      GClaimDatePage.fillClaimDate(context, testData => {
        testData.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "01/10/2016"
      })
      GTheirPersonalDetailsPage.fillDpDetails(context, testData => {})

      val page = GBreaksTypesPage(context)
      page goToThePage()
      val errors = page.submitPage().listErrors
      errors.size mustEqual 2
      errors(0) mustEqual ("Since 1 October 2016 have you or Albert Johnson been in any of the following for at least a week? - You must select 'None' if only None applies.")
      errors(1) mustEqual ("Have there been any other weeks you've not provided care for Albert Johnson for 35 hours a week? - You must complete this section")
    }

    "present 1 error if 2nd field is not populated" in new WithJsBrowser with PageObjects {
      GClaimDatePage.fillClaimDate(context, testData => {
        testData.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "01/10/2016"
      })
      GTheirPersonalDetailsPage.fillDpDetails(context, testData => {})

      val page = GBreaksTypesPage(context)
      page goToThePage()
      val claim = new TestData
      claim.BreaktypeHospitalCheckbox = "true"
      page fillPageWith claim
      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
    }

    "present 1 error if 1st field is not populated" in new WithJsBrowser with PageObjects {
      GClaimDatePage.fillClaimDate(context, testData => {
        testData.ClaimDateWhenDoYouWantYourCarersAllowanceClaimtoStart = "01/10/2016"
      })
      GTheirPersonalDetailsPage.fillDpDetails(context, testData => {})

      val page = GBreaksTypesPage(context)
      page goToThePage()
      val claim = new TestData
      claim.BreaktypeOtherYesNo = "yes"
      page fillPageWith claim
      val errors = page.submitPage().listErrors
      errors.size mustEqual 1
    }

    "navigate to next page on valid submission" in new WithJsBrowser with PageObjects {
      val page = GBreaksTypesPage(context)
      page goToThePage()

      val claim = new TestData
      claim.BreaktypeHospitalCheckbox = "true"
      claim.BreaktypeOtherYesNo = "yes"

      page fillPageWith claim
      val nextPage = page submitPage()
      println("nextPage ================")
      println(nextPage.source)

      // Dont know where we go till we done the rest of breaks ... just expect it to fail for the moment
      // And we need to test the different pages i.e. collect breaks or skip straight through
      page submitPage() must beAnInstanceOf[GStatutorySickPayPage]
      "nextPage" mustEqual ("still-to-do")
    }

    "data should be saved in claim and displayed when go back to page" in new WithJsBrowser with PageObjects with WithBrowserHelper {
      val page = GBreaksTypesPage(context)
      val claim = new TestData
      claim.BreaktypeHospitalCheckbox = "true"
      claim.BreaktypeOtherYesNo = "yes"

      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()
      //      nextPage must beAnInstanceOf[GStatutorySickPayPage]
      //      val pageAgain = nextPage.goBack()
      println(nextPage.source)

      $("#breaktype_hospital").getAttribute("checked") mustEqual ("true")
      $("#breaktype_carehome").getAttribute("checked") mustEqual (null)
      $("#breaktype_none").getAttribute("checked") mustEqual (null)
      $("#breaktype_other_yes").getAttribute("checked") mustEqual ("true")
      $("#breaktype_other_no").getAttribute("checked") mustEqual (null)

      "nextPage" mustEqual ("still-to-do")
    }
  }
  section("integration", models.domain.YourIncomes.id)
}
