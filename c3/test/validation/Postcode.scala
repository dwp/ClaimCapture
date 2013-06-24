package validation

import org.specs2.mutable.Specification
import play.api.test.{ FakeRequest, WithApplication }
import play.api.cache.Cache
import play.api.test.Helpers._
import org.specs2.mock.Mockito
import models.Postcode
import play.api.data.Form
import play.api.data.Mapping
import play.api.data.validation.Constraints._
import play.api.data.Forms._
import controllers.Mappings

class PostcodeValidationSpec extends Specification {
  def createPostcodeForm(postcode: String) = Form("postcode" -> Mappings.postcode.verifying(Mappings.validPostcode)).bind(Map(
    "postcode.content" -> postcode))

  "Postcode" should {

    "accept valid input" in {
      createPostcodeForm("PR2 8AE").fold(
        formWithErrors => { "The mapping should not fail." must equalTo("Error") },
        { postcode =>
          postcode.content must equalTo(Some("PR2 8AE"))
        })
    }

    "accept valid input in lowercase" in {
      createPostcodeForm("rm11 1da").fold(
        formWithErrors => { "The mapping should not fail." must equalTo("Error") },
        { postcode =>
          postcode.content must equalTo(Some("rm11 1da"))
        })
    }

    "reject invalid input" in {
      createPostcodeForm("1111 111").fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.postcode"),
        postcode => "This mapping should not happen." must equalTo("Valid"))
    }

  }
}