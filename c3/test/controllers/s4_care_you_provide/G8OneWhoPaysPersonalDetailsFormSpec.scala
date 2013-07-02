package controllers.s4_care_you_provide

import org.specs2.mutable.Specification
import models.DayMonthYear
import scala.Some

class G8OneWhoPaysPersonalDetailsFormSpec extends Specification {

  "One Who Pays Personal Details Form" should {

    "map data into case class" in {
      G8OneWhoPaysPersonalDetails.form.bind(
        Map("organisation" -> "DWP",
          "title" -> "mr",
          "firstName" -> "Ronald",
          "middleName" -> "Mc",
          "surname" -> "Donald",
          "amount" -> "30",
          "startDatePayment.day" -> "3",
          "startDatePayment.month" -> "4",
          "startDatePayment.year" -> "1980"
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.organisation must equalTo(Some("DWP"))
          f.title must equalTo(Some("mr"))
          f.firstName must equalTo(Some("Ronald"))
          f.middleName must equalTo(Some("Mc"))
          f.surname must equalTo(Some("Donald"))
          f.amount must equalTo(Some("30"))
          f.startDatePayment must equalTo(Some(DayMonthYear(Some(3), Some(4), Some(1980), None, None)))
        }
      )
    }

    "reject organisation name of over 100 characters" in {
      G8OneWhoPaysPersonalDetails.form.bind(
        Map("organisation" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.maxLength"),
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject first name of over 60 characters" in {
      G8OneWhoPaysPersonalDetails.form.bind(
        Map("firstName" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.maxLength"),
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject middle name of over 60 characters" in {
      G8OneWhoPaysPersonalDetails.form.bind(
        Map("middleName" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.maxLength"),
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject surname of over 60 characters" in {
      G8OneWhoPaysPersonalDetails.form.bind(
        Map("middleName" -> "CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS,CHARACTERS")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.maxLength"),
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject two decimal amount" in {
      G8OneWhoPaysPersonalDetails.form.bind(
        Map("amount" -> "500.50")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("decimal.invalid"),
        f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid date" in {
      G8OneWhoPaysPersonalDetails.form.bind(
        Map("startDatePayment.year" -> "12345")).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.invalid"),
        f => "This mapping should not happen." must equalTo("Valid"))
    }
  }
}