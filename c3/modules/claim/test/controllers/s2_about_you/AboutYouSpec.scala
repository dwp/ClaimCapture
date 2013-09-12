package controllers.s2_about_you

import org.specs2.mutable.{Tags, Specification}
import play.api.test.{FakeRequest, WithApplication}
import play.api.cache.Cache
import play.api.test.Helpers._
import models.domain._
import models.domain.Claim
import controllers.s2_about_you
import models.view.CachedClaim

class AboutYouSpec extends Specification with Tags {
  "About you" should {
    "accept all initial mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(
        "firstName" -> "Scooby",
        "title" -> "Mr",
        "surname" -> "Doo",
        "nationality" -> "US",
        "dateOfBirth.day" -> "5",
        "dateOfBirth.month" -> "12",
        "dateOfBirth.year" -> "1990",
        "maritalStatus" -> "s",
        "alwaysLivedUK" -> "yes")

      val result = G1YourDetails.submit(request)
      redirectLocation(result) must beSome("/about-you/contact-details")

      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup(YourDetails) must beLike { case Some(y: YourDetails) => y.firstName mustEqual "Scooby" }
    }

    "highlight missing mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody("firstName" -> "Scooby", "action" -> "next")

      val result = G1YourDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "highlight invalid date" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(
        "firstName" -> "Scooby",
        "title" -> "Mr",
        "surname" -> "Doo",
        "nationality" -> "US",
        "dateOfBirth.day" -> "5",
        "dateOfBirth.month" -> "12",
        "dateOfBirth.year" -> "199000001",
        "maritalStatus" -> "s",
        "alwaysLivedUK" -> "yes")

      val result = G1YourDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "not complain about a valid NI" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(
        "firstName" -> "Scooby",
        "title" -> "Mr",
        "surname" -> "Doo",
        "nationality" -> "US",

        "nationalInsuranceNumber.ni1" -> "JW",
        "nationalInsuranceNumber.ni2" -> "12",
        "nationalInsuranceNumber.ni3" -> "34",
        "nationalInsuranceNumber.ni4" -> "56",
        "nationalInsuranceNumber.ni5" -> "C",

        "dateOfBirth.day" -> "5",
        "dateOfBirth.month" -> "12",
        "dateOfBirth.year" -> "1990",
        "maritalStatus" -> "s",
        "alwaysLivedUK" -> "yes")

      val result = G1YourDetails.submit(request)
      status(result) mustNotEqual BAD_REQUEST
    }

    "complain about an invalid NI" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(
        "firstName" -> "Scooby",
        "title" -> "Mr",
        "surname" -> "Doo",

        // Missing first field 
        "nationalInsuranceNumber.ni2" -> "12",
        "nationalInsuranceNumber.ni3" -> "34",
        "nationalInsuranceNumber.ni4" -> "56",
        "nationalInsuranceNumber.ni5" -> "C",

        "dateOfBirth.day" -> "5",
        "dateOfBirth.month" -> "12",
        "dateOfBirth.year" -> "1990",
        "nationality" -> "US",
        "maritalStatus" -> "s",
        "alwaysLivedUK" -> "yes"
      )

      val result = G1YourDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "not complain about a valid Postcode" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(
        "address.lineOne" -> "123 Street",
        "postcode" -> "PR2 8AE")

      val result = G2ContactDetails.submit(request)
      status(result) mustNotEqual BAD_REQUEST
    }

    "complain about an invalid Postcode" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(
        "address.lineOne" -> "123 Street",
        "postcode" -> "1234567890")

      val result = G2ContactDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "complain about an empty Address" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)
        .withFormUrlEncodedBody(
        "address.lineOne" -> "",
        "address.lineTwo" -> "",
        "address.lineThree" -> "")

      val result = G2ContactDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "present completion" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val result = s2_about_you.AboutYou.completed(request)
      status(result) mustEqual OK
    }

    """present first "about you" page upon completing with missing forms""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val claim = Claim()().update(mockQuestionGroup[YourDetails](YourDetails))

      Cache.set(claimKey, claim)

      val result = s2_about_you.AboutYou.completedSubmit(request)
      redirectLocation(result) must beSome("/about-you/your-details")
    }

    "continue to partner/spouse upon section completion when all forms are done" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val moreAboutYou = mockQuestionGroup[MoreAboutYou](MoreAboutYou)
      moreAboutYou.hadPartnerSinceClaimDate returns "yes"

      val claim = Claim()()
        .update(mockQuestionGroup[YourDetails](YourDetails))
        .update(mockQuestionGroup[ContactDetails](ContactDetails))
        .update(mockQuestionGroup[ClaimDate](ClaimDate))
        .update(moreAboutYou)
        .update(mockQuestionGroup[Employment](Employment))

      Cache.set(claimKey, claim)

      val result = s2_about_you.AboutYou.completedSubmit(request)
      redirectLocation(result) must beSome("/your-partner/personal-details")
    }

    "continue to partner/spouse upon section completion when all forms are done including 'About Your Time Outside The UK'" in new WithApplication with Claiming {
      val request = FakeRequest().withSession(CachedClaim.key -> claimKey)

      val yourDetails = mockQuestionGroup[YourDetails](YourDetails)
      yourDetails.alwaysLivedUK returns "no"

      val moreAboutYou = mockQuestionGroup[MoreAboutYou](MoreAboutYou)
      moreAboutYou.hadPartnerSinceClaimDate returns "yes"

      val claim = Claim()()
        .update(yourDetails)
        .update(mockQuestionGroup[ContactDetails](ContactDetails))
        .update(mockQuestionGroup[TimeOutsideUK](TimeOutsideUK))
        .update(mockQuestionGroup[ClaimDate](ClaimDate))
        .update(moreAboutYou)
        .update(mockQuestionGroup[Employment](Employment))

      Cache.set(claimKey, claim)

      val result = s2_about_you.AboutYou.completedSubmit(request)
      redirectLocation(result) must beSome("/your-partner/personal-details")
    }
  } section("unit", models.domain.AboutYou.id)
}