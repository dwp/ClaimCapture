package controllers.s1_carers_allowance

import org.specs2.mutable.Specification
import play.api.test.{WithApplication, FakeRequest}
import play.api.test.Helpers._
import play.api.cache.Cache
import models.domain._
import models.domain.Section
import models.domain.Claim
import scala.Some
import controllers.s1_carers_allowance

class G4LivesInGBSpec extends Specification {
  """Can you get Carer's Allowance""" should {
    "present the lives in GB form" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val claimWithBenefitFrom = Claim().update(Benefits(answer = true))
      val claimWithHoursForm = claimWithBenefitFrom.update(Hours(answer = true))
      val claimWithOver16Form = claimWithHoursForm.update(Over16(answer = true))
      Cache.set(claimKey, claimWithOver16Form)

      val result = s1_carers_allowance.G2Hours.present(request)

      status(result) mustEqual OK

      val sectionID = Section.sectionID(LivesInGB.id)
      val answeredForms = claimWithOver16Form.completedQuestionGroups(sectionID)

      answeredForms(0) mustEqual Benefits(answer = true)
      answeredForms(1) mustEqual Hours(answer = true)
      answeredForms(2) mustEqual Over16(answer = true)
    }

    "acknowledge that carer lives in Great Britain" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "true", "action" -> "next")
      s1_carers_allowance.G4LivesInGB.submit(request)

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(LivesInGB) must beLike {
        case Some(f: LivesInGB) => f.answer mustEqual true
      }
    }
  }
}