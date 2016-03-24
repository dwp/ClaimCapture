package controllers.circs.report_changes

import app.ConfigProperties._
import org.specs2.mutable._
import play.api.test.FakeRequest
import models.domain.MockForm
import models.view.CachedChangeOfCircs
import play.api.test.Helpers._
import utils.pageobjects.circumstances.report_changes.GAddressChangePage
import utils.{WithBrowser, WithApplication}

/**
 * Created by neddakaltcheva on 2/14/14.
 */
class GAddressChangeSpec extends Specification {
  val yes = "yes"
  val no = "no"
  val postCode = "PR1A4JQ"
  val stillCaringDateDay = 10
  val stillCaringDateMonth = 11
  val stillCaringDateYear = 2012
  val moreAboutChanges = "This is more about the change"
  val addressLineOne = "lineOne"
  val addressLineTwo = "lineTwo"
  val addressLineThree = "lineThree"
  val addressChangePath = "DWPCAChangeOfCircumstances//AddressChange//OtherChanges//Answer"

  val validStillCaringFormInput = Seq(
    "previousAddress.lineOne" -> addressLineOne,
    "previousAddress.lineTwo" -> addressLineTwo,
    "previousAddress.lineThree" -> addressLineThree,
    "previousPostcode" -> postCode,
    "stillCaring.answer" -> yes,
    "newAddress.lineOne" -> addressLineOne,
    "newAddress.lineTwo" -> addressLineTwo,
    "newAddress.lineThree" -> addressLineThree,
    "newPostcode" -> postCode,
    "caredForChangedAddress.answer" -> yes,
    "sameAddress.answer" -> yes,
    "moreAboutChanges" -> moreAboutChanges)

  val validNotCaringFormInput = Seq(
    "previousAddress.lineOne" -> addressLineOne,
    "previousAddress.lineTwo" -> addressLineTwo,
    "previousAddress.lineThree" -> addressLineThree,
    "previousPostcode" -> postCode,
    "stillCaring.answer" -> no,
    "stillCaring.date.day" -> stillCaringDateDay.toString,
    "stillCaring.date.month" -> stillCaringDateMonth.toString,
    "stillCaring.date.year" -> stillCaringDateYear.toString,
    "newAddress.lineOne" -> addressLineOne,
    "newAddress.lineTwo" -> addressLineTwo,
    "newAddress.lineThree" -> addressLineThree
  )

  section("unit", models.domain.CircumstancesAddressChange.id)
  "Circumstances - Address Change - Controller" should {
    "present 'CoC Address Change' " in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

      val result = GAddressChange.present(request)
      status(result) mustEqual OK
    }

    "redirect to the next page after a valid still caring submission" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validStillCaringFormInput: _*)

      val result = GAddressChange.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "redirect to the next page after a valid not caring submission" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validNotCaringFormInput: _*)

      val result = GAddressChange.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    "handle gracefully when bad schema number passed to SchemaValidation getRestriction" in new WithApplication {
      val schemaVersion = "BAD-SCHEMA"
      schemaMaxLength(schemaVersion, addressChangePath) mustEqual -1
    }

    "pull maxlength from xml commons OK" in new WithApplication {
      val schemaVersion = getProperty("xml.schema.version", "NOT-SET")
      schemaVersion must not be "NOT-SET"
      schemaMaxLength(schemaVersion, addressChangePath) mustEqual 3000
    }

    "have text maxlength set correctly in present()" in new WithBrowser {
      browser.goTo(GAddressChangePage.url)
      val anythingElse = browser.$("#moreAboutChanges")
      val countdown = browser.$("#moreAboutChanges + .countdown")

      anythingElse.getAttribute("maxlength") mustEqual "3000"
      countdown.getText must contain( "3000 char")
      browser.pageSource must contain("maxChars:3000")
    }
  }
  section("unit", models.domain.CircumstancesAddressChange.id)
}

