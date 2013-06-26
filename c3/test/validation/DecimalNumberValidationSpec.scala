package validation

import org.specs2.mutable.Specification
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._

class DecimalNumberValidationSpec extends Specification {

  def createDecimalNumberForm(decimalNumber: String) = Form("decimalNumber" -> optional(play.api.data.Forms.text verifying (validDecimalNumber))).bind(Map(
    "decimalNumber" -> decimalNumber))

  "Decimal Number" should {

    "accept single decimal input" in {
      val value = "500.5"
      createDecimalNumberForm(value).fold(
        formWithErrors => "The mapping should not happen." must equalTo("Error"),
        decimal => decimal must equalTo(Some(value))
      )
    }

    "accept number with no decimal" in {
      val value = "500"
      createDecimalNumberForm(value).fold(
        formWithErrors => "The mapping should not happen." must equalTo("Error"),
        decimal => decimal must equalTo(Some(value))
      )
    }

    "reject characters" in {
      val value = "abc"
      createDecimalNumberForm(value).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("decimal.invalid"),
        decimal => "The mapping should not happen." must equalTo("Error")
      )
    }

    "reject number with dot and no decimal" in {
      val value = "500."
      createDecimalNumberForm(value).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("decimal.invalid"),
        decimal => "The mapping should not happen." must equalTo("Error")
      )
    }

    "reject two decimal input" in {
      val value = "500.50"
      createDecimalNumberForm(value).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("decimal.invalid"),
        decimal => "The mapping should not happen." must equalTo("Error")
      )
    }

    "reject three decimal input" in {
      val value = "500.500"
      createDecimalNumberForm(value).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("decimal.invalid"),
        decimal => "The mapping should not happen." must equalTo("Error")
      )
    }
  }
}
