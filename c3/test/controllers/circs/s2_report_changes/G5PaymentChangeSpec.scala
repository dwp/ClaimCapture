package controllers.circs.s2_report_changes

import org.specs2.mutable._
import play.api.test.FakeRequest
import models.domain.MockForm
import models.view.CachedChangeOfCircs
import play.api.test.Helpers._
import controllers.circs.s2_report_changes
import models.SortCode
import utils.WithApplication

class G5PaymentChangeSpec extends Specification {
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
  val sortCode = SortCode(sortCode1,sortCode2,sortCode3)
  val accountNumber = "12345678"
  val rollOrReferenceNumber = "My Ref: 123"
  val paymentFrequency = "everyWeek"
  val moreAboutChanges = "Some additional info goes here"

  val validPaymentChangeFormInputScenario1 = Seq(
    "currentlyPaidIntoBank.answer" -> yes,
    "currentlyPaidIntoBank.text1" -> nameOfCurrentBank,
    "currentlyPaidIntoBank.text2" -> "",
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
    "currentlyPaidIntoBank.answer" -> no,
    "currentlyPaidIntoBank.text1" -> "",
    "currentlyPaidIntoBank.text2" -> currentPaymentMethod,
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

  section("unit", models.domain.CircumstancesReportChanges.id)
  "Report a change in your circumstances - Change in circumstances - Controller" should {
    "present 'CoC Report Changes' " in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)

      val result = G5PaymentChange.present(request)
      status(result) mustEqual OK
    }

    "redirect to the next page after a valid submission when 'yes' answered to currently paid into bank " in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validPaymentChangeFormInputScenario1: _*)

      val result = s2_report_changes.G5PaymentChange.submit(request)
      redirectLocation(result) must beSome("/circumstances/consent-and-declaration/declaration")
    }

    "redirect to the next page after a valid submission when 'no' answered to currently paid into bank " in new WithApplication with MockForm {
      val request = FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey)
        .withFormUrlEncodedBody(validPaymentChangeFormInputScenario2: _*)

      val result = s2_report_changes.G5PaymentChange.submit(request)
      redirectLocation(result) must beSome("/circumstances/consent-and-declaration/declaration")
    }
  }
  section("unit", models.domain.CircumstancesReportChanges.id)
}
