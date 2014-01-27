package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s8_self_employment._
import utils.pageobjects.s2_about_you.{G3ClaimDatePage, G10AboutYouCompletedPage, G3ClaimDatePageContext}
import controllers.ClaimScenarioFactory
import utils.pageobjects.s9_other_money.G1AboutOtherMoneyPage
import utils.pageobjects.{IterationManager, TestData}
import utils.pageobjects.s3_your_partner.G1YourPartnerPersonalDetailsPage

class G5ChildcareExpensesWhileAtWorkIntegrationSpec extends Specification with Tags {

  "Self Employment Child Care expenses" should {
    "be presented" in new WithBrowser with G5ChildcareExpensesWhileAtWorkPageContext {

      val claimPensionAndExpenses = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
      val pagePensionAndExpenses = new G4SelfEmploymentPensionsAndExpensesPage(browser)
      pagePensionAndExpenses goToThePage()
      pagePensionAndExpenses fillPageWith claimPensionAndExpenses
      pagePensionAndExpenses.submitPage(throwException = true)

      page goToThePage ()
    }

    "not be presented if section not visible" in new WithBrowser with G3ClaimDatePageContext {
      IterationManager.init()
      val claim = ClaimScenarioFactory.s2AnsweringNoToQuestions()
      page goToThePage()
      page runClaimWith (claim, G10AboutYouCompletedPage.title, waitForPage = true)

      val nextPage = page goToPage( throwException = false, page = new G5ChildcareExpensesWhileAtWorkPage(browser))
      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    "contain errors on invalid submission" in {
      "missing mandatory field" in new WithBrowser with G5ChildcareExpensesWhileAtWorkPageContext {
        val claimPensionAndExpenses = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
        val pagePensionAndExpenses = new G4SelfEmploymentPensionsAndExpensesPage(browser)
        pagePensionAndExpenses goToThePage()
        pagePensionAndExpenses fillPageWith claimPensionAndExpenses
        pagePensionAndExpenses.submitPage(throwException = true)

        val claim = new TestData

        page goToThePage()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 6
      }
    }

    "accept submit if all mandatory fields are populated" in new WithBrowser with G5ChildcareExpensesWhileAtWorkPageContext {
      IterationManager.init()
      val claimDate = ClaimScenarioFactory.s2AboutYouWithTimeOutside()
      val pageClaimDate = new G3ClaimDatePage(browser)
      pageClaimDate goToThePage()
      pageClaimDate fillPageWith claimDate
      val nationality = pageClaimDate.submitPage()
      nationality fillPageWith claimDate
      val abroadForMoreThan52Weeks = nationality.submitPage(throwException = true)
      abroadForMoreThan52Weeks fillPageWith claimDate
      val otherEAAStateOrSwitzerland = abroadForMoreThan52Weeks.submitPage(throwException = true)
      otherEAAStateOrSwitzerland fillPageWith claimDate
      val pageMoreAboutYou = otherEAAStateOrSwitzerland.submitPage(throwException = true)
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
      //claim.SelfEmployedChildcareProviderWhatRelationIsToYourPartner = "son"
      page goToThePage()
      page fillPageWith claim
      page submitPage true
    }

    "navigate to next page on valid submission" in new WithBrowser with G5ChildcareExpensesWhileAtWorkPageContext {
      IterationManager.init()
      val claimDate = ClaimScenarioFactory.s2AboutYouWithTimeOutside
      val pageClaimDate = new G3ClaimDatePage(browser)
      pageClaimDate goToThePage()
      pageClaimDate fillPageWith claimDate
      val nationality = pageClaimDate.submitPage()
      nationality fillPageWith claimDate
      val abroadForMoreThan52Weeks = nationality.submitPage(throwException = true)
      abroadForMoreThan52Weeks fillPageWith claimDate
      val otherEAAStateOrSwitzerland = abroadForMoreThan52Weeks.submitPage(throwException = true)
      otherEAAStateOrSwitzerland fillPageWith claimDate
      val pageMoreAboutYou = otherEAAStateOrSwitzerland.submitPage(throwException = true)
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

      nextPage must beAnInstanceOf[G7ExpensesWhileAtWorkPage]
    }
  } section("integration", models.domain.SelfEmployment.id)
}