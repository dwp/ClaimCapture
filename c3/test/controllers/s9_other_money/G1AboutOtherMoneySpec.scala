package controllers.s9_other_money

import models.domain._
import models.{MultiLineAddress, PaymentFrequency, domain}
import org.specs2.mutable.{Specification, Tags}
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}

class G1AboutOtherMoneySpec extends Specification with Tags {
  "Benefits and payments - Controller" should {
    val anyPaymentsSinceClaimDate = "yes"
    val whoPaysYou = "The Man"
    val howMuch = "12"
    val howOften_frequency = "other"
    val howOften_frequency_other = "Every day and twice on Sundays"
    val employersName = "Toys R Us"
    val employersAddressLineOne = "Address line 1"
    val employersAddressLineTwo = "Address line 2"
    val employersAddressLineThree = "Address line 3"
    val employersPostcode = "PR1A4JQ"
    val yes = "yes"

    val formInput = Seq("anyPaymentsSinceClaimDate.answer" -> anyPaymentsSinceClaimDate,
      "whoPaysYou" -> whoPaysYou,
      "howMuch" -> howMuch,
      "howOften.frequency" -> howOften_frequency,
      "howOften.frequency.other" -> howOften_frequency_other,
      "statutorySickPay.answer" -> yes,
      "statutorySickPay.howMuch" -> howMuch,
      "statutorySickPay.howOften.frequency" -> howOften_frequency,
      "statutorySickPay.howOften.frequency.other" -> howOften_frequency_other,
      "statutorySickPay.employersName" -> employersName,
      "statutorySickPay.employersAddress.lineOne" -> employersAddressLineOne,
      "statutorySickPay.employersAddress.lineTwo" -> employersAddressLineTwo,
      "statutorySickPay.employersAddress.lineThree" -> employersAddressLineThree,
      "statutorySickPay.employersPostcode" -> employersPostcode,
      "otherStatutoryPay.answer" -> yes,
      "otherStatutoryPay.howMuch" -> howMuch,
      "otherStatutoryPay.howOften.frequency" -> howOften_frequency,
      "otherStatutoryPay.howOften.frequency.other" -> howOften_frequency_other,
      "otherStatutoryPay.employersName" -> employersName,
      "otherStatutoryPay.employersAddress.lineOne" -> employersAddressLineOne,
      "otherStatutoryPay.employersAddress.lineTwo" -> employersAddressLineTwo,
      "otherStatutoryPay.employersAddress.lineThree" -> employersAddressLineThree,
      "otherStatutoryPay.employersPostcode" -> employersPostcode
    )

    "present 'Other money '" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = G1AboutOtherMoney.present(request)

      status(result) mustEqual OK
    }

    "add submitted data to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(formInput: _*)

      val result = G1AboutOtherMoney.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(domain.OtherMoney)

      section.questionGroup(AboutOtherMoney) must beLike {
        case Some(f: AboutOtherMoney) => {
          f.anyPaymentsSinceClaimDate.answer must equalTo(anyPaymentsSinceClaimDate)
          f.whoPaysYou must equalTo(Some(whoPaysYou))
          f.howMuch must equalTo(Some(howMuch))
          f.howOften must equalTo(Some(PaymentFrequency(howOften_frequency, Some(howOften_frequency_other))))
          f.statutorySickPay.answer must equalTo(yes)
          f.statutorySickPay.howMuch must equalTo(Some(howMuch))
          f.statutorySickPay.howOften must equalTo(Some(PaymentFrequency(howOften_frequency, Some(howOften_frequency_other))))
          f.statutorySickPay.employersName must equalTo(Some(employersName))
          f.statutorySickPay.address must equalTo(Some(MultiLineAddress(Some(employersAddressLineOne), Some(employersAddressLineTwo), Some(employersAddressLineThree))))
          f.statutorySickPay.postCode must equalTo(Some(employersPostcode))
          f.otherStatutoryPay.answer must equalTo(yes)
          f.otherStatutoryPay.howMuch must equalTo(Some(howMuch))
          f.otherStatutoryPay.howOften must equalTo(Some(PaymentFrequency(howOften_frequency, Some(howOften_frequency_other))))
          f.otherStatutoryPay.employersName must equalTo(Some(employersName))
          f.otherStatutoryPay.address must equalTo(Some(MultiLineAddress(Some(employersAddressLineOne), Some(employersAddressLineTwo), Some(employersAddressLineThree))))
          f.otherStatutoryPay.postCode must equalTo(Some(employersPostcode))
        }
      }
    }

    "return a bad request after an invalid submission" in {
      "reject invalid yesNo answers" in new WithApplication with Claiming {
        val request = FakeRequest()
          .withFormUrlEncodedBody("anyPaymentsSinceClaimDate.answer" -> "INVALID",
            "statutorySickPay.answer" -> "INVALID", "otherStatutoryPay.answer" -> "INVALID")

        val result = controllers.s9_other_money.G1AboutOtherMoney.submit(request)
        status(result) mustEqual BAD_REQUEST
      }

      "missing mandatory fields" in new WithApplication with Claiming {
        val request = FakeRequest()
          .withFormUrlEncodedBody("" -> "")

        val result = controllers.s9_other_money.G1AboutOtherMoney.submit(request)
        status(result) mustEqual BAD_REQUEST
      }

      "reject a howOften frequency of other with no other text entered" in new WithApplication with Claiming {
        val request = FakeRequest()
          .withFormUrlEncodedBody("anyPaymentsSinceClaimDate.answer" -> anyPaymentsSinceClaimDate,
            "whoPaysYou" -> whoPaysYou,
            "howMuch" -> howMuch,
            "howOften.frequency" -> "Other",
            "howOften.frequency.other" -> "",
            "statutorySickPay.answer" -> "no",
            "otherStatutoryPay.answer" -> "no")
        val result = controllers.s9_other_money.G1AboutOtherMoney.submit(request)
        status(result) mustEqual BAD_REQUEST
      }
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(formInput: _*)

      val result = G1AboutOtherMoney.submit(request)

      status(result) mustEqual SEE_OTHER
    }
  } section ("unit", models.domain.OtherMoney.id)
}
