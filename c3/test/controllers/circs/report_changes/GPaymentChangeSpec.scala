package controllers.circs.report_changes

import app.ConfigProperties._
import org.specs2.mutable._
import play.api.test.FakeRequest
import models.domain.MockForm
import models.view.CachedChangeOfCircs
import play.api.test.Helpers._
import models.SortCode
import utils.pageobjects.circumstances.consent_and_declaration.GCircsDeclarationPage
import utils.pageobjects.circumstances.report_changes.GPaymentChangePage
import utils.{WithBrowser, WithApplication}

class GPaymentChangeSpec extends Specification {
  val yes = "yes"
  val no = "no"
  val nameOfCurrentBank = "Nat West"
  val currentPaymentMethod = "Cheque"
  val accountHolderName = "Mr John Doe"
  val whoseNameIsTheAccountIn = "yourName"
  val bankFullName = "HSBC"
  val sortCode1 = "11"
  val sortCode2 = "22"
  val sortCode3 = "33"
  val sortCode = SortCode(sortCode1, sortCode2, sortCode3)
  val accountNumber = "12345678"
  val rollOrReferenceNumber = "My Ref: 123"
  val paymentFrequency = "everyWeek"
  val moreAboutChanges = "Some additional info goes here"
  val paymentChangePath = "DWPCAChangeOfCircumstances//PaymentChange//OtherChanges//Answer"
  val nextPageUrl = GCircsDeclarationPage.url

  val validPaymentChangeFormInputScenario1 = Seq(
    "currentlyPaidIntoBankAnswer" -> yes,
    "currentlyPaidIntoBankText1" -> nameOfCurrentBank,
    "currentlyPaidIntoBankText2" -> "",
    "currentPaymentMethod" -> currentPaymentMethod,
    "accountHolderName" -> accountHolderName,
    "whoseNameIsTheAccountIn" -> whoseNameIsTheAccountIn,
    "bankFullName" -> bankFullName,
    "sortCode.sort1" -> sortCode1,
    "sortCode.sort2" -> sortCode2,
    "sortCode.sort3" -> sortCode3,
    "accountNumber" -> accountNumber,
    "rollOrReferenceNumber" -> "",
    "paymentFrequency" -> paymentFrequency,
    "moreAboutChanges" -> ""
  )

  val validPaymentChangeFormInputScenario2 = Seq(
    "currentlyPaidIntoBankAnswer" -> no,
    "currentlyPaidIntoBankText1" -> "",
    "currentlyPaidIntoBankText2" -> currentPaymentMethod,
    "currentPaymentMethod" -> currentPaymentMethod,
    "accountHolderName" -> accountHolderName,
    "whoseNameIsTheAccountIn" -> whoseNameIsTheAccountIn,
    "bankFullName" -> bankFullName,
    "sortCode.sort1" -> sortCode1,
    "sortCode.sort2" -> sortCode2,
    "sortCode.sort3" -> sortCode3,
    "accountNumber" -> accountNumber,
    "rollOrReferenceNumber" -> "",
    "paymentFrequency" -> paymentFrequency,
    "moreAboutChanges" -> ""
  )

  val blankAccount = Seq(
    "currentlyPaidIntoBankAnswer" -> no,
    "currentlyPaidIntoBankText1" -> "",
    "currentlyPaidIntoBankText2" -> currentPaymentMethod,
    "currentPaymentMethod" -> currentPaymentMethod,
    "accountHolderName" -> accountHolderName,
    "whoseNameIsTheAccountIn" -> whoseNameIsTheAccountIn,
    "bankFullName" -> bankFullName,
    "sortCode.sort1" -> sortCode1,
    "sortCode.sort2" -> sortCode2,
    "sortCode.sort3" -> sortCode3,
    "accountNumber" -> "",
    "rollOrReferenceNumber" -> "",
    "paymentFrequency" -> paymentFrequency,
    "moreAboutChanges" -> ""
  )

  val alphaAccount = Seq(
    "currentlyPaidIntoBankAnswer" -> no,
    "currentlyPaidIntoBankText1" -> "",
    "currentlyPaidIntoBankText2" -> currentPaymentMethod,
    "currentPaymentMethod" -> currentPaymentMethod,
    "accountHolderName" -> accountHolderName,
    "whoseNameIsTheAccountIn" -> whoseNameIsTheAccountIn,
    "bankFullName" -> bankFullName,
    "sortCode.sort1" -> sortCode1,
    "sortCode.sort2" -> sortCode2,
    "sortCode.sort3" -> sortCode3,
    "accountNumber" -> "12345A",
    "rollOrReferenceNumber" -> "",
    "paymentFrequency" -> paymentFrequency,
    "moreAboutChanges" -> ""
  )

