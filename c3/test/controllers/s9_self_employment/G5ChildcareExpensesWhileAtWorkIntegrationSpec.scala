package controllers.s9_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s9_self_employment._
import utils.pageobjects.s2_about_you.{G7PropertyAndRentPage, G4ClaimDatePageContext}
import controllers.ClaimScenarioFactory
import utils.pageobjects.s8_other_money.G1AboutOtherMoneyPage
import utils.pageobjects.ClaimScenario


class G5ChildcareExpensesWhileAtWorkIntegrationSpec extends Specification with Tags {

  "Self Employment Child Care expenses" should {
    "be presented" in new WithBrowser with G5ChildcareExpensesWhileAtWorkPageContext {

      browser.goTo("/employment/beenEmployed")
      browser.click("#beenEmployed_yes")
      browser.submit("button[type='submit']")

      browser.goTo(s"/employment/aboutExpenses/${browser.find("jobID").getValue}")
      browser.click("#payForAnythingNecessary_yes")
      browser.click("#payAnyoneToLookAfterChildren_yes")
      browser.click("#payAnyoneToLookAfterPerson_yes")
      browser.submit("button[type='submit']")

      page goToThePage ()
    }

    "not be presented if section not visible" in new WithBrowser with G4ClaimDatePageContext {
      val claim = ClaimScenarioFactory.s2AnsweringNoToQuestions()
      page goToThePage()
      page runClaimWith (claim, G7PropertyAndRentPage.title, waitForPage = true)

      val nextPage = page goToPage( throwException = false, page = new G5ChildcareExpensesWhileAtWorkPage(browser))
      nextPage must beAnInstanceOf[G1AboutOtherMoneyPage]
    }

    "contain errors on invalid submission" in {
      "missing mandatory field" in new WithBrowser with G5ChildcareExpensesWhileAtWorkPageContext {

        browser.goTo("/employment/beenEmployed")
        browser.click("#beenEmployed_yes")
        browser.submit("button[type='submit']")

        browser.goTo(s"/employment/aboutExpenses/${browser.find("jobID").getValue}")
        browser.click("#payForAnythingNecessary_yes")
        browser.click("#payAnyoneToLookAfterChildren_yes")
        browser.click("#payAnyoneToLookAfterPerson_yes")
        browser.submit("button[type='submit']")


        val claim = new ClaimScenario
        claim.SelfEmployedChildcareProviderNameOfPerson = ""
        page goToThePage()
        page fillPageWith claim
        val pageWithErrors = page.submitPage()
        pageWithErrors.listErrors.size mustEqual 1
      }
    }

    "accept submit if all mandatory fields are populated" in new WithBrowser with G5ChildcareExpensesWhileAtWorkPageContext {

      browser.goTo("/employment/beenEmployed")
      browser.click("#beenEmployed_yes")
      browser.submit("button[type='submit']")

      browser.goTo(s"/employment/aboutExpenses/${browser.find("jobID").getValue}")
      browser.click("#payForAnythingNecessary_yes")
      browser.click("#payAnyoneToLookAfterChildren_yes")
      browser.click("#payAnyoneToLookAfterPerson_yes")
      browser.submit("button[type='submit']")


      val claim = ClaimScenarioFactory.s9SelfEmploymentChildCareExpenses
      page goToThePage()
      page fillPageWith claim
      page submitPage()
    }

    "navigate to next page on valid submission" in new WithBrowser with G5ChildcareExpensesWhileAtWorkPageContext {

      browser.goTo("/employment/beenEmployed")
      browser.click("#beenEmployed_yes")
      browser.submit("button[type='submit']")

      browser.goTo(s"/employment/aboutExpenses/${browser.find("jobID").getValue}")
      browser.click("#payForAnythingNecessary_yes")
      browser.click("#payAnyoneToLookAfterChildren_yes")
      browser.click("#payAnyoneToLookAfterPerson_yes")
      browser.submit("button[type='submit']")


      val claim = ClaimScenarioFactory.s9SelfEmploymentChildCareExpenses
      page goToThePage()
      page fillPageWith claim

      val nextPage = page submitPage()

      nextPage must not(beAnInstanceOf[G7ExpensesWhileAtWorkPage])
    }
  }
}
