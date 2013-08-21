package controllers.s8_self_employment

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import play.api.cache.Cache
import models.domain.{Claiming, ChildcareProvidersContactDetails, Section, Claim}
import models.{MultiLineAddress, domain}
import models.view.CachedClaim

class G6ChildcareProvidersContactDetailsSpec extends Specification with Tags {
  "Childcare provider's contact Details Form - Controller" should {
    val addressLineOne = "lineOne"
    val addressLineTwo = "lineTwo"
    val addressLineThree = "lineThree"
    val postcode = "SE1 6EH"
      
    val childcareProvidersContactDetailsInput = Seq("address.lineOne" -> addressLineOne, 
        "address.lineTwo" -> addressLineTwo,
        "address.lineThree" -> addressLineThree,
        "postcode" -> postcode)

    "present 'Your Partner Contact Details' " in new WithApplication with Claiming {

      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody("doYouPayToPensionScheme.answer" -> "no", "doYouPayToLookAfterYourChildren" -> "yes","didYouPayToLookAfterThePersonYouCaredFor" -> "yes")

      val result = G4SelfEmploymentPensionsAndExpenses.submit(request)

      val request2 = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)

      val result2 = G6ChildcareProvidersContactDetails.present(request)
      status(result2) mustEqual OK

    }

    "add submitted form to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody(childcareProvidersContactDetailsInput: _*)

      val result = G6ChildcareProvidersContactDetails.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(domain.SelfEmployment)

      section.questionGroup(ChildcareProvidersContactDetails) must beLike {
        case Some(y: ChildcareProvidersContactDetails) => {
          y.address must equalTo(Some(MultiLineAddress(Some(addressLineOne), Some(addressLineTwo), Some(addressLineThree))))
          y.postcode must equalTo(Some(postcode))
        }
      }
    }

    "return a bad request after an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody("postcode" -> "INVALID")

      val result = G6ChildcareProvidersContactDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.claimKey -> claimKey)
        .withFormUrlEncodedBody(childcareProvidersContactDetailsInput: _*)

      val result = G6ChildcareProvidersContactDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }
  } section("unit", models.domain.SelfEmployment.id)
}