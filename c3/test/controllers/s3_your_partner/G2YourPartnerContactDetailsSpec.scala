package controllers.s3_your_partner

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import play.api.cache.Cache
import models.domain.{Claiming, YourPartnerContactDetails, Section, Claim}
import models.{MultiLineAddress, domain}

class G2YourPartnerContactDetailsSpec extends Specification with Tags {

  val yourPartnerContactDetailsInput = Seq("address.lineOne" -> "123 Street", "postcode" -> "PR2 8AE")

  "Your Partner Contact Details - Controller" should {
    "present 'Your Partner Contact Details' " in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G2YourPartnerContactDetails.present(request)
      status(result) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(yourPartnerContactDetailsInput: _*)

      val result = G2YourPartnerContactDetails.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.YourPartner)

      section.questionGroup(YourPartnerContactDetails) must beLike {
        case Some(y: YourPartnerContactDetails) => {
          y.address must equalTo(Some(MultiLineAddress(Some("123 Street"), None, None)))
          y.postcode must equalTo(Some("PR2 8AE"))
        }
      }
    }

    "return a bad request after an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("postcode" -> "INVALID")

      val result = G2YourPartnerContactDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(yourPartnerContactDetailsInput: _*)

      val result = G2YourPartnerContactDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section "unit"
}