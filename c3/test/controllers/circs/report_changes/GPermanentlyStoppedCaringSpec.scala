package controllers.circs.report_changes

import app.ConfigProperties._
import play.api.test.FakeRequest
import models.domain._
import models.view.CachedChangeOfCircs
import play.api.test.Helpers._
import org.specs2.mutable._
import utils.pageobjects.circumstances.report_changes.GPermanentlyStoppedCaringPage
import utils.{WithBrowser, WithApplication}

class GPermanentlyStoppedCaringSpec extends Specification {
  val moreAboutChanges = "more about the change"
  val stoppedCaringDateDay = 23
  val stoppedCaringDateMonth = 12
  val stoppedCaringDateYear = 2013
  val stoppedCaringPath = "DWPCAChangeOfCircumstances//StoppedCaring//OtherChanges//Answer"

  val stoppedCaringInput = Seq("moreAboutChange" -> moreAboutChanges,
    "stoppedCaringDate.day" -> stoppedCaringDateDay.toString,
    "stoppedCaringDate.month" -> stoppedCaringDateMonth.toString,
    "stoppedCaringDate.year" -> stoppedCaringDateYear.toString
  )

  section("unit", models.domain.CircumstancesStoppedCaring.id)
  "Circumstances - PermanentlyStoppedCaring - Controller" should {
    "present 'Permanently Stopped Caring' " in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

      val result = GPermanentlyStoppedCaring.present(request)
      status(result) mustEqual OK
    }

    "redirect to the next page after a valid submission" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(stoppedCaringInput: _*)

      val result = GPermanentlyStoppedCaring.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "handle gracefully when bad schema number passed to SchemaValidation getRestriction" in new WithApplication {
      val schemaVersion = "BAD-SCHEMA"
      schemaMaxLength(schemaVersion, stoppedCaringPath) mustEqual -1
    }

    "pull maxlength from xml commons OK" in new WithApplication {
      val schemaVersion = getStringProperty("xml.schema.version")
      schemaVersion must not be "NOT-SET"
      schemaMaxLength(schemaVersion, stoppedCaringPath) mustEqual 3000
    }

    "have text maxlength set correctly in present()" in new WithBrowser {
      browser.goTo(GPermanentlyStoppedCaringPage.url)
      val anythingElse = browser.$("#moreAboutChanges")
      val countdown = browser.$("#moreAboutChanges + .countdown")

      anythingElse.getAttribute("maxlength") mustEqual "3000"
      countdown.getText must contain( "3000 char")
      browser.pageSource must contain("maxChars:3000")
    }
  }
  section("unit", models.domain.CircumstancesStoppedCaring.id)
}
