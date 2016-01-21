package controllers.s_about_you

import controllers.mappings.Mappings
import models.domain.{ContactDetails, Claiming}
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.{LightFakeApplication, WithApplication}

class GContactDetailsSpec extends Specification {
  section("unit", models.domain.ContactDetails.id)
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
        case Some(f: ContactDetails) => f.wantsContactEmail shouldEqual "no"
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

    "show old email label when save for later switched off" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "false"))) with Claiming {
      val request = FakeRequest()
      val result = GContactDetails.present(request)
      val bodyText: String = contentAsString(result)
      bodyText must contain("Do you want an email to confirm your application has been received?")
      bodyText must not contain("Have you got an email address?")
      status(result) mustEqual OK
    }

    "show new email label when save for later switched on" in new WithApplication(app = LightFakeApplication(additionalConfiguration = Map("saveForLaterSaveEnabled" -> "true"))) with Claiming {
      val request = FakeRequest()
      val result = GContactDetails.present(request)
      val bodyText: String = contentAsString(result)
      bodyText must not contain("Do you want an email to confirm your application has been received?")
      bodyText must contain("Have you got an email address?")
      status(result) mustEqual OK
    }

    """be added to cached claim upon entering valid data with space in email".""" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "address.lineOne" -> "101 Clifton Street",
          "address.lineTwo" -> "Blackpool",
          "postcode" -> "FY1 2RW",
          "howWeContactYou" -> "01772888901",
          "wantsEmailContact" -> Mappings.yes,
          "mail" -> "j@bt.com ",
          "mailConfirmation" -> "j@bt.com "
        )

      val result = GContactDetails.submit(request)

      getClaimFromCache(result).questionGroup(ContactDetails) should beLike {
        case Some(f: ContactDetails) => f.wantsContactEmail shouldEqual "yes"; f.email shouldEqual Some("j@bt.com")
      }
    }

    """be added to cached claim upon entering valid data with space in postcode".""" in new WithApplication with Claiming {
      val request = FakeRequest()
        .withFormUrlEncodedBody(
          "address.lineOne" -> "101 Clifton Street",
          "address.lineTwo" -> "Blackpool",
          "postcode" -> " FY1   2RW ",
          "howWeContactYou" -> "01772888901",
          "wantsEmailContact" -> Mappings.no
        )

      val result = GContactDetails.submit(request)

      getClaimFromCache(result).questionGroup(ContactDetails) should beLike {
        case Some(f: ContactDetails) => f.postcode shouldEqual Some("FY1 2RW")
      }
    }
  }
  section("unit", models.domain.ContactDetails.id)
}
