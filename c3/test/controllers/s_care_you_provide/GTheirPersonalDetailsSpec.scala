package controllers.s_care_you_provide

import org.specs2.mutable.{Tags, Specification}
import play.api.test.FakeRequest
import play.api.cache.Cache
import models.domain._
import models.{DayMonthYear, domain}
import play.api.test.Helpers._
import models.domain.Claim
import models.view.CachedClaim
import utils.WithApplication
import utils.pageobjects.s_care_you_provide.GMoreAboutTheCarePage

class GTheirPersonalDetailsSpec extends Specification with Tags {

  val theirPersonalDetailsInput = Seq("relationship" -> "father", "title" -> "Mr", "firstName" -> "John", "surname" -> "Doo",
    "dateOfBirth.day" -> "5", "dateOfBirth.month" -> "12", "dateOfBirth.year" -> "1990", "armedForcesPayment" -> "yes")

  "Their Personal Details - Controller" should {

    "present 'Their Personal Details'." in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GTheirPersonalDetails.present(request)
      status(result) mustEqual OK
    }

    "add 'Their Personal Details' to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody((theirPersonalDetailsInput ++ Map( "theirAddress.answer" -> "yes")): _*)

      val result = GTheirPersonalDetails.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(domain.CareYouProvide)

      section.questionGroup(TheirPersonalDetails) must beLike {
        case Some(t: TheirPersonalDetails) => {
          t.relationship mustEqual "father"
          t.title mustEqual "Mr"
          t.firstName mustEqual "John"
          t.surname mustEqual "Doo"
          t.dateOfBirth mustEqual DayMonthYear(Some(5), Some(12), Some(1990), None, None)
          t.theirAddress.answer mustEqual "yes"
        }
      }
    }

    "add 'Their Personal Details - caree address' to the cached claim" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody((theirPersonalDetailsInput ++ Map( "theirAddress.answer" -> "no", "theirAddress.address.lineOne" -> "123 Street",
        "theirAddress.address.lineTwo" -> "Preston",
        "theirAddress.postCode" -> "PR2 8AE")): _*)

      val result = GTheirPersonalDetails.submit(request)
      val claim = getClaimFromCache(result)
      val section: Section = claim.section(domain.CareYouProvide)

      section.questionGroup(TheirPersonalDetails) must beLike {
        case Some(t: TheirPersonalDetails) => {
          t.theirAddress.answer mustEqual "no"
          t.theirAddress.address.get.lineOne mustEqual Some("123 Street")
          t.theirAddress.postCode mustEqual Some("PR2 8AE")
        }
      }
    }

    "return a bad request after an invalid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody("title" -> "Mr")

      val result = GTheirPersonalDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "return a bad request after an invalid address submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody( "theirAddress.postCode" -> "INVALID")

      val result = GTheirPersonalDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    "redirect to the next page after a valid submission" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(theirPersonalDetailsInput++ Map( "theirAddress.answer" -> "yes"): _*)

      val result = GTheirPersonalDetails.submit(request)
      redirectLocation(result) must beSome(GMoreAboutTheCarePage.url)
    }
  } section("unit", models.domain.CareYouProvide.id)
}