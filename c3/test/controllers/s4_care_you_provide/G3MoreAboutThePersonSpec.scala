package controllers.s4_care_you_provide

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import play.api.test.{WithApplication, FakeRequest}
import models.view.Claiming
import play.api.cache.Cache
import models.domain.{MoreAboutThePerson, Claim}
import models.domain
import play.api.test.Helpers._
import models.domain.Section
import scala.Some

class G3MoreAboutThePersonSpec extends Specification with Mockito {

  val moreAboutThePersonInput = Seq("relationship" -> "father", "armedForcesPayment" -> "yes", "claimedAllowanceBefore" -> "yes")

  "More About The Person - Controller" should {

    "add more about the person details to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(moreAboutThePersonInput: _*)

      val result = controllers.CareYouProvide.moreAboutThePersonSubmit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.CareYouProvide.id).get

      section.questionGroup(MoreAboutThePerson.id) must beLike {
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

      val result = controllers.CareYouProvide.moreAboutThePersonSubmit(request)
      status(result) mustEqual BAD_REQUEST
    }

      "return a Redirect on a valid submission" in new WithApplication with Claiming {
        val request = FakeRequest().withSession("connected" -> claimKey)
          .withFormUrlEncodedBody(moreAboutThePersonInput: _*)

        val result = controllers.CareYouProvide.moreAboutThePersonSubmit(request)
        status(result) mustEqual SEE_OTHER
      }
  }
}
