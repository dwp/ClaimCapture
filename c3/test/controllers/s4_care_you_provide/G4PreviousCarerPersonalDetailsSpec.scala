package controllers.s4_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import org.specs2.mock.Mockito
import play.api.test.{WithApplication, FakeRequest}
import play.api.cache.Cache
import models.domain.{Claiming, PreviousCarerPersonalDetails, Claim}
import play.api.test.Helpers._

class G4PreviousCarerPersonalDetailsSpec extends Specification with Mockito with Tags {
  val data = Seq("firstName" -> "Rip", "middleName" -> "Van",  "surname" -> "Winkle",
                 "dateOfBirth.day" -> "1", "dateOfBirth.month" -> "1", "dateOfBirth.year" -> "1980")

  "Previous Carer Personal Details - Controller" should {

    "add previous carer personal details to the cached claim" in new WithApplication with Claiming {
      val filteredData = data filter { case (k, v) => k == "firstName" || k == "surname" }

      val request = FakeRequest().withSession(models.view.CachedClaim.CLAIM_KEY -> claimKey).withFormUrlEncodedBody(filteredData: _*)

      val result = G4PreviousCarerPersonalDetails.submit(request)
      val claim = Cache.getAs[Claim](claimKey).get

      claim.questionGroup[PreviousCarerPersonalDetails] should beSome(PreviousCarerPersonalDetails(firstName = "Rip", surname = "Winkle"))

      status(result) mustEqual SEE_OTHER
    }

    "return a BadRequest when missing mandatory data" in new WithApplication with Claiming {
      val missingData = data filterNot { case (k, v) => k == "firstName" }

      val request = FakeRequest().withSession(models.view.CachedClaim.CLAIM_KEY -> claimKey).withFormUrlEncodedBody(missingData: _*)

      val result = G4PreviousCarerPersonalDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "return a BadRequest on an invalid submission" in new WithApplication with Claiming {
      val badBirthdayData = data map { case (k, v) => if (k == "dateOfBirth.day") "dateOfBirth.day" -> "INVALID" else k -> v }

      val request = FakeRequest().withSession(models.view.CachedClaim.CLAIM_KEY -> claimKey).withFormUrlEncodedBody(badBirthdayData: _*)

      val result = G4PreviousCarerPersonalDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }
  } section("unit", models.domain.CareYouProvide.id)
}