package controllers.s1_carers_allowance

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{WithApplication, FakeRequest}
import play.api.test.Helpers._
import play.api.cache.Cache
import models.domain
import models.domain._
import models.domain.Section
import models.domain.Claim
import controllers.s1_carers_allowance

class G2HoursSpec extends Specification with Tags {
  """Can you get Carer's Allowance""" should {
    "present the hours form" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val claim = Claim().update(Benefits(answer = true))
      Cache.set(claimKey, claim)

      val result = s1_carers_allowance.G2Hours.present(request)

      status(result) mustEqual OK

      val sectionID = Section.sectionID(Benefits)
      val answeredForms = claim.completedQuestionGroups(sectionID).dropWhile(_.id != Benefits.id)
      answeredForms(0) mustEqual Benefits(answer = true)
    }

    "acknowledge that you spend 35 hours or more each week caring for the person you look after" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "true", "action" -> "next")
      s1_carers_allowance.G2Hours.submit(request)

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(Hours) must beLike {
        case Some(f: Hours) => f.answer mustEqual true
      }
    }

    """acknowledge that the person looks after get one of the required benefits AND (proving that previous steps are cached)
       acknowledge that you spend 35 hours or more each week caring for the person you look after""" in new WithApplication with Claiming {
      val benefitsRequest = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "true", "action" -> "next")
      s1_carers_allowance.G1Benefits.submit(benefitsRequest)

      val hoursRequest = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "true", "action" -> "next")
      s1_carers_allowance.G2Hours.submit(hoursRequest)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.CarersAllowance.id)

      section.questionGroups.size mustEqual 2

      section.questionGroup(Hours) must beLike {
        case Some(f: Hours) => f.answer mustEqual true
      }
    }
  } section "unit"
}