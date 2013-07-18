package controllers.s8_other_money

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import play.api.cache.Cache
import models.MultiLineAddress
import scala.Some
import models.domain.{Section, PersonContactDetails, Claim, Claiming}

class G4PersonContactDetailsSpec extends Specification with Tags {

  val personContactDetailsInput = Seq("address.lineOne" -> "123 Street", "postcode" -> "PR2 8AE")

  "Other Money - Person Contact Details - Controller" should {
    "present 'Person Contact Details' " in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.s8_other_money.G4PersonContactDetails.present(request)
      status(result) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(personContactDetailsInput: _*)

      val result = controllers.s8_other_money.G4PersonContactDetails.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(models.domain.OtherMoney.id)

      section.questionGroup(PersonContactDetails) must beLike {
        case Some(f: PersonContactDetails) => {
          f.address must equalTo(Some(MultiLineAddress(Some("123 Street"), None, None)))
          f.postcode must equalTo(Some("PR2 8AE"))
        }
      }
    }

    "return a bad request after an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("postcode" -> "INVALID")

      val result = controllers.s8_other_money.G4PersonContactDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(personContactDetailsInput: _*)

      val result = controllers.s8_other_money.G4PersonContactDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section "unit"

}
