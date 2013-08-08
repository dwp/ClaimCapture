package controllers.s9_other_money

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import play.api.cache.Cache
import models.domain._
import models.MultiLineAddress
import models.domain.Claim
import play.api.Play.current

class G4PersonContactDetailsSpec extends Specification with Tags {

  val personContactDetailsInput = Seq("address.lineOne" -> "123 Street", "postcode" -> "PR2 8AE")

  def prepareCache(claimKey: String) = Cache.set(claimKey, Claim().update(new MoneyPaidToSomeoneElseForYou("no")))

  "Other Money - Person Contact Details - Controller" should {
    "present 'Person Contact Details if visible' " in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = controllers.s9_other_money.G4PersonContactDetails.present(request)
      status(result) mustEqual OK
    }

    "redirect 'Person Contact Details if hidden' " in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
      prepareCache(claimKey)
      val result = controllers.s9_other_money.G4PersonContactDetails.present(request)
      status(result) mustEqual SEE_OTHER
    }

    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(personContactDetailsInput: _*)

      val result = controllers.s9_other_money.G4PersonContactDetails.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get

      val section: Section = claim.section(models.domain.OtherMoney)

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

      val result = controllers.s9_other_money.G4PersonContactDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(personContactDetailsInput: _*)

      val result = controllers.s9_other_money.G4PersonContactDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section("unit", models.domain.OtherMoney.id)
}