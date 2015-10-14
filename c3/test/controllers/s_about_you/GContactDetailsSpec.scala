package controllers.s_about_you

import controllers.mappings.Mappings
import models.domain.{Claim, ContactDetails, Claiming}
import org.specs2.mutable.{Specification, Tags}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.WithApplication

class GContactDetailsSpec extends Specification with Tags {
  "Contact Details" should {
    "present contact details" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GContactDetails.present(request)
      status(result) mustEqual OK
    }

    "return bad request on invalid data" in new WithApplication with Claiming {
      val request = FakeRequest()

      val result = GContactDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """be added to cached claim upon entering valid data".""" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "address.lineOne" -> "101 Clifton Street",
          "address.lineTwo" -> "Blackpool",
          "postcode" -> "FY1 2RW",
          "howWeContactYou" -> "01772888901",
          "wantsEmailContact" -> Mappings.no)

      val result = GContactDetails.submit(request)

      getClaimFromCache(result).questionGroup(ContactDetails) should beLike {
        case Some(f: ContactDetails) => f.wantsContactEmail shouldEqual Some("no")
      }
    }

    """should be valid submission with no contact number supplied".""" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "address.lineOne" -> "101 Clifton Street",
          "address.lineTwo" -> "Blackpool",
          "postcode" -> "FY1 2RW",
          "wantsEmailContact" -> Mappings.no)

      val result = GContactDetails.submit(request)
      status(result) mustEqual SEE_OTHER
    }

    """return bad request with characters in contact number".""" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "address.lineOne" -> "101 Clifton Street",
          "address.lineTwo" -> "Blackpool",
          "postcode" -> "FY1 2RW",
          "howWeContactYou" -> "hjhdjhj",
          "wantsEmailContact" -> Mappings.no)

      val result = GContactDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """return bad request with less than minimum digits entered".""" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "address.lineOne" -> "101 Clifton Street",
          "address.lineTwo" -> "Blackpool",
          "postcode" -> "FY1 2RW",
          "howWeContactYou" -> "012345",
          "wantsEmailContact" -> Mappings.no)

      val result = GContactDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }

    """return bad request with greater than maximum digits entered".""" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "address.lineOne" -> "101 Clifton Street",
          "address.lineTwo" -> "Blackpool",
          "postcode" -> "FY1 2RW",
          "howWeContactYou" -> "012345678901234567890",
          "wantsEmailContact" -> Mappings.no)

      val result = GContactDetails.submit(request)
      status(result) mustEqual BAD_REQUEST
    }
  } section("unit", models.domain.ContactDetails.id)
}
