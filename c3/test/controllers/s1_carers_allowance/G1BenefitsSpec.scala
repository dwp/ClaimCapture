package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{WithApplication, FakeRequest}
import play.api.test.Helpers._
import play.api.cache.Cache
import java.util.concurrent.TimeUnit
import models.domain._
import models.domain.Claim

class G1BenefitsSpec extends Specification with Tags {
  """Can you get Carer's Allowance""" should {
    "start with a new Claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      G1Benefits.present(request)
      val claim = Cache.getAs[Claim](claimKey)

      TimeUnit.MILLISECONDS.sleep(100)

      val result = G1Benefits.present(request)
      header(CACHE_CONTROL, result) must beSome("no-cache, no-store")

      Cache.getAs[Claim](claimKey) must beLike { case Some(c: Claim) => c.created mustNotEqual claim.get.created }
    }

    "acknowledge that the person looks after get one of the required benefits" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "true", "action" -> "next")
      G1Benefits.submit(request)

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(Benefits) must beLike { case Some(b: Benefits) => b.answer must beTrue }
    }

    "acknowledge that the person looks after does not get one of the required benefits " in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "false", "action" -> "next")
      G1Benefits.submit(request)

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(Benefits) must beLike { case Some(b: Benefits) => b.answer must beFalse }
    }
  } section "unit"
}