package integration.s4_careYouProvide

import org.specs2.mutable.Specification
import controllers.CareYouProvide

class G1TheirPersonalDetailsFormSpec extends Specification {

  "Their Personal Details Form" should {
      "reject too long firstName, middleName or surname" in {
        CareYouProvide.theirPersonalDetailsForm.bind(
          Map( "title" -> "Mr",
            "firstName" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS",
            "middleName" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS",
            "surname" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS",
            "dateOfBirth.day" -> "1",
            "dateOfBirth.month" -> "1",
            "dateOfBirth.year" -> "1980",
            "liveAtSameAddress" -> "yes"
          )
        ).fold(
          formWithErrors => {
                            formWithErrors.errors(0).message must equalTo("error.maxLength")
                            formWithErrors.errors(1).message must equalTo("error.maxLength")
                            formWithErrors.errors(2).message must equalTo("error.maxLength")
                            },
          theirPersonalDetails => "This mapping should not happen." must equalTo("Valid")
        )
      }

    "have 5 mandatory fields" in {
      CareYouProvide.theirPersonalDetailsForm.bind(
        Map( "middleName" -> "middle name is optional")
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(5)
          formWithErrors.errors(0).message must equalTo("error.required")
          formWithErrors.errors(1).message must equalTo("error.required")
          formWithErrors.errors(2).message must equalTo("error.required")
          formWithErrors.errors(3).message must equalTo("error.required")
          formWithErrors.errors(4).message must equalTo("error.required")
        },
        theirPersonalDetails => "This mapping should not happen." must equalTo("Valid")
      )
    }
    }


}
