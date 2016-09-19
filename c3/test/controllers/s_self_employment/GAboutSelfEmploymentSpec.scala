package controllers.s_self_employment

import controllers.mappings.Mappings
import models.domain._
import org.specs2.mutable._
import play.api.test.{FakeRequest}
import utils.pageobjects.PageObjects
import utils.pageobjects.s_self_employment.GSelfEmploymentDatesPage
import utils.{WithJsBrowser, WithBrowser, WithApplication}
import play.api.test.Helpers._
import utils.pageobjects.your_income.{GYourIncomePage}

class GAboutSelfEmploymentSpec extends Specification {
  val simplePageInput = Seq(
    "typeOfWork" -> "Tree cutter",
    "stillSelfEmployed" -> Mappings.yes,
    "moreThanYearAgo" -> Mappings.yes,
    "haveAccounts" -> Mappings.yes
  )

  section("unit", models.domain.SelfEmployment.id)
  "Self Employment - About Self Employment - Controller" should {
    "present Self-Employment page" in new WithBrowser with PageObjects {
      GYourIncomePage.fillYourIncomes(context, testData => {
        testData.EmploymentHaveYouBeenSelfEmployedAtAnyTime = Mappings.yes
      })
      val page = GSelfEmploymentDatesPage(context)
      page must beAnInstanceOf[GSelfEmploymentDatesPage]
    }

    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody(simplePageInput: _*)
      val result = controllers.s_self_employment.GSelfEmploymentDates.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(models.domain.SelfEmployment)
      section.questionGroup(SelfEmploymentDates) must beLike {
        case Some(f: SelfEmploymentDates) => {
          f.typeOfWork mustEqual ("Tree cutter")
          f.stillSelfEmployed mustEqual (Mappings.yes)
        }
      }
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody(simplePageInput: _*)
      val result = controllers.s_self_employment.GSelfEmploymentDates.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "have text maxlength set correctly in present()" in new WithBrowser with PageObjects {
      val selfEmploymentPage = GYourIncomePage.fillYourIncomes(context, testData => {
        testData.EmploymentHaveYouBeenSelfEmployedAtAnyTime = Mappings.yes
        testData.YourIncomeNone = "true"
      })
      selfEmploymentPage must beAnInstanceOf[GSelfEmploymentDatesPage]
      val typeOfWork = browser.$("#typeOfWork")
      typeOfWork.getAttribute("maxlength") mustEqual "60"
    }

    "present 3 errors when submit with nothing populated" in new WithJsBrowser with PageObjects {
      val selfEmploymentPage = GYourIncomePage.fillYourIncomes(context, testData => {
        testData.EmploymentHaveYouBeenSelfEmployedAtAnyTime = Mappings.yes
        testData.YourIncomeNone = "true"
      })
      val errors = selfEmploymentPage.submitPage().listErrors
      errors.size mustEqual 3
      errors(0) mustEqual ("Type of business or work - You must complete this section")
      errors(1) mustEqual ("Are you still doing this work? - You must complete this section")
      errors(2) mustEqual ("Did you start this work more than a year ago? - You must complete this section")
    }

    "present when did you finish this work date error" in new WithJsBrowser with PageObjects {
      val selfEmployErrorPage = GYourIncomePage.fillSelfEmployed(context, testData => {
        testData.EmploymentHaveYouBeenSelfEmployedAtAnyTime = Mappings.yes
        testData.SelfEmployedTypeOfWork = "Plumber"
        testData.SelfEmployedAreYouSelfEmployedNow = Mappings.no
        testData.SelfEmployedMoreThanYearAgo = Mappings.yes
        testData.SelfEmployedHaveAccounts = Mappings.yes
      })
      val errors = selfEmployErrorPage.listErrors
      errors.size mustEqual 1
      errors(0) mustEqual ("When did you finish this work? - You must complete this section")
    }

    "present do you have accounts error" in new WithJsBrowser with PageObjects {
      val selfEmployErrorPage = GYourIncomePage.fillSelfEmployed(context, testData => {
        testData.EmploymentHaveYouBeenSelfEmployedAtAnyTime = Mappings.yes
        testData.SelfEmployedTypeOfWork = "Plumber"
        testData.SelfEmployedAreYouSelfEmployedNow = Mappings.yes
        testData.SelfEmployedMoreThanYearAgo = Mappings.yes
        testData.SelfEmployedHaveAccounts = ""
      })
      val errors = selfEmployErrorPage.listErrors
      errors.size mustEqual 1
      errors(0) mustEqual ("Do you have accounts? - You must complete this section")
    }

    "present when did you start this work error and paid money yet error" in new WithJsBrowser with PageObjects {
      val selfEmployErrorPage = GYourIncomePage.fillSelfEmployed(context, testData => {
        testData.EmploymentHaveYouBeenSelfEmployedAtAnyTime = Mappings.yes
        testData.SelfEmployedTypeOfWork = "Plumber"
        testData.SelfEmployedAreYouSelfEmployedNow = Mappings.yes
        testData.SelfEmployedMoreThanYearAgo = Mappings.no
        testData.SelfEmployedHaveAccounts = ""
      })
      val errors = selfEmployErrorPage.listErrors
      errors.size mustEqual 2
      errors(0) mustEqual ("When did you start this work? - You must complete this section")
      errors(1) mustEqual ("Has your self-employed business been paid any money yet? - You must complete this section")
    }
  }
  section("unit", models.domain.SelfEmployment.id)
}
