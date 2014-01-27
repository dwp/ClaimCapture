package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s8_self_employment._
import controllers.{Formulate, ClaimScenarioFactory}
import utils.pageobjects.s2_about_you.{G3ClaimDatePage, G9EmploymentPage, G3ClaimDatePageContext}
import utils.pageobjects.s9_other_money.G1AboutOtherMoneyPage
import utils.pageobjects.s3_your_partner.G1YourPartnerPersonalDetailsPage
import utils.pageobjects.IterationManager

class G7ExpensesWhileAtWorkIntegrationSpec extends Specification with Tags {

  "Expenses related to the Person you care for while at work" should {
    "be presented" in new WithBrowser with G7ExpensesWhileAtWorkPageContext {
      val claimPensionAndExpenses = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
      val pagePensionAndExpenses = new G4SelfEmploymentPensionsAndExpensesPage(browser)
      pagePensionAndExpenses goToThePage()
      pagePensionAndExpenses fillPageWith claimPensionAndExpenses
      pagePensionAndExpenses.submitPage(throwException = true)

      page goToThePage()
    }

    "not be presented if section not visible" in new WithBrowser with G3ClaimDatePageContext {
      Formulate.yourDetails(browser)
      Formulate.yourContactDetails(browser)
      Formulate.claimDate(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.abroadForMoreThan52Weeks(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      Formulate.moreAboutYou(browser)
      Formulate.notInEmployment(browser)

      page goToPage( throwException = false, page = new G9EmploymentPage(browser))

      val nextPage = page goToPage(throwException = false, page = new G7ExpensesWhileAtWorkPage(browser))
      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    "contain the completed forms" in new WithBrowser with G1AboutSelfEmploymentPageContext {
      val claim = ClaimScenarioFactory.s9SelfEmployment

      val claimPensionAndExpenses = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
      val pagePensionAndExpenses = new G4SelfEmploymentPensionsAndExpensesPage(browser)
      pagePensionAndExpenses goToThePage()
      pagePensionAndExpenses fillPageWith claimPensionAndExpenses
      pagePensionAndExpenses.submitPage(throwException = true)

      page goToThePage()
      page fillPageWith claim
      val g2 = page.submitPage(throwException = true)
      val g7 = g2 goToPage (new G7ExpensesWhileAtWorkPage(browser))
      g7.listCompletedForms.size mustEqual 2
    }

    "contain errors on invalid submission missing mandatory field" in new WithBrowser with G7ExpensesWhileAtWorkPageContext {
      val claimDate = ClaimScenarioFactory.s2AboutYouWithTimeOutside
      val pageClaimDate = new G3ClaimDatePage(browser)
      pageClaimDate goToThePage()
      pageClaimDate fillPageWith claimDate
      val pageMoreAboutYou = pageClaimDate.submitPage(throwException = true)
      pageMoreAboutYou fillPageWith claimDate
      pageMoreAboutYou.submitPage(throwException = true)

      val claimAboutYourPartner = ClaimScenarioFactory.s3YourPartnerNotThePersonYouCareFor
      val pageAboutYourPartner = new G1YourPartnerPersonalDetailsPage(browser)
      pageAboutYourPartner goToThePage()
      pageAboutYourPartner fillPageWith claimAboutYourPartner
      pageAboutYourPartner.submitPage(throwException = true)

      val claimPensionAndExpenses = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
      val pagePensionAndExpenses = new G4SelfEmploymentPensionsAndExpensesPage(browser)
      pagePensionAndExpenses goToThePage()
      pagePensionAndExpenses fillPageWith claimPensionAndExpenses
      pagePensionAndExpenses.submitPage(throwException = true)

      val claim = ClaimScenarioFactory.s9SelfEmploymentChildCareExpenses
      page goToThePage()
      page fillPageWith claim
      val nextPage = page submitPage()

      val pageWithErrors = nextPage.submitPage()
      pageWithErrors.listErrors.size mustEqual 5
      pageWithErrors.listErrors(0).contains("nameOfPerson")
    }

    "navigate back to previous page" in new WithBrowser with G4SelfEmploymentPensionsAndExpensesPageContext {
      /*      val claim = ClaimScenarioFactory.s9SelfEmploymentExpensesRelatedToPersonYouCareFor

            page goToThePage()

            val g7 = page runClaimWith(claim, G7ExpensesWhileAtWorkPage.title)

            g7.goBack() must beAnInstanceOf[G5ChildcareExpensesWhileAtWorkPage]
      */


      IterationManager.init()
      val claimDate = ClaimScenarioFactory.s2AboutYouWithTimeOutside
      val pageClaimDate = new G3ClaimDatePage(browser)
      pageClaimDate goToThePage()
      pageClaimDate fillPageWith claimDate
      val nationality = pageClaimDate.submitPage(throwException = true)
      nationality fillPageWith ClaimScenarioFactory.s2AboutYouWithTimeOutside
      val abroadForMoreThan52Weeks = nationality.submitPage(throwException = true)
      abroadForMoreThan52Weeks fillPageWith ClaimScenarioFactory.s2AboutYouWithTimeOutside
      val otherEEAStateOrSwitzerland = abroadForMoreThan52Weeks.submitPage(throwException = true)
      otherEEAStateOrSwitzerland fillPageWith ClaimScenarioFactory.s2AboutYouWithTimeOutside
      val pageMoreAboutYou = otherEEAStateOrSwitzerland.submitPage(throwException = true)
      pageMoreAboutYou fillPageWith claimDate
      pageMoreAboutYou.submitPage(throwException = true)

      val claimAboutYourPartner = ClaimScenarioFactory.s3YourPartnerNotThePersonYouCareFor
      val pageAboutYourPartner = new G1YourPartnerPersonalDetailsPage(browser)
      pageAboutYourPartner goToThePage()
      pageAboutYourPartner fillPageWith claimAboutYourPartner
      pageAboutYourPartner.submitPage(throwException = true)

      val claimPensionAndExpenses = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
      val pagePensionAndExpenses = new G4SelfEmploymentPensionsAndExpensesPage(browser)
      pagePensionAndExpenses goToThePage()
      pagePensionAndExpenses fillPageWith claimPensionAndExpenses
      pagePensionAndExpenses.submitPage(throwException = true)

      val claim = ClaimScenarioFactory.s9SelfEmploymentChildCareExpenses
      page goToThePage()
      page fillPageWith claim
      val g5 = page submitPage()
      g5 fillPageWith claim

      val g7 = g5 submitPage()

      g7.goBack() must beAnInstanceOf[G5ChildcareExpensesWhileAtWorkPage]
    }

    "navigate to next page on valid submission" in new WithBrowser with G7ExpensesWhileAtWorkPageContext {
      IterationManager.init()
      val pageClaimDate = new G3ClaimDatePage(browser)
      pageClaimDate goToThePage()
      pageClaimDate fillPageWith ClaimScenarioFactory.s2AboutYouWithTimeOutside
      val nationality = pageClaimDate.submitPage(throwException = true)
      nationality fillPageWith ClaimScenarioFactory.s2AboutYouWithTimeOutside
      val abroadForMoreThan52Weeks = nationality.submitPage(throwException = true)
      abroadForMoreThan52Weeks fillPageWith ClaimScenarioFactory.s2AboutYouWithTimeOutside
      val otherEEAStateOrSwitzerland = abroadForMoreThan52Weeks.submitPage(throwException = true)
      otherEEAStateOrSwitzerland fillPageWith ClaimScenarioFactory.s2AboutYouWithTimeOutside
      val pageMoreAboutYou = otherEEAStateOrSwitzerland.submitPage(throwException = true)
      pageMoreAboutYou fillPageWith ClaimScenarioFactory.s2AboutYouWithTimeOutside
      pageMoreAboutYou.submitPage(throwException = true)

      val pageAboutYourPartner = new G1YourPartnerPersonalDetailsPage(browser)
      pageAboutYourPartner goToThePage()
      pageAboutYourPartner fillPageWith ClaimScenarioFactory.s3YourPartnerNotThePersonYouCareFor
      pageAboutYourPartner.submitPage(throwException = true)

      val g4 = new G4SelfEmploymentPensionsAndExpensesPage(browser)
      g4 goToThePage()
      g4 fillPageWith ClaimScenarioFactory.s9SelfEmploymentExpensesRelatedToPersonYouCareFor
      val g5 = g4.submitPage(throwException = true)
      g5 fillPageWith ClaimScenarioFactory.s9SelfEmploymentExpensesRelatedToPersonYouCareFor
      val g7 = g5.submitPage(throwException = true)
      g7 fillPageWith ClaimScenarioFactory.s9SelfEmploymentExpensesRelatedToPersonYouCareFor

      val nextPage = g7.submitPage(throwException = true)

      nextPage must beAnInstanceOf[G9CompletedPage]
    }
  } section("integration", models.domain.SelfEmployment.id)
}