  val shortAccount = Seq(
    "currentlyPaidIntoBankAnswer" -> no,
    "currentlyPaidIntoBankText1" -> "",
    "currentlyPaidIntoBankText2" -> currentPaymentMethod,
    "currentPaymentMethod" -> currentPaymentMethod,
    "accountHolderName" -> accountHolderName,
    "whoseNameIsTheAccountIn" -> whoseNameIsTheAccountIn,
    "bankFullName" -> bankFullName,
    "sortCode.sort1" -> sortCode1,
    "sortCode.sort2" -> sortCode2,
    "sortCode.sort3" -> sortCode3,
    "accountNumber" -> "12345",
    "rollOrReferenceNumber" -> "",
    "paymentFrequency" -> paymentFrequency,
    "moreAboutChanges" -> ""
  )

  val spacedAccount = Seq(
    "currentlyPaidIntoBankAnswer" -> no,
    "currentlyPaidIntoBankText1" -> "",
    "currentlyPaidIntoBankText2" -> currentPaymentMethod,
    "currentPaymentMethod" -> currentPaymentMethod,
    "accountHolderName" -> accountHolderName,
    "whoseNameIsTheAccountIn" -> whoseNameIsTheAccountIn,
    "bankFullName" -> bankFullName,
    "sortCode.sort1" -> sortCode1,
    "sortCode.sort2" -> sortCode2,
    "sortCode.sort3" -> sortCode3,
    "accountNumber" -> " 12   12 ",
    "rollOrReferenceNumber" -> "",
    "paymentFrequency" -> paymentFrequency,
    "moreAboutChanges" -> ""
  )

  section("unit", models.domain.CircumstancesReportChanges.id)
  "Report a change in your circumstances - Change in circumstances - Controller" should {
    "present 'CoC Report Changes' " in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

      val result = GPaymentChange.present(request)
      status(result) mustEqual OK
    }

    "redirect to the next page after a valid submission when 'yes' answered to currently paid into bank " in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validPaymentChangeFormInputScenario1: _*)

      val result = GPaymentChange.submit(request)
      redirectLocation(result) must beSome(nextPageUrl)
    }

    "redirect to the next page after a valid submission when 'no' answered to currently paid into bank " in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validPaymentChangeFormInputScenario2: _*)

      val result = GPaymentChange.submit(request)
      redirectLocation(result) must beSome(nextPageUrl)
    }

    "handle gracefully when bad schema number passed to SchemaValidation getRestriction" in new WithApplication {
      val schemaVersion = "BAD-SCHEMA"
      schemaMaxLength(schemaVersion, paymentChangePath) mustEqual -1
    }

    "pull maxlength from xml commons OK" in new WithApplication {
      val schemaVersion = getStringProperty("xml.schema.version")
      schemaVersion must not be "NOT-SET"
      schemaMaxLength(schemaVersion, paymentChangePath) mustEqual 3000
    }

    "have text maxlength set correctly in present()" in new WithBrowser {
      browser.goTo(GPaymentChangePage.url)
      val anythingElse = browser.$("#moreAboutChanges")
      val countdown = browser.$("#moreAboutChanges + .countdown")

      anythingElse.getAttribute("maxlength") mustEqual "3000"
      countdown.getText must contain("3000 char")
      browser.pageSource must contain("maxChars:3000")
    }

    "block with correct error when nothing entered in account number field" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(blankAccount: _*)

      val result = GPaymentChange.submit(request)
      val source = contentAsString(result)
      println("Source:" + source)
      status(result) mustEqual BAD_REQUEST
      source must contain("Account number - Enter your bank account number")
    }

    "block with correct error when chars entered in account number field" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(alphaAccount: _*)

      val result = GPaymentChange.submit(request)
      val source = contentAsString(result)
      println("Source:" + source)
      status(result) mustEqual BAD_REQUEST
      source must contain("Account number - You must only enter numbers")
    }

    "block with correct error when short number entered in account number field" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(shortAccount: _*)

      val result = GPaymentChange.submit(request)
      val source = contentAsString(result)
      println("Source:" + source)
      status(result) mustEqual BAD_REQUEST
      source must contain("Account number - Minimum length is 6")
    }

    "block with correct error when lots of spaces entered" in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(spacedAccount: _*)

      val result = GPaymentChange.submit(request)
      val source = contentAsString(result)
      println("Source:" + source)
      status(result) mustEqual BAD_REQUEST
      source must contain("Account number - Minimum length is 6")
    }
  }
  section("unit", models.domain.CircumstancesReportChanges.id)
}
