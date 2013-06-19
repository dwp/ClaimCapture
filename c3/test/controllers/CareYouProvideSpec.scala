package controllers

import org.specs2.mutable.Specification
import org.specs2.mock.Mockito
import play.api.test.{FakeRequest, WithApplication}
import models.claim._
import play.api.cache.Cache
import models.claim.Section
import models.claim.Claim
import scala.Some

class CareYouProvideSpec extends Specification with Mockito {

  "Care You Provide" should {
    "accept all initial mandatory data" in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)
        .withFormUrlEncodedBody(
        "title" -> "Mr",
        "firstName" -> "John",
        "surname" -> "Doo",
        "dateOfBirth.day" -> "5",
        "dateOfBirth.month" -> "12",
        "dateOfBirth.year" -> "1990",
        "liveAtSameAddress" -> "yes")

      val result = controllers.CareYouProvide.theirPersonalDetailsSubmit(request)
      //      redirectLocation(result) must beSome("/careYouProvide/theirContactDetails")

      val claim = Cache.getAs[Claim](claimKey).get
      val section: Section = claim.section(models.claim.CareYouProvide.id).get

      section.form(TheirPersonalDetails.id) must beLike {
        case Some(f: TheirPersonalDetails) => {
          f.title mustEqual "Mr"
          f.firstName mustEqual "John"
          f.surname mustEqual "Doo"
          f.liveAtSameAddress mustEqual "yes"
        }
      }
    }
  }
}
