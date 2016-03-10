package controllers.circs.report_changes

import app.ConfigProperties._
import org.specs2.mutable._
import play.api.test.FakeRequest
import models.domain.MockForm
import models.view.CachedChangeOfCircs
import play.api.test.Helpers._
import utils.pageobjects.circumstances.report_changes.GSelfEmploymentPage
import utils.{WithBrowser, WithApplication}

class GSelfEmploymentSpec extends Specification {
  val yes = "yes"
  val no = "no"
  val stillCaringDateDay = 10
  val stillCaringDateMonth = 11
  val stillCaringDateYear = 2012
  val whenThisSelfEmploymentStartedDateDay = 11
  val whenThisSelfEmploymentStartedDateMonth = 12
  val whenThisSelfEmploymentStartedDateYear = 2013
  val typeOfBusiness = "Plumber"
  val moreAboutChanges = "This is more about the change"
  val invalidYear = 99999
  val selfEmploymentPath = "DWPCAChangeOfCircumstances//EmploymentChange//SelfEmployment//MoreAboutChanges//Answer"

  val validStillCaringFormInput = Seq(
    "stillCaring.answer" -> yes,
    "whenThisSelfEmploymentStarted.day" -> whenThisSelfEmploymentStartedDateDay.toString,
    "whenThisSelfEmploymentStarted.month" -> whenThisSelfEmploymentStartedDateMonth.toString,
    "whenThisSelfEmploymentStarted.year" -> whenThisSelfEmploymentStartedDateYear.toString,
    "typeOfBusiness" -> typeOfBusiness,
    "totalOverWeeklyIncomeThreshold" -> yes,
    "moreAboutChanges" -> moreAboutChanges
  )

  val validNotCaringFormInput = Seq(
    "stillCaring.answer" -> no,
    "stillCaring.date.day" -> stillCaringDateDay.toString,
    "stillCaring.date.month" -> stillCaringDateMonth.toString,
    "stillCaring.date.year" -> stillCaringDateYear.toString,
    "whenThisSelfEmploymentStarted.day" -> whenThisSelfEmploymentStartedDateDay.toString,
    "whenThisSelfEmploymentStarted.month" -> whenThisSelfEmploymentStartedDateMonth.toString,
    "whenThisSelfEmploymentStarted.year" -> whenThisSelfEmploymentStartedDateYear.toString,
    "typeOfBusiness" -> typeOfBusiness,
    "totalOverWeeklyIncomeThreshold" -> yes,
    "moreAboutChanges" -> moreAboutChanges
  )


  section("unit", models.domain.CircumstancesSelfEmployment.id)
  "Circumstances - Self Employment - Controller" should {
    "present 'CoC Self Employment' " in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

      val result = GSelfEmployment.present(request)
      status(result) mustEqual OK
    }

    "redirect to the next page after a valid still caring submission" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validStillCaringFormInput: _*)

      val result = GSelfEmployment.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "redirect to the next page after a valid still caring submission" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validNotCaringFormInput: _*)

      val result = GSelfEmployment.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "handle gracefully when bad schema number passed to SchemaValidation getRestriction" in new WithApplication {
      val schemaVersion = "BAD-SCHEMA"
      schemaMaxLength(schemaVersion, selfEmploymentPath) mustEqual -1
    }

    "pull maxlength from xml commons OK" in new WithApplication {
      val schemaVersion = getProperty("xml.schema.version", "NOT-SET")
      schemaVersion must not be "NOT-SET"
      schemaMaxLength(schemaVersion, selfEmploymentPath) mustEqual 3000
    }

    "have text maxlength set correctly in present()" in new WithBrowser {
      browser.goTo(GSelfEmploymentPage.url)
      val anythingElse = browser.$("#moreAboutChanges")
      val countdown = browser.$("#moreAboutChanges + .countdown")

      anythingElse.getAttribute("maxlength") mustEqual "3000"
      countdown.getText must contain( "3000 char")
      browser.pageSource must contain("maxChars:3000")
    }
  }
  section("unit", models.domain.CircumstancesSelfEmployment.id)
}
