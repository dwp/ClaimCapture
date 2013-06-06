package controllers

import org.specs2.mutable.Specification
import play.api.test.{WithApplication, FakeRequest}
import play.api.test.Helpers._

import play.api.cache.Cache
import models._
import models.BenefitsForm
import models.HoursForm
import models.Section
import models.Claim
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

      section.form("s1.q1") must beLike {
        case Some(f: BenefitsForm) => f.answer mustEqual true
      }
    }

    "acknowledge that the person looks after does not get one of the required benefits " in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody("answer" -> "false").withSession("connected" -> "claim")
      CarersAllowance.benefitsSubmit(request)

      val claim = Cache.getAs[Claim]("claim").get
      val section: Section = claim.section("s1").get

      section.form("s1.q1") must beLike {
        case Some(f: BenefitsForm) => f.answer mustEqual false
      }
    }

    "present the hours form" in new WithApplication {
      val request = FakeRequest().withSession("connected" -> "claim")

      val claim = Claim().update(BenefitsForm(true))
      Cache.set("claim", claim)

      val result = CarersAllowance.hours(request)

      status(result) mustEqual OK

      val answeredForms = claim.answeredFormsForSection(BenefitsForm().section).dropWhile(_.id != BenefitsForm().id)
      answeredForms(0) mustEqual BenefitsForm(true)
    }

    "acknowledge that you spend 35 hours or more each week caring for the person you look after" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody("answer" -> "true").withSession("connected" -> "claim")
      CarersAllowance.hoursSubmit(request)
      val claim = Cache.getAs[Claim]("claim").get
      val section: Section = claim.section("s1").get

      section.form("s1.q2") must beLike {
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

      section.form("s1.q2") must beLike {
        case Some(f: HoursForm) => f.answer mustEqual true
      }
    }

    "acknowledge that carer lives in Great Britain" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody("answer" -> "true").withSession("connected" -> "claim")
      CarersAllowance.livesInGBSubmit(request)

      val claim = Cache.getAs[Claim]("claim").get
      val section: Section = claim.section("s1").get

      section.form("s1.q3") must beLike {
        case Some(f: LivesInGBForm) => f.answer mustEqual true
      }

    }
  }
}