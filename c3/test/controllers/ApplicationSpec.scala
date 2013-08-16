package controllers

import org.specs2.mutable.Specification
import play.api.test.{FakeRequest, WithApplication}
import models.domain.Claiming
import controllers.s2_about_you.{G1YourDetails, G2ContactDetails}
import play.api.test.Helpers._

class ApplicationSpec extends Specification {
  "About you" should {
    "go from 'contact details' to the previous 'your details'." in new WithApplication with Claiming {
      val request = FakeRequest().withSession("connected" -> claimKey)

      G1YourDetails.present(request)

      val result = G1YourDetails.submit(request.withFormUrlEncodedBody(
        "firstName" -> "Scooby",
        "title" -> "Mr",
        "surname" -> "Doo",
        "nationality" -> "US",
        "dateOfBirth.day" -> "5",
        "dateOfBirth.month" -> "12",
        "dateOfBirth.year" -> "1990",
        "maritalStatus" -> "s",
        "alwaysLivedUK" -> "yes"))

      redirectLocation(result) must beSome("/about-you/contact-details")

      Application.previous(request)


    }
  }
}