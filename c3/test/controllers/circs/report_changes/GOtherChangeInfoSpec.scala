package controllers.circs.report_changes

import app.ConfigProperties._
import play.api.test.FakeRequest
import models.domain.{CircumstancesOtherInfo, MockForm}
import models.view.CachedChangeOfCircs
import play.api.test.Helpers._
import org.specs2.mutable._
import utils.pageobjects.circumstances.report_changes.GOtherChangeInfoPage
import utils.{WithBrowser, WithApplication}

class GOtherChangeInfoSpec extends Specification {
  val otherInfo = "other info"
  val otherChangeInfoInput = Seq("changeInCircs" -> otherInfo)
  val otherChangePath = "DWPCAChangeOfCircumstances//OtherChanges//Answer"

  section("unit", models.domain.CircumstancesOtherInfo.id)
  "Circumstances - OtherChangeInfo - Controller" should {
    "present 'Other Change Information' " in new WithApplication with MockForm {
      val request = FakeRequest()

      val result = GOtherChangeInfo.present(request)
      status(result) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication with MockForm {
      val request = FakeRequest()
        .withFormUrlEncodedBody(otherChangeInfoInput: _*)

      val result = GOtherChangeInfo.submit(request)
      val claim = getClaimFromCache(result,CachedChangeOfCircs.key)
      claim.questionGroup[CircumstancesOtherInfo] must beLike {
        case Some(f: CircumstancesOtherInfo) => {
          f.change must equalTo(otherInfo)
        }
      }
    }

    "redirect to the next page after a valid submission" in new WithApplication with MockForm {
      val request = FakeRequest()
        .withFormUrlEncodedBody(otherChangeInfoInput: _*)

      val result = GOtherChangeInfo.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "handle gracefully when bad schema number passed to SchemaValidation getRestriction" in new WithApplication {
      val schemaVersion = "BAD-SCHEMA"
      schemaMaxLength(schemaVersion, otherChangePath) mustEqual -1
    }

    "pull maxlength from xml commons OK" in new WithApplication {
      val schemaVersion = getProperty("xml.schema.version", "NOT-SET")
      schemaVersion must not be "NOT-SET"
      schemaMaxLength(schemaVersion, otherChangePath) mustEqual 3000
    }

    "have text maxlength set correctly in present()" in new WithBrowser {
      browser.goTo(GOtherChangeInfoPage.url)
      val anythingElse = browser.$("#changeInCircs")
      val countdown = browser.$("#changeInCircs + .countdown")

      anythingElse.getAttribute("maxlength") mustEqual "3000"
      countdown.getText must contain( "3000 char")
      browser.pageSource must contain("maxChars:3000")
    }
  }
  section("unit", models.domain.CircumstancesOtherInfo.id)
}
