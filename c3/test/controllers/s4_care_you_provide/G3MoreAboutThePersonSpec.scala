package controllers.s4_care_you_provide

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import play.api.test.{WithApplication, FakeRequest}
import play.api.cache.Cache
import models.domain.{Claiming, MoreAboutThePerson, Claim, Section}
import models.domain
import play.api.test.Helpers._

class G3MoreAboutThePersonSpec extends Specification with Mockito {

  val moreAboutThePersonInput = Seq("relationship" -> "father", "armedForcesPayment" -> "yes", "claimedAllowanceBefore" -> "yes")

  "More About The Person - Controller" should {

    "present 'More About The Person' " in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.s4_care_you_provide.G3MoreAboutThePerson.present(request)
      status(result) mustEqual OK
    }


    "add more about the person details to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(moreAboutThePersonInput: _*)

      val result = controllers.s4_care_you_provide.G3MoreAboutThePerson.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.CareYouProvide.id)

      section.questionGroup(MoreAboutThePerson) must beLike {
        case Some(f: MoreAboutThePerson) => {
          f.relationship mustEqual "father"
          f.armedForcesPayment mustEqual Some("yes")
          f.claimedAllowanceBefore mustEqual "yes"
        }
      }
    }

    "return a BadRequest on an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("relationship" -> "")

      val result = controllers.s4_care_you_provide.G3MoreAboutThePerson.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(moreAboutThePersonInput: _*)

      val result = controllers.s4_care_you_provide.G3MoreAboutThePerson.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  }
}
