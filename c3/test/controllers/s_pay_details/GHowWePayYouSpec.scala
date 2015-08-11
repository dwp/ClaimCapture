package controllers.s_pay_details

import org.specs2.mutable.{Tags, Specification}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import models.domain.Claiming
import models.view.CachedClaim
import utils.WithApplication
import utils.pageobjects.s_information.GAdditionalInfoPage

class GHowWePayYouSpec extends Specification with Tags {
  "How we pay you" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GHowWePayYou.present(request)
      status(result) mustEqual OK
    }

    """enforce answer to "How would you like to be paid?" and "How often do you want to get paid?".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = GHowWePayYou.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept customer gets paid by bank account or building society""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody("likeToPay" -> "01", // todo - 01 does not mean anything...
          "paymentFrequency" -> "Every four weeks")

      val result = GHowWePayYou.submit(request)

      redirectLocation(result) must beSome(GAdditionalInfoPage.url)
    }


    "pass after filling all fields" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "likeToPay" -> "yes",
          "paymentFrequency" -> "1W",
          "bankDetails.accountHolderName" -> "some Holder",
          "bankDetails.bankFullName" -> "some bank",
          "bankDetails.sortCode.sort1" -> "10",
          "bankDetails.sortCode.sort2" -> "10",
          "bankDetails.sortCode.sort3" -> "10",
          "bankDetails.accountNumber" -> "12345678",
          "bankDetails.rollOrReferenceNumber" -> "1234567899",
          "bankDetails.whoseNameIsTheAccountIn" -> "Your name"
        )

      val result2 = GHowWePayYou.submit(request)
      status(result2) mustEqual SEE_OTHER
    }

  } section("unit", models.domain.PayDetails.id)
}