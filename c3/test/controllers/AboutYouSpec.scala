package controllers

import org.specs2.mutable.Specification
import play.api.test.{FakeRequest, WithApplication}
import play.api.cache.Cache
import models.claim.{YourDetails, Section, Claim}
import play.api.test.Helpers._

class AboutYouSpec extends Specification {
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

    """present first "benefits" page upon unexpected forms""" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = AboutYou.completed(request)
      status(result) mustEqual BAD_REQUEST
    }

    "continue to partner/spouse upon section completion" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      val result = AboutYou.completedSubmit(request)
      redirectLocation(result) must beSome("/yourpartner/yourpartner")
    }
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
}