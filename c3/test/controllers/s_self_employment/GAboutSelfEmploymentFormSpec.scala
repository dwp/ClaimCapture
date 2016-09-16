package controllers.s_self_employment

import controllers.mappings.Mappings
import org.specs2.mutable._
import utils.WithApplication

class GAboutSelfEmploymentFormSpec extends Specification {
  section("unit", models.domain.SelfEmployment.id)
  "About Self Employment - About Self Employment Form" should {
    val typeOfWork = "gardener"

    "map data into case class" in new WithApplication {
      GSelfEmploymentDates.form.bind(
        Map(
          "typeOfWork" -> typeOfWork,
          "stillSelfEmployed" -> "yes",
          "moreThanYearAgo" -> "yes",
          "haveAccounts" -> "yes")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.typeOfWork mustEqual(typeOfWork)
          f.stillSelfEmployed mustEqual("yes")
          f.moreThanYearAgo mustEqual("yes")
          f.haveAccounts mustEqual(Some("yes"))
        }
      )
    }

    "reject if typeOfWork is not filled" in new WithApplication {
      GSelfEmploymentDates.form.bind(
        Map(
          "typeOfWork" -> "",
          "stillSelfEmployed" -> "yes",
          "moreThanYearAgo" -> "yes",
          "haveAccounts" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.errorRequired),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if stillSelfEmployed is not filled" in new WithApplication {
      GSelfEmploymentDates.form.bind(
        Map(
          "typeOfWork" -> typeOfWork,
          "stillSelfEmployed" -> "",
          "moreThanYearAgo" -> "yes",
          "haveAccounts" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo(Mappings.errorRequired),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  }
  section("unit", models.domain.SelfEmployment.id)
}
