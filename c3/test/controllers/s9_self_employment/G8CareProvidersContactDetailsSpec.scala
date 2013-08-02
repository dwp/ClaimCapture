package controllers.s9_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import play.api.cache.Cache
import models.domain.{Claiming, CareProvidersContactDetails, Section, Claim}
import models.{MultiLineAddress, domain}

class G8CareProvidersContactDetailsSpec extends Specification with Tags {
  "Care provider's contact Details Form - Controller" should {
    val addressLineOne = "lineOne"
    val addressLineTwo = "lineTwo"
    val addressLineThree = "lineThree"
    val postcode = "SE1 6EH"
      
    val careProvidersContactDetailsInput = Seq("address.lineOne" -> addressLineOne, 
        "address.lineTwo" -> addressLineTwo,
        "address.lineThree" -> addressLineThree,
        "postcode" -> postcode)

    "present 'Your Partner Contact Details' " in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = G8CareProvidersContactDetails.present(request)
      status(result) mustEqual OK
    }

    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(careProvidersContactDetailsInput: _*)

      val result = G8CareProvidersContactDetails.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.SelfEmployment)

      section.questionGroup(CareProvidersContactDetails) must beLike {
        case Some(y: CareProvidersContactDetails) => {
          y.address must equalTo(Some(MultiLineAddress(Some(addressLineOne), Some(addressLineTwo), Some(addressLineThree))))
          y.postcode must equalTo(Some(postcode))
        }
      }
    }

    "return a bad request after an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("postcode" -> "INVALID")

      val result = G8CareProvidersContactDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(careProvidersContactDetailsInput: _*)

      val result = G8CareProvidersContactDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section "unit"
}