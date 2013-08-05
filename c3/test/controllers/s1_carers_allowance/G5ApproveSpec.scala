package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{WithApplication, FakeRequest}
import play.api.test.Helpers._
import play.api.cache.Cache
import models.domain._
import controllers.s1_carers_allowance
import models.domain.Claim

class G5ApproveSpec extends Specification with Tags {
  """Can you get Carer's Allowance""" should {
    "acknowledge that the carer is eligible for allowance" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val claim = Claim().update(Benefits(NoRouting, answer = true))
        .update(Hours(NoRouting, answer = true))
        .update(Over16(NoRouting, answer = true))
        .update(LivesInGB(NoRouting, answer = true))

      Cache.set(claimKey, claim)

      val result = s1_carers_allowance.CarersAllowance.approve(request)
      contentAsString(result) must contain("div class=\"prompt\"")
    }

    "note that the carer is not eligible for allowance" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val claim = Claim().update(Benefits(NoRouting, answer = true))
        .update(Hours(NoRouting, answer = true))
        .update(Over16(NoRouting, answer = false))
        .update(LivesInGB(NoRouting, answer = true))

      Cache.set(claimKey, claim)

      val result = s1_carers_allowance.CarersAllowance.approve(request)

      contentAsString(result) must contain("div class=\"prompt error\"")
    }
  } section "unit"
}