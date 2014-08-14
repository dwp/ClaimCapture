package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{TestBrowser, WithBrowser}
import utils.pageobjects.s8_self_employment._
import controllers.{Formulate, ClaimScenarioFactory}
import utils.pageobjects.s9_other_money.G1AboutOtherMoneyPage
import utils.pageobjects.s3_your_partner.G1YourPartnerPersonalDetailsPage
import utils.pageobjects.{PageObjects, PageObjectsContext}
import utils.pageobjects.s7_employment.G1EmploymentPage
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage
import utils.pageobjects.s2_about_you.{G4NationalityAndResidencyPage, G1YourDetailsPage}

class G7ExpensesWhileAtWorkIntegrationSpec extends Specification with Tags {

  "Expenses related to the Person you care for while at work" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G7ExpensesWhileAtWorkPage(context)
      val claimPensionAndExpenses = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
      val pagePensionAndExpenses = new G4SelfEmploymentPensionsAndExpensesPage(PageObjectsContext(browser))
      pagePensionAndExpenses goToThePage()
      pagePensionAndExpenses fillPageWith claimPensionAndExpenses
      pagePensionAndExpenses.submitPage(throwException = true)

      page goToThePage()
    }

    "not be presented if section not visible" in new WithBrowser with PageObjects{
			val page =  G1ClaimDatePage(context)
      Formulate.claimDate(browser)
      Formulate.yourDetails(browser)
      Formulate.yourContactDetails(browser)
      Formulate.nationalityAndResidency(browser)
      Formulate.abroadForMoreThan52Weeks(browser)
      Formulate.otherEEAStateOrSwitzerland(browser)
      Formulate.moreAboutYou(browser)
      Formulate.notInEmployment(browser)

      page goToPage( throwException = false, page = new G1EmploymentPage(PageObjectsContext(browser)))

      val nextPage = page goToPage(throwException = false, page = new G7ExpensesWhileAtWorkPage(PageObjectsContext(browser)))
      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    "contain the completed forms" in new WithBrowser with PageObjects{
			val page =  G1AboutSelfEmploymentPage(context)
      val claim = ClaimScenarioFactory.s9SelfEmployment

      val claimPensionAndExpenses = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
      val pagePensionAndExpenses = new G4SelfEmploymentPensionsAndExpensesPage(PageObjectsContext(browser))
      pagePensionAndExpenses goToThePage()
      pagePensionAndExpenses fillPageWith claimPensionAndExpenses
      pagePensionAndExpenses.submitPage(throwException = true)

      page goToThePage()
      page fillPageWith claim
      val g2 = page.submitPage(throwException = true)
      val g7 = g2 goToPage (new G7ExpensesWhileAtWorkPage(PageObjectsContext(browser)))
      g7.listCompletedForms.size mustEqual 2
    }

    "contain errors on invalid submission missing mandatory field" in new WithBrowser with PageObjects{
			val page =  G7ExpensesWhileAtWorkPage(context)

      val aboutYou = ClaimScenarioFactory.s2AboutYouWithTimeOutside
      val pageAboutYou = new G1YourDetailsPage(PageObjectsContext(browser))
      pageAboutYou goToThePage()
      pageAboutYou fillPageWith aboutYou

      val pageMoreAboutYou = pageAboutYou.submitPage(throwException = true)
      pageMoreAboutYou fillPageWith aboutYou
      pageMoreAboutYou.submitPage(throwException = true)

      val claimAboutYourPartner = ClaimScenarioFactory.s3YourPartnerNotThePersonYouCareFor
      val pageAboutYourPartner = new G1YourPartnerPersonalDetailsPage(PageObjectsContext(browser))
      pageAboutYourPartner goToThePage()
      pageAboutYourPartner fillPageWith claimAboutYourPartner
      pageAboutYourPartner.submitPage(throwException = true)

      val claimPensionAndExpenses = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
      val pagePensionAndExpenses = new G4SelfEmploymentPensionsAndExpensesPage(PageObjectsContext(browser))
      pagePensionAndExpenses goToThePage()
      pagePensionAndExpenses fillPageWith claimPensionAndExpenses
      pagePensionAndExpenses.submitPage(throwException = true)

      val claim = ClaimScenarioFactory.s9SelfEmploymentChildCareExpenses
      page goToThePage()
      page fillPageWith claim
      val nextPage = page submitPage()

      val pageWithErrors = nextPage.submitPage()
      pageWithErrors.listErrors.size mustEqual 4
      pageWithErrors.listErrors(0).contains("nameOfPerson")
    }

    "navigate back to previous page" in new WithBrowser with PageObjects{

      val g7ExpensesWhileAtWork = gotoExpensesPage(context,browser)

      g7ExpensesWhileAtWork.goBack() must beAnInstanceOf[G5ChildcareExpensesWhileAtWorkPage]
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects{
      val g7ExpensesWhileAtWork = gotoExpensesPage(context,browser)
      g7ExpensesWhileAtWork fillPageWith ClaimScenarioFactory.s9SelfEmploymentExpensesRelatedToPersonYouCareFor

      val nextPage = g7ExpensesWhileAtWork.submitPage(throwException = true)

      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    def gotoExpensesPage(context:PageObjectsContext, browser:TestBrowser) = {

      val aboutYou = ClaimScenarioFactory.s2AboutYouWithTimeOutside
      val pageAboutYou = new G1YourDetailsPage(PageObjectsContext(browser))
      pageAboutYou goToThePage()
      pageAboutYou fillPageWith aboutYou

      val aboutYouContactDetails = pageAboutYou.submitPage(throwException = true)
      aboutYouContactDetails fillPageWith ClaimScenarioFactory.s2AboutYouWithTimeOutside

      val nationalityAndResidency = aboutYouContactDetails.submitPage(throwException = true)
      nationalityAndResidency fillPageWith ClaimScenarioFactory.s2AboutYouWithTimeOutside

      val abroadForMoreThan52Weeks = nationalityAndResidency.submitPage(throwException = true)
      abroadForMoreThan52Weeks fillPageWith ClaimScenarioFactory.s2AboutYouWithTimeOutside

      val otherEEAStateOrSwitzerland = abroadForMoreThan52Weeks.submitPage(throwException = true)
      otherEEAStateOrSwitzerland fillPageWith aboutYou

      val pageMoreAboutYou = otherEEAStateOrSwitzerland.submitPage(throwException = true)
      pageMoreAboutYou fillPageWith aboutYou

      val pageAboutYourPartner = pageMoreAboutYou.submitPage(throwException = true)
      pageAboutYourPartner fillPageWith ClaimScenarioFactory.s3YourPartnerNotThePersonYouCareFor
      pageAboutYourPartner.submitPage(throwException = true)

      val g4SelfEmploymentPensionAndExpenses = new G4SelfEmploymentPensionsAndExpensesPage(PageObjectsContext(browser))
      g4SelfEmploymentPensionAndExpenses goToThePage()
      g4SelfEmploymentPensionAndExpenses fillPageWith ClaimScenarioFactory.s9SelfEmploymentExpensesRelatedToPersonYouCareFor

      val g5ChildCareExpensesWhileAtWork = g4SelfEmploymentPensionAndExpenses.submitPage(throwException = true)
      g5ChildCareExpensesWhileAtWork fillPageWith ClaimScenarioFactory.s9SelfEmploymentExpensesRelatedToPersonYouCareFor

      val g7ExpensesWhileAtWork = g5ChildCareExpensesWhileAtWork.submitPage(throwException = true)

      g7ExpensesWhileAtWork
    }
  } section("integration", models.domain.SelfEmployment.id)
}