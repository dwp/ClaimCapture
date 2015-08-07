package controllers.s_pay_details

import org.specs2.mutable.{Tags, Specification}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import models.domain.Claiming
import app.AccountStatus
import models.view.CachedClaim
import play.api.i18n.Messages
import utils.WithApplication

class GBankBuildingSocietyDetailsSpec extends Specification with Tags {
  "Bank building society details" should {
    "present after correct details in How We Pay You" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("likeToPay" -> Messages(AccountStatus.BankBuildingAccount),
        "paymentFrequency"->"1W")

      val result = GHowWePayYou.submit(request)

      val request2 = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result))

      val result2 = GBankBuildingSocietyDetails.present(request2)
      status(result2) mustEqual OK
    }

    "don't present after How We Pay You" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("likeToPay" -> Messages(AccountStatus.AppliedForAccount),
        "paymentFrequency"->"1W")

      val result = GHowWePayYou.submit(request)

      val request2 = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result))

      val result2 = GBankBuildingSocietyDetails.present(request2)
      status(result2) mustEqual SEE_OTHER
    }

    "require all fields to be filled" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("likeToPay" -> Messages(AccountStatus.BankBuildingAccount.name),
        "paymentFrequency"->"1W")

      val result = GHowWePayYou.submit(request)

      val request2 = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result))

      val result2 = GBankBuildingSocietyDetails.submit(request2)
      status(result2) mustEqual BAD_REQUEST
    }

    "pass after filling all fields" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("likeToPay" ->Messages( AccountStatus.BankBuildingAccount),
        "paymentFrequency"->"1W")

      val result = GHowWePayYou.submit(request)

      val request2 = FakeRequest().withSession(CachedClaim.key -> extractCacheKey(result))
        .withFormUrlEncodedBody("accountHolderName" -> "some Holder","bankFullName"->"some bank",
        "sortCode.sort1" -> "10","sortCode.sort2" -> "10","sortCode.sort3" -> "10",
        "accountNumber"->"12345678","rollOrReferenceNumber"->"1234567899", "whoseNameIsTheAccountIn" -> "Your n   ame")

      val result2 = GBankBuildingSocietyDetails.submit(request2)
      status(result2) mustEqual SEE_OTHER
    }
  } section("unit", models.domain.PayDetails.id)
}