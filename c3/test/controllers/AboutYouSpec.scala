package controllers

import org.specs2.mutable.Specification
import play.api.test.{FakeRequest, WithApplication}
import play.api.cache.Cache
import models.claim._
import play.api.test.Helpers._
import org.specs2.mock.Mockito
import models.claim.Section
import models.claim.Claim
import scala.Some

class AboutYouSpec extends Specification with Mockito {
  "About you" should {
    "accept all initial mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "firstName" -> "Scooby",
        "title" -> "Mr",
        "surname" -> "Doo",
        "nationality" -> "US",
        "dateOfBirth.day" -> "5",
        "dateOfBirth.month" -> "12",
        "dateOfBirth.year" -> "1990",
        "maritalStatus" -> "Single",
        "alwaysLivedUK" -> "yes")

      val result = AboutYou.yourDetailsSubmit(request)
      redirectLocation(result) must beSome("/aboutyou/contactDetails")

      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(models.claim.AboutYou.id).get

      section.form(YourDetails.id) must beLike {
        case Some(f: YourDetails) => f.firstName mustEqual "Scooby"
      }
    }

    "highlight missing mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody("firstName" -> "Scooby", "action" -> "next")

      val result = AboutYou.yourDetailsSubmit(request)
      status(result) mustEqual BAD_REQUEST

      val claim = Cache.getAs[Claim](claimKey).get
      claim.section(models.claim.AboutYou.id) must beNone
    }

    "not complain about a valid NI" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "firstName" -> "Scooby",
        "title" -> "Mr",
        "surname" -> "Doo",
        "nationality" -> "US",
        "dateOfBirth.day" -> "5",
        "dateOfBirth.month" -> "12",
        "dateOfBirth.year" -> "1990",
        "maritalStatus" -> "Single",
        "alwaysLivedUK" -> "yes",
        "nationalInsuranceNumber" -> "aB123456A")

      val result = AboutYou.yourDetailsSubmit(request)
      status(result) mustNotEqual BAD_REQUEST
    }

    "complain about an invalid NI" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "firstName" -> "Scooby",
        "title" -> "Mr",
        "surname" -> "Doo",
        "nationality" -> "US",
        "dateOfBirth.day" -> "5",
        "dateOfBirth.month" -> "12",
        "dateOfBirth.year" -> "1990",
        "maritalStatus" -> "Single",
        "alwaysLivedUK" -> "yes",
        "nationalInsuranceNumber" -> "AB123456")

      val result = AboutYou.yourDetailsSubmit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "not complain about a valid Postcode" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "address.lineOne" -> "123 Street",
        "postcode" -> "PR2 8AE")

      val result = AboutYou.contactDetailsSubmit(request)
      status(result) mustNotEqual BAD_REQUEST
    }

    "complain about an invalid Postcode" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "address.lineOne" -> "123 Street",
        "postcode" -> "PR2")

      val result = AboutYou.contactDetailsSubmit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "complain about an empty Address" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "address.lineOne" -> "",
        "address.lineTwo" -> "",
        "address.lineThree" -> "")

      val result = AboutYou.contactDetailsSubmit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """present completion""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = AboutYou.completed(request)
      status(result) mustEqual OK
    }

    """present first "about you" page upon completing with missing forms""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val claim = Claim().update(mockForm[YourDetails](YourDetails.id))

      Cache.set(claimKey, claim)

      val result = AboutYou.completedSubmit(request)
      redirectLocation(result) must beSome("/aboutyou/yourDetails")
    }

    "continue to partner/spouse upon section completion" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val claim = Claim()
        .update(mockForm[YourDetails](YourDetails.id))
        .update(mockForm[ContactDetails](ContactDetails.id))
        .update(mockForm[ClaimDate](ClaimDate.id))
        .update(mockForm[MoreAboutYou](MoreAboutYou.id))
        .update(mockForm[Employment](Employment.id))
        .update(mockForm[PropertyAndRent](PropertyAndRent.id))

      Cache.set(claimKey, claim)

      val result = AboutYou.completedSubmit(request)
      redirectLocation(result) must beSome("/yourpartner/yourpartner")
    }
  }
}