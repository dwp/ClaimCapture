package controllers.s11_pay_details

import org.specs2.mutable.{Tags, Specification}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import models.domain.Claiming
import models.view.CachedClaim
import utils.WithApplication

class G1HowWePayYouSpec extends Specification with Tags {
  "How we pay you" should {
    "present" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G1HowWePayYou.present(request)
      status(result) mustEqual OK
    }

    """enforce answer to "How would you like to be paid?" and "How often do you want to get paid?".""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = G1HowWePayYou.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """accept customer gets paid by bank account or building society""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
                                 .withFormUrlEncodedBody("likeToPay" -> "01",
                                                         "paymentFrequency"->"Every four weeks")

      val result = G1HowWePayYou.submit(request)
      redirectLocation(result) must beSome("/pay-details/bank-building-society-details")
    }
  } section("unit", models.domain.PayDetails.id)
}