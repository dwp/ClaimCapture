package models

import org.specs2.mutable._
import play.api.data.Form
import play.api.data.Forms._
import controllers.mappings.Mappings._

class NationalityFormSpec extends Specification {
  "Nationality" should {
    "accept valid input " in {
      val n = "British"

      createNationalityForm(n).fold(
        formWithErrors => "The mapping should not fail." must equalTo("Error"),
        f => f must equalTo(Some(n))
      )
    }

    "accept spaces, uppercase and lowercase" in {
      val n = "United States"

      createNationalityForm(n).fold(
        formWithErrors => "The mapping should not fail." must equalTo("Error"),
        f => f must equalTo(Some(n))
      )
    }

    "reject numbers" in {
      val n = "a12345"

      createNationalityForm(n).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.nationality"),
        f => "The f should not happen." must equalTo("Error")
      )
    }

    "reject special character" in {
      val n = "a!@£$%^&*(){}"

      createNationalityForm(n).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.nationality"),
        f => "The f should not happen." must equalTo("Error")
      )
    }
  }

  private def createNationalityForm(nationality: String)
    = Form("nationality" -> optional(play.api.data.Forms.text verifying validNationality)).bind(Map("nationality" -> nationality))
}
