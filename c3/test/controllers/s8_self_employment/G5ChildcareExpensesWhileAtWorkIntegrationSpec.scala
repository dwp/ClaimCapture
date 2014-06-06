package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s8_self_employment._
import controllers.ClaimScenarioFactory
import utils.pageobjects.s9_other_money.G1AboutOtherMoneyPage
import utils.pageobjects.{PageObjects, PageObjectsContext, TestData}
import utils.pageobjects.s3_your_partner.G1YourPartnerPersonalDetailsPage
import utils.pageobjects.s7_employment.G1EmploymentPage
import utils.pageobjects.s1_2_claim_date.{G1ClaimDatePageContext, G1ClaimDatePage}

class G5ChildcareExpensesWhileAtWorkIntegrationSpec extends Specification with Tags {

  "Self Employment Child Care expenses" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G5ChildcareExpensesWhileAtWorkPage(context)

      val claimPensionAndExpenses = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
      val pagePensionAndExpenses = new G4SelfEmploymentPensionsAndExpensesPage(PageObjectsContext(browser))
      pagePensionAndExpenses goToThePage()
      pagePensionAndExpenses fillPageWith claimPensionAndExpenses
      pagePensionAndExpenses.submitPage(throwException = true)

      page goToThePage ()
    }

    "not be presented if section not visible" in new WithBrowser with G1ClaimDatePageContext {
      val claim = ClaimScenarioFactory.s4CareYouProvideWithNoBreaksInCareWithNoEducationAndNotEmployed()
      page goToThePage()

      val employmentHistoryPage = page runClaimWith(claim, G1EmploymentPage.title, waitForPage = true)
      employmentHistoryPage fillPageWith(claim)

      val nextPage = employmentHistoryPage submitPage()
      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    "contain errors on invalid submission" in {
      "missing mandatory field" in new WithBrowser with PageObjects{
			val page =  G5ChildcareExpensesWhileAtWorkPage(context)
        val claimPensionAndExpenses = ClaimScenarioFactory.s9SelfEmploymentPensionsAndExpenses
        val pagePensionAndExpenses = new G4SelfEmploymentPensionsAndExpensesPage(PageObjectsContext(browser))
        pagePensionAndExpenses goToThePage()
        pagePensionAndExpenses fillPageWith claimPensionAndExpenses
        pagePensionAndExpenses.submitPage(throwException = true)

        val claim = new TestData

        page goToThePage()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 5
      }
    }

    "accept submit if all mandatory fields are populated" in new WithBrowser with PageObjects{
			val page =  G5ChildcareExpensesWhileAtWorkPage(context)

      val claimDate = ClaimScenarioFactory.s2AboutYouWithTimeOutside()
      val pageClaimDate = new G1ClaimDatePage(PageObjectsContext(browser))
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
      //claim.SelfEmployedChildcareProviderWhatRelationIsToYourPartner = "son"
      page goToThePage()
      page fillPageWith claim
      page submitPage true
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects{
			val page =  G5ChildcareExpensesWhileAtWorkPage(context)

      val claimDate = ClaimScenarioFactory.s2AboutYouWithTimeOutside
      val pageClaimDate = new G1ClaimDatePage(PageObjectsContext(browser))
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

      nextPage must beAnInstanceOf[G7ExpensesWhileAtWorkPage]
    }
  } section("integration", models.domain.SelfEmployment.id)
}