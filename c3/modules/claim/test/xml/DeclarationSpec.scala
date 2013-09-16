package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain.{YourDetails, Claim}

class DeclarationSpec extends Specification with Tags {

  "Declaration" should {

    "generate claimer full name in declaration text when" in {
      "first name and surname are present" in {
        val personalDetails = YourDetails(firstName = "firstName", surname="surname")
        val fullName = Declaration.fullName(Claim().update(personalDetails).asInstanceOf[Claim])

        fullName mustEqual "firstName surname"
      }

      "first name, middle name and surname are present" in {
        val personalDetails = YourDetails(firstName = "firstName", middleName=Some("middleName"), surname="surname")
        val fullName = Declaration.fullName(Claim().update(personalDetails).asInstanceOf[Claim])

        fullName mustEqual "firstName middleName surname"
      }
    }
  } section "unit"
}
