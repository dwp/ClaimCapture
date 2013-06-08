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

class CarersAllowanceSpec extends Specification {
  """Can you get Carer's Allowance""" should {
    "start with a new Claim" in new WithApplication {
      val request = FakeRequest().withSession("connected" -> "claim")

      val claim = Claim()
      Cache.set("claim", claim)

      val result = CarersAllowance.benefits(request)

      Cache.get("claim") must beLike {
        case Some(c: Claim) => c.created mustNotEqual claim.created
      }
    }

    "acknowledge that the person looks after get one of the required benefits" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody("answer" -> "true").withSession("connected" -> "claim")
      CarersAllowance.benefitsSubmit(request)

      val claim = Cache.getAs[Claim]("claim").get
      val section: Section = claim.section("s1").get

      section.form("s1.g1") must beLike {
        case Some(f: BenefitsForm) => f.answer mustEqual true
      }
    }

    "acknowledge that the person looks after does not get one of the required benefits " in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody("answer" -> "false").withSession("connected" -> "claim")
      CarersAllowance.benefitsSubmit(request)

      val claim = Cache.getAs[Claim]("claim").get
      val section: Section = claim.section("s1").get

      section.form("s1.g1") must beLike {
        case Some(f: BenefitsForm) => f.answer mustEqual false
      }
    }

    "present the hours form" in new WithApplication {
      val request = FakeRequest().withSession("connected" -> "claim")

      val claim = Claim().update(BenefitsForm(answer = true))
      Cache.set("claim", claim)

      val result = CarersAllowance.hours(request)

      status(result) mustEqual OK

      val answeredForms = claim.completedFormsForSection(BenefitsForm().section).dropWhile(_.id != BenefitsForm().id)
      answeredForms(0) mustEqual BenefitsForm(answer = true)
    }

    "acknowledge that you spend 35 hours or more each week caring for the person you look after" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody("answer" -> "true").withSession("connected" -> "claim")
      CarersAllowance.hoursSubmit(request)
      val claim = Cache.getAs[Claim]("claim").get
      val section: Section = claim.section("s1").get

      section.form("s1.g2") must beLike {
        case Some(f: HoursForm) => f.answer mustEqual true
      }
    }

    """acknowledge that the person looks after get one of the required benefits AND (proving that previous steps are cached)
       acknowledge that you spend 35 hours or more each week caring for the person you look after""" in new WithApplication {
      val benefitsRequest = FakeRequest().withFormUrlEncodedBody("answer" -> "true").withSession("connected" -> "claim")
      CarersAllowance.benefitsSubmit(benefitsRequest)

      val hoursRequest = FakeRequest().withFormUrlEncodedBody("answer" -> "true").withSession("connected" -> "claim")
      CarersAllowance.hoursSubmit(hoursRequest)
      val claim = Cache.getAs[Claim]("claim").get
      val section: Section = claim.section("s1").get

      section.forms.size mustEqual 2

      section.form("s1.g2") must beLike {
        case Some(f: HoursForm) => f.answer mustEqual true
      }
    }

    "present the lives in GB form" in new WithApplication {
      val request = FakeRequest().withSession("connected" -> "claim")

      val claimWithBenefitFrom = Claim().update(BenefitsForm(answer = true))
      val claimWithHoursForm = claimWithBenefitFrom.update(HoursForm(answer = true))
      Cache.set("claim", claimWithHoursForm)

      val result = CarersAllowance.hours(request)

      status(result) mustEqual OK

      val answeredForms = claimWithHoursForm.completedFormsForSection(LivesInGBForm().section)

      answeredForms(0) mustEqual BenefitsForm(answer = true)
      answeredForms(1) mustEqual HoursForm(answer = true)
    }

    "acknowledge that carer lives in Great Britain" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody("answer" -> "true").withSession("connected" -> "claim")
      CarersAllowance.livesInGBSubmit(request)

      val claim = Cache.getAs[Claim]("claim").get
      val section: Section = claim.section("s1").get

      section.form("s1.g3") must beLike {
        case Some(f: LivesInGBForm) => f.answer mustEqual true
      }
    }

    "present the Are you aged 16 or over form" in new WithApplication {
      val request = FakeRequest().withSession("connected" -> "claim")

      val claimWithBenefit = Claim().update(BenefitsForm(answer = true))
      val claimWithHours = claimWithBenefit.update(HoursForm(answer = true))
      val claimWithLivesInGB = claimWithHours.update(LivesInGBForm(answer = true))
      Cache.set("claim", claimWithLivesInGB)

      val result = CarersAllowance.hours(request)

      status(result) mustEqual OK

      val answeredForms = claimWithLivesInGB.completedFormsForSection(Over16Form().section)

      answeredForms(0) mustEqual BenefitsForm(answer = true)
      answeredForms(1) mustEqual HoursForm(answer = true)
      answeredForms(2) mustEqual LivesInGBForm(answer = true)
    }

    "acknowledge that carer is aged 16 or over" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody("answer" -> "true").withSession("connected" -> "claim")
      CarersAllowance.over16Submit(request)

      val claim = Cache.getAs[Claim]("claim").get
      val section: Section = claim.section("s1").get

      section.form("s1.g4") must beLike {
        case Some(f: Over16Form) => f.answer mustEqual true
      }
    }

    "acknowledge that the carer is eligible for allowance" in new WithApplication {
      val claim = Claim().update(BenefitsForm(answer = true))
                         .update(HoursForm(answer = true))
                         .update(LivesInGBForm(answer = true))
                         .update(Over16Form(answer = true))

      Cache.set("claim", claim)

      val result = CarersAllowance.approve(FakeRequest())
      contentAsString(result) must contain("Based on your answers you may be entitled to Carer’s Allowance.")
    }

    "note that the carer is not eligible for allowance" in new WithApplication {
      val request = FakeRequest().withSession("connected" -> "claim")

      val claim = Claim().update(BenefitsForm(answer = true))
                         .update(HoursForm(answer = true))
                         .update(LivesInGBForm(answer = false))
                         .update(Over16Form(answer = true))

      Cache.set("claim", claim)

      val result = CarersAllowance.approve(request)

      contentAsString(result) must contain("Based on your answers you may not be entitled  to  Carer’s Allowance.")
    }
  }
}