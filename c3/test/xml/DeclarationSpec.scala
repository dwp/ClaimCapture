package xml

import org.specs2.mutable.{Tags, Specification}
import models.domain._
import models.yesNo.YesNoWithText
import play.test.WithApplication
import scala.Some

class DeclarationSpec extends Specification with Tags {

  "Declaration" should {

    "generate claimer full name in declaration text when" in {
      "first name and surname are present" in {
        val personalDetails = TheirPersonalDetails(firstName = "firstName", surname="surname")
        val fullName = Declaration.fullName(Claim().update(personalDetails))

        fullName mustEqual "firstName surname"
      }

      "first name, middle name and surname are present" in {
        val personalDetails = TheirPersonalDetails(firstName = "firstName", middleName=Some("middleName"), surname="surname")
        val fullName = Declaration.fullName(Claim().update(personalDetails))

        fullName mustEqual "firstName middleName surname"
      }

      "declaration and disclaimer are present" in {
        val personalDetails = TheirPersonalDetails(firstName = "firstName", middleName=Some("middleName"), surname="surname")
        val consent = Consent(YesNoWithText(answer = "yes", text = None),YesNoWithText(answer = "yes", text = None))
        val additionalInfo = AdditionalInfo()
        val claim = Claim() + personalDetails + consent + additionalInfo
        val xml = Declaration.xml(claim)
        xml.text must contain("declaration.1.pdf")
        xml.text must contain("disclaimer.7")
      }
    }
  } section "unit"
}
