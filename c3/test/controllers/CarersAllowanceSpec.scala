package controllers

import org.specs2.mutable.Specification
import play.api.test.{WithApplication, FakeRequest}
import play.api.test.Helpers._

import play.api.cache.Cache
import models.view._
import java.util.concurrent.TimeUnit
import models.domain
import models.domain._
import models.domain.Section
import models.domain.Claim
import scala.Some

class CarersAllowanceSpec extends Specification {
  """Can you get Carer's Allowance""" should {
    "start with a new Claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      controllers.CarersAllowance.benefits(request)
      val claim = Cache.getAs[Claim](claimKey)

      TimeUnit.MILLISECONDS.sleep(100)

      val result = controllers.CarersAllowance.benefits(request)
      header(CACHE_CONTROL, result) must beSome("no-cache, no-store")

      Cache.getAs[Claim](claimKey) must beLike {
        case Some(c: Claim) => c.timeStamp mustNotEqual claim.get.timeStamp
      }
    }

    "acknowledge that the person looks after get one of the required benefits" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "true", "action" -> "next")
      controllers.CarersAllowance.benefitsSubmit(request)

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(Benefits.id) must beLike {
        case Some(f: Benefits) => f.answer mustEqual true
      }
    }

    "acknowledge that the person looks after does not get one of the required benefits " in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "false", "action" -> "next")
      controllers.CarersAllowance.benefitsSubmit(request)

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(Benefits.id) must beLike {
        case Some(f: Benefits) => f.answer mustEqual false
      }
    }

    "present the hours form" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val claim = Claim().update(Benefits(answer = true))
      Cache.set(claimKey, claim)

      val result = controllers.CarersAllowance.hours(request)

      status(result) mustEqual OK

      val sectionId = Claim.sectionId(Benefits.id)
      val answeredForms = claim.completedQuestionGroups(sectionId).dropWhile(_.id != Benefits.id)
      answeredForms(0) mustEqual Benefits(answer = true)
    }

    "acknowledge that you spend 35 hours or more each week caring for the person you look after" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "true", "action" -> "next")
      controllers.CarersAllowance.hoursSubmit(request)

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(Hours.id) must beLike {
        case Some(f: Hours) => f.answer mustEqual true
      }
    }

    """acknowledge that the person looks after get one of the required benefits AND (proving that previous steps are cached)
       acknowledge that you spend 35 hours or more each week caring for the person you look after""" in new WithApplication with Claiming {
      val benefitsRequest = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "true", "action" -> "next")
      controllers.CarersAllowance.benefitsSubmit(benefitsRequest)

      val hoursRequest = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "true", "action" -> "next")
      controllers.CarersAllowance.hoursSubmit(hoursRequest)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.CarersAllowance.id).get

      section.questionGroups.size mustEqual 2

      section.questionGroup(Hours.id) must beLike {
        case Some(f: Hours) => f.answer mustEqual true
      }
    }

    "present the lives in GB form" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val claimWithBenefitFrom = Claim().update(Benefits(answer = true))
      val claimWithHoursForm = claimWithBenefitFrom.update(Hours(answer = true))
      Cache.set(claimKey, claimWithHoursForm)

      val result = controllers.CarersAllowance.hours(request)

      status(result) mustEqual OK

      val sectionId = Claim.sectionId(LivesInGB.id)
      val answeredForms = claimWithHoursForm.completedQuestionGroups(sectionId)

      answeredForms(0) mustEqual Benefits(answer = true)
      answeredForms(1) mustEqual Hours(answer = true)
    }

    "acknowledge that carer lives in Great Britain" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "true", "action" -> "next")
      controllers.CarersAllowance.livesInGBSubmit(request)

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(LivesInGB.id) must beLike {
        case Some(f: LivesInGB) => f.answer mustEqual true
      }
    }

    "present the Are you aged 16 or over form" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val claimWithBenefit = Claim().update(Benefits(answer = true))
      val claimWithHours = claimWithBenefit.update(Hours(answer = true))
      val claimWithLivesInGB = claimWithHours.update(LivesInGB(answer = true))
      Cache.set(claimKey, claimWithLivesInGB)

      val result = controllers.CarersAllowance.hours(request)

      status(result) mustEqual OK

      val sectionId = Claim.sectionId(Over16.id)
      val answeredForms = claimWithLivesInGB.completedQuestionGroups(sectionId)

      answeredForms(0) mustEqual Benefits(answer = true)
      answeredForms(1) mustEqual Hours(answer = true)
      answeredForms(2) mustEqual LivesInGB(answer = true)
    }

    "acknowledge that carer is aged 16 or over" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey).withFormUrlEncodedBody("answer" -> "true", "action" -> "next")
      controllers.CarersAllowance.over16Submit(request)

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(Over16.id) must beLike {
        case Some(f: Over16) => f.answer mustEqual true
      }
    }

    "acknowledge that the carer is eligible for allowance" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val claim = Claim().update(Benefits(answer = true))
        .update(Hours(answer = true))
        .update(LivesInGB(answer = true))
        .update(Over16(answer = true))

      Cache.set(claimKey, claim)

      val result = controllers.CarersAllowance.approve(request)
      contentAsString(result) must contain("div class=\"prompt\"")
    }

    "note that the carer is not eligible for allowance" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val claim = Claim().update(Benefits(answer = true))
        .update(Hours(answer = true))
        .update(LivesInGB(answer = false))
        .update(Over16(answer = true))

      Cache.set(claimKey, claim)

      val result = controllers.CarersAllowance.approve(request)

      contentAsString(result) must contain("div class=\"prompt error\"")
    }
  }
}