package controllers.s10_pay_details

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import models.domain.Claiming
import app.AccountStatus
import models.view.CachedClaim

class G2BankBuildingSocietyDetailsSpec extends Specification with Tags {
  "Bank building society details" should {
    "present after correct details in How We Pay You" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody("likeToPay" -> AccountStatus.BankBuildingAccount.name,
        "paymentFrequency"->"1W")

      val result = G1HowWePayYou.submit(request)

      val request2 = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)

      val result2 = G2BankBuildingSocietyDetails.present(request)
      status(result2) mustEqual OK
    }

    "don't present after How We Pay You" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody("likeToPay" -> AccountStatus.AppliedForAccount.name,
        "paymentFrequency"->"1W")

      val result = G1HowWePayYou.submit(request)

      val request2 = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)

      val result2 = G2BankBuildingSocietyDetails.present(request)
      status(result2) mustEqual SEE_OTHER
    }

    "require all fields to be filled" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody("likeToPay" -> AccountStatus.BankBuildingAccount.name,
        "paymentFrequency"->"1W")

      val result = G1HowWePayYou.submit(request)

      val request2 = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)

      val result2 = G2BankBuildingSocietyDetails.submit(request2)
      status(result2) mustEqual BAD_REQUEST
    }

    "pass after filling all fields" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody("likeToPay" -> AccountStatus.BankBuildingAccount.name,
        "paymentFrequency"->"1W")

      val result = G1HowWePayYou.submit(request)

      val request2 = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody("accountHolderName" -> "some Holder","bankFullName"->"some bank",
        "sortCode.sort1" -> "10","sortCode.sort2" -> "10","sortCode.sort3" -> "10",
        "accountNumber"->"12345678","rollOrReferenceNumber"->"1234567899")

      val result2 = G2BankBuildingSocietyDetails.submit(request2)
      status(result2) mustEqual SEE_OTHER
    }
  } section("unit", models.domain.PayDetails.id)
}