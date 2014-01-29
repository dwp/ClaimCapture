package controllers.s9_other_money

import org.specs2.mutable.Specification
import org.specs2.mutable.Tags

import controllers.ClaimScenarioFactory
import utils.pageobjects.{PageObjects, PageObjectsContext, TestData}
import utils.pageobjects.s9_other_money._
import play.api.test.WithBrowser

class G5StatutorySickPayIntegrationSpec extends Specification with Tags {

  "Statutory Sick Pay" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G5StatutorySickPayPage(context)
      page goToThePage ()
    }

    "contain errors on invalid submission" in {
      "had sick pay but missing mandatory field" in new WithBrowser with PageObjects{
			val page =  G5StatutorySickPayPage(context)
        val claim = new TestData
        claim.OtherMoneyStatutorySickPayHaveYouHadAnyStatutorySickPay = "yes"
        page goToThePage ()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 1
      }

      "had sick pay but then invalid postcode" in new WithBrowser with PageObjects{
			val page =  G5StatutorySickPayPage(context)
        val claim = new TestData
        claim.OtherMoneyStatutorySickPayHaveYouHadAnyStatutorySickPay = "yes"
        claim.OtherMoneyStatutorySickPayEmployersNameEmployers = "Johnny B Good"
        claim.OtherMoneyStatutorySickPayEmployersPostCode = "INVALID"
        page goToThePage ()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 1
        pageWithErrors.listErrors(0).contains("postcode")
      }

      "howOften frequency of other with no other text entered" in new WithBrowser with PageObjects {
        val page = G5StatutorySickPayPage(context)
        val claim = new TestData
        claim.OtherMoneyHaveYouSSPSinceClaim = "yes"
        claim.OtherMoneySSPHowMuch = "123"
        claim.OtherMoneySSPHowOften = "other"
        claim.OtherMoneySSPEmployerName = "Burger King"
        page goToThePage ()
        page fillPageWith claim

        val errors = page.submitPage().listErrors

        errors.size mustEqual 1
        errors(0) must contain("How often?")
      }
    }

    "contain the completed forms" in new WithBrowser with PageObjects{
			val page =  G1AboutOtherMoneyPage(context)
      val claim = ClaimScenarioFactory.s9otherMoney
      page goToThePage ()
      page fillPageWith claim
      val moneyPaidPage = page submitPage ()
      val personContactPage = moneyPaidPage.goToPage(new G5StatutorySickPayPage(PageObjectsContext(browser)))
      personContactPage.listCompletedForms.size mustEqual 1
    }

    "navigate back" in new WithBrowser with PageObjects{
			val page =  G1AboutOtherMoneyPage(context)
      val claim = ClaimScenarioFactory.s9otherMoney
      page goToThePage ()
      page fillPageWith claim
      val nextPage = page.submitPage()
      nextPage must beAnInstanceOf[G5StatutorySickPayPage]
      val prevPage = nextPage.goBack()
      prevPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects{
			val page =  G5StatutorySickPayPage(context)
      val claim = ClaimScenarioFactory.s9otherMoney
      page goToThePage ()
      page fillPageWith claim

      val nextPage = page submitPage ()

      nextPage must beAnInstanceOf[G6OtherStatutoryPayPage]
    }

    "navigate to next page on valid submission with other field selected" in new WithBrowser with PageObjects {
      val page = G5StatutorySickPayPage(context)
      val claim = new TestData
      claim.OtherMoneyHaveYouSSPSinceClaim = "yes"
      claim.OtherMoneySSPHowMuch = "123"
      claim.OtherMoneySSPHowOften = "other"
      claim.OtherMoneySSPHowOftenOther = "every day and twice on Sundays"
      claim.OtherMoneySSPEmployerName = "Burger King"
      page goToThePage ()
      page fillPageWith claim

      val nextPage = page submitPage ()

      nextPage must beAnInstanceOf[G6OtherStatutoryPayPage]
    }
  } section ("integration", models.domain.OtherMoney.id)
}