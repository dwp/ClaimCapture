package controllers.s9_other_money

import org.specs2.mutable.Specification
import org.specs2.mutable.Tags

import controllers.ClaimScenarioFactory
import utils.pageobjects.{PageObjects, PageObjectsContext, TestData}
import utils.pageobjects.s9_other_money._
import play.api.test.WithBrowser
import utils.pageobjects.s10_pay_details.G1HowWePayYouPage

class G6OtherStatutoryPayIntegrationSpec extends Specification with Tags {

  "Other Statutory Pay - About Other Money" should {
    "be presented" in new WithBrowser with PageObjects{
			val page =  G6OtherStatutoryPayPage(context)
      page goToThePage ()
    }

    "contain errors on invalid submission" in {
      "had sick pay but missing mandatory field" in new WithBrowser with PageObjects{
			val page =  G6OtherStatutoryPayPage(context)
        val claim = new TestData
        claim.OtherMoneyHaveYouSMPSinceClaim = "yes"
        page goToThePage ()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 1
      }

      "howOften frequency of other with no other text entered" in new WithBrowser with PageObjects {
        val page = G6OtherStatutoryPayPage(context)
        val claim = new TestData
        claim.OtherMoneyHaveYouSMPSinceClaim = "yes"
        claim.OtherMoneySMPEmployerName = "Employers Name"
        claim.OtherMOneySMPHowOften = "Other"

        page goToThePage ()
        page fillPageWith claim

        val errors = page.submitPage().listErrors

        errors.size mustEqual 1
        errors(0) must contain("How often?")
      }
    }

    "contain the completed forms" in new WithBrowser with PageObjects{
			val page =  G5StatutorySickPayPage(context)
      val claim = ClaimScenarioFactory.s9otherMoney
      page goToThePage ()
      page fillPageWith claim
      val otherStatutoryPayPage = page submitPage ()
      otherStatutoryPayPage.listCompletedForms.size mustEqual 1
    }

    "navigate back to previous page" in new WithBrowser with PageObjects{
			val page =  G5StatutorySickPayPage(context)
      val claim = ClaimScenarioFactory.s9otherMoney
      page goToThePage ()
      page fillPageWith claim
      val nextPage = page.submitPage()
      nextPage must beAnInstanceOf[G6OtherStatutoryPayPage]

      nextPage.goBack() must beAnInstanceOf[G5StatutorySickPayPage]
    }

    "navigate to next page on valid submission" in new WithBrowser with PageObjects{
			val page =  G6OtherStatutoryPayPage(context)
      val claim = ClaimScenarioFactory.s9otherMoney
      page goToThePage ()
      page fillPageWith claim

      val nextPage = page submitPage ()

      nextPage must not(beAnInstanceOf[G6OtherStatutoryPayPage])
    }

    "navigate to next page on valid submission with other field selected" in new WithBrowser with PageObjects {
      val page = G6OtherStatutoryPayPage(context)
      val claim = new TestData
      claim.OtherMoneyHaveYouSMPSinceClaim = "yes"
      claim.OtherMoneySMPEmployerName = "Employers Name"
      claim.OtherMOneySMPHowOften = "other"
      claim.OtherMOneySMPHowOftenOther = "every day and twice on Sundays"
      page goToThePage ()

      page fillPageWith claim

      val nextPage = page submitPage ()

      nextPage must not(beAnInstanceOf[G6OtherStatutoryPayPage])
    }

    "navigate to the Pay Details on clicking next" in new WithBrowser with PageObjects{
			val page =  G1AboutOtherMoneyPage(context)
      val claim = ClaimScenarioFactory.s9otherMoney
      page goToThePage()
      page fillPageWith claim
      page submitPage()

      val OtherStatutoryPage = page goToPage new G6OtherStatutoryPayPage(PageObjectsContext(browser))
      OtherStatutoryPage fillPageWith claim
      val howWePayPage = OtherStatutoryPage submitPage()

      howWePayPage must beAnInstanceOf[G1HowWePayYouPage]
    }
  } section ("integration", models.domain.OtherMoney.id)
}