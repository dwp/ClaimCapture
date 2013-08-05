package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{WithApplication, FakeRequest}
import play.api.test.Helpers._
import play.api.cache.Cache
import models.domain._
import models.domain.Section
import models.domain.Claim

class G4LivesInGBSpec extends Specification with Tags {
  """Can you get Carer's Allowance""" should {
    "present the lives in GB form" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val claimWithBenefitFrom = Claim().update(Benefits(NoRouting, answer = true))
      val claimWithHoursForm = claimWithBenefitFrom.update(Hours(NoRouting, answer = true))
      val claimWithOver16Form = claimWithHoursForm.update(Over16(NoRouting, answer = true))
      Cache.set(claimKey, claimWithOver16Form)

      val result = G2Hours.present(request)

      status(result) mustEqual OK

      val sectionIdentifier = Section.sectionIdentifier(LivesInGB)
      val completedQuestionGroups = claimWithOver16Form.completedQuestionGroups(sectionIdentifier)

      completedQuestionGroups(0) must beLike { case Benefits(_, answer) => answer must beTrue }
      completedQuestionGroups(1) must beLike { case Hours(_, answer) => answer must beTrue }
      completedQuestionGroups(2) must beLike { case Over16(_, answer) => answer must beTrue }
    }

    "acknowledge that carer lives in Great Britain" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "true", "action" -> "next")
      G4LivesInGB.submit(request)

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(LivesInGB) must beLike { case Some(l: LivesInGB) => l.answer must beTrue }
    }
  } section "unit"
}