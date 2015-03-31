package controllers.s1_2_claim_date

import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import org.specs2.mutable.{Tags, Specification}
import models.DayMonthYear
import models.yesNo.YesNoWithDate

class G1ClaimDateFormSpec  extends Specification with Tags {

  val claimDateDay = 1
  val claimDateMonth = 1
  val claimDateYear = 2014

  val spent35HoursCaringBeforeClaimYes = YesNoWithDate(yes, Some(DayMonthYear(Some(claimDateDay), Some(claimDateMonth), Some(claimDateYear), None, None)))
  val spent35HoursCaringBeforeClaimYesWithNoDate = YesNoWithDate(yes, None)
  val spent35HoursCaringBeforeClaimNo = YesNoWithDate(no, None)

  "Claim Date Form" should {

    "map data into case class" in {
      G1ClaimDate.form.bind(
        Map(
          "dateOfClaim.day" -> claimDateDay.toString,
          "dateOfClaim.month" -> claimDateMonth.toString,
          "dateOfClaim.year" -> claimDateYear.toString,
          "beforeClaimCaring.answer" -> spent35HoursCaringBeforeClaimYes.answer,
          "beforeClaimCaring.date.day" -> claimDateDay.toString,
          "beforeClaimCaring.date.month" -> claimDateMonth.toString,
          "beforeClaimCaring.date.year" -> claimDateYear.toString)).fold(
          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.dateOfClaim must equalTo(DayMonthYear(Some(claimDateDay), Some(claimDateMonth), Some(claimDateYear), None, None))
            f.spent35HoursCaringBeforeClaim must equalTo(spent35HoursCaringBeforeClaimYes)
          })
    }

    "map data into case class when spent35HoursCaringBeforeClaim is no" in {
      G1ClaimDate.form.bind(
        Map(
          "dateOfClaim.day" -> claimDateDay.toString,
          "dateOfClaim.month" -> claimDateMonth.toString,
          "dateOfClaim.year" -> claimDateYear.toString,
          "beforeClaimCaring.answer" -> spent35HoursCaringBeforeClaimNo.answer)).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.dateOfClaim must equalTo(DayMonthYear(Some(claimDateDay), Some(claimDateMonth), Some(claimDateYear), None, None))
          f.spent35HoursCaringBeforeClaim must equalTo(spent35HoursCaringBeforeClaimNo)
        })
    }

    "have 2 mandatory fields" in {
      G1ClaimDate.form.bind(
        Map("" -> "")).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(2)
            formWithErrors.errors(0).message must equalTo(Mappings.errorRequired)
            formWithErrors.errors(1).message must equalTo(Mappings.errorRequired)
          },
          claimDateDetails => "This mapping should not happen." must equalTo("Valid"))
    }

    "have 1 mandatory field when spent35HoursCaringBeforeClaim is yes" in {
      G1ClaimDate.form.bind(
        Map(
          "dateOfClaim.day" -> claimDateDay.toString,
          "dateOfClaim.month" -> claimDateMonth.toString,
          "dateOfClaim.year" -> claimDateYear.toString,
          "beforeClaimCaring.answer" -> spent35HoursCaringBeforeClaimYes.answer)).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(1)
          formWithErrors.errors(0).message must equalTo(Mappings.required)
        },
        claimDateDetails => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid date" in {
      G1ClaimDate.form.bind(
        Map(
          "dateOfClaim.day" -> claimDateDay.toString,
          "dateOfClaim.month" -> claimDateMonth.toString,
          "dateOfClaim.year" -> "12345",
          "beforeClaimCaring.answer" -> spent35HoursCaringBeforeClaimNo.answer)).fold(
          formWithErrors => {
            formWithErrors.errors.head.message must equalTo(Mappings.errorInvalid)
            formWithErrors.errors.length must equalTo(1)
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid date when spent35HoursCaringBeforeClaim is yes" in {
      G1ClaimDate.form.bind(
        Map(
          "dateOfClaim.day" -> claimDateDay.toString,
          "dateOfClaim.month" -> claimDateMonth.toString,
          "dateOfClaim.year" -> claimDateYear.toString,
          "beforeClaimCaring.answer" -> spent35HoursCaringBeforeClaimYes.answer,
          "beforeClaimCaring.date.day" -> claimDateDay.toString,
          "beforeClaimCaring.date.month" -> claimDateMonth.toString,
          "beforeClaimCaring.date.year" -> "aaaa")).fold(
        formWithErrors => {
          formWithErrors.errors.head.message must equalTo(Mappings.errorInvalid)
          formWithErrors.errors.length must equalTo(1)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }

  } section("unit", models.domain.YourClaimDate.id)
}
