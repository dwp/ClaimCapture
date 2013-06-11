package controllers

import org.specs2.mutable.Specification
import play.api.test.{WithApplication, FakeRequest}
import play.api.test.Helpers._

import play.api.cache.Cache
import models.claim.BenefitsForm
import models.claim.HoursForm
import models.claim.Section
import models.claim.LivesInGBForm
import models.claim.Over16Form
import models.claim.Claim
import scala.Some
import utils.ClaimUtils

class CarersAllowanceSpec extends Specification {
  """Can you get Carer's Allowance""" should {
    "start with a new Claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      CarersAllowance.benefits(request)
      val claim = Cache.getAs[Claim](claimKey)

      val result = CarersAllowance.benefits(request)
      header(CACHE_CONTROL, result) must beSome("no-cache, no-store")

      Cache.getAs[Claim](claimKey) must beLike {
        case Some(c: Claim) => c.created mustNotEqual claim.get.created
      }
    }

    "acknowledge that the person looks after get one of the required benefits" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody("answer" -> "true").withSession("connected" -> claimKey)
      CarersAllowance.benefitsSubmit(request)

      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(Section.allowanceId).get

      section.form(BenefitsForm.id) must beLike {
        case Some(f: BenefitsForm) => f.answer mustEqual true
      }
    }

    "acknowledge that the person looks after does not get one of the required benefits " in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody("answer" -> "false").withSession("connected" -> claimKey)
      CarersAllowance.benefitsSubmit(request)

      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(Section.allowanceId).get

      section.form(BenefitsForm.id) must beLike {
        case Some(f: BenefitsForm) => f.answer mustEqual false
      }
    }

    "present the hours form" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val claim = Claim().update(BenefitsForm(answer = true))
      Cache.set(claimKey, claim)

      val result = CarersAllowance.hours(request)

      status(result) mustEqual OK

      val sectionId = ClaimUtils.sectionId(BenefitsForm().id)
      val answeredForms = claim.completedFormsForSection(sectionId).dropWhile(_.id != BenefitsForm().id)
      answeredForms(0) mustEqual BenefitsForm(answer = true)
    }

    "acknowledge that you spend 35 hours or more each week caring for the person you look after" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody("answer" -> "true").withSession("connected" -> claimKey)
      CarersAllowance.hoursSubmit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(Section.allowanceId).get

      section.form(HoursForm.id) must beLike {
        case Some(f: HoursForm) => f.answer mustEqual true
      }
    }

    """acknowledge that the person looks after get one of the required benefits AND (proving that previous steps are cached)
       acknowledge that you spend 35 hours or more each week caring for the person you look after""" in new WithApplication with Claiming {
      val benefitsRequest = FakeRequest().withFormUrlEncodedBody("answer" -> "true").withSession("connected" -> claimKey)
      CarersAllowance.benefitsSubmit(benefitsRequest)

      val hoursRequest = FakeRequest().withFormUrlEncodedBody("answer" -> "true").withSession("connected" -> claimKey)
      CarersAllowance.hoursSubmit(hoursRequest)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(Section.allowanceId).get

      section.forms.size mustEqual 2

      section.form(HoursForm.id) must beLike {
        case Some(f: HoursForm) => f.answer mustEqual true
      }
    }

    "present the lives in GB form" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val claimWithBenefitFrom = Claim().update(BenefitsForm(answer = true))
      val claimWithHoursForm = claimWithBenefitFrom.update(HoursForm(answer = true))
      Cache.set(claimKey, claimWithHoursForm)

      val result = CarersAllowance.hours(request)

      status(result) mustEqual OK

      val sectionId = ClaimUtils.sectionId(LivesInGBForm.id)
      val answeredForms = claimWithHoursForm.completedFormsForSection(sectionId)

      answeredForms(0) mustEqual BenefitsForm(answer = true)
      answeredForms(1) mustEqual HoursForm(answer = true)
    }

    "acknowledge that carer lives in Great Britain" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody("answer" -> "true").withSession("connected" -> claimKey)
      CarersAllowance.livesInGBSubmit(request)

      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(Section.allowanceId).get

      section.form(LivesInGBForm.id) must beLike {
        case Some(f: LivesInGBForm) => f.answer mustEqual true
      }
    }

    "present the Are you aged 16 or over form" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val claimWithBenefit = Claim().update(BenefitsForm(answer = true))
      val claimWithHours = claimWithBenefit.update(HoursForm(answer = true))
      val claimWithLivesInGB = claimWithHours.update(LivesInGBForm(answer = true))
      Cache.set(claimKey, claimWithLivesInGB)

      val result = CarersAllowance.hours(request)

      status(result) mustEqual OK

      val sectionId = ClaimUtils.sectionId(Over16Form().id)
      val answeredForms = claimWithLivesInGB.completedFormsForSection(sectionId)

      answeredForms(0) mustEqual BenefitsForm(answer = true)
      answeredForms(1) mustEqual HoursForm(answer = true)
      answeredForms(2) mustEqual LivesInGBForm(answer = true)
    }

    "acknowledge that carer is aged 16 or over" in new WithApplication with Claiming {
      val request = FakeRequest().withFormUrlEncodedBody("answer" -> "true").withSession("connected" -> claimKey)
      CarersAllowance.over16Submit(request)

      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(Section.allowanceId).get

      section.form(Over16Form.id) must beLike {
        case Some(f: Over16Form) => f.answer mustEqual true
      }
    }

    "acknowledge that the carer is eligible for allowance" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val claim = Claim().update(BenefitsForm(answer = true))
        .update(HoursForm(answer = true))
        .update(LivesInGBForm(answer = true))
        .update(Over16Form(answer = true))

      Cache.set(claimKey, claim)

      val result = CarersAllowance.approve(request)
      contentAsString(result) must contain("Based on your answers you may be entitled to Carer’s Allowance.")
    }

    "note that the carer is not eligible for allowance" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val claim = Claim().update(BenefitsForm(answer = true))
        .update(HoursForm(answer = true))
        .update(LivesInGBForm(answer = false))
        .update(Over16Form(answer = true))

      Cache.set(claimKey, claim)

      val result = CarersAllowance.approve(request)

      contentAsString(result) must contain("Based on your answers you may not be entitled to Carer’s Allowance.")
    }
  }
}