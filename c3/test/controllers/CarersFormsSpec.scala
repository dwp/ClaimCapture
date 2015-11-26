package controllers

import utils.WithApplication
import controllers.mappings.Mappings
import org.specs2.mutable._
import language.reflectiveCalls
import play.api.data.Forms._
import play.api.data.Form
import controllers.CarersForms._

case class Details(v1: String = "", v2: String)

object TestForm {
  val form = Form(mapping(
    "v1" -> carersText(maxLength = 20),
    "v2" -> carersNonEmptyText(minLength = 5)
  )(Details.apply)(Details.unapply))
}

class CarersFormsSpec extends Specification {

  "Carers Forms" should {
    "handle good data" in new WithApplication {
      TestForm.form.bind(
        Map("v1" -> "title",
          "v2" -> "firstName")).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.v1 must equalTo("title")
          f.v2 must equalTo("firstName")
        })
    }

    "handle short data" in new WithApplication {
      TestForm.form.bind(
        Map("v1" -> "title",
          "v2" -> "kjjj")).fold(
        formWithErrors => {
          formWithErrors.errors(0).message must equalTo("error.minLength")
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "handle bad char" in new WithApplication {
      TestForm.form.bind(
        Map("v1" -> ">itle",
          "v2" -> "kjhkhjhjhkjmr`")).fold(
        formWithErrors => {
          formWithErrors.errors(0).message must equalTo(Mappings.errorRestrictedCharacters)
          formWithErrors.errors(1).message must equalTo(Mappings.errorRestrictedCharacters)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
  }

}
