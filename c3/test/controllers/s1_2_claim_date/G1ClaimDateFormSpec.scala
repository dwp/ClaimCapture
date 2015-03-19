package controllers.s1_2_claim_date

import controllers.mappings.Mappings
import org.specs2.mutable.{Tags, Specification}
import models.DayMonthYear

class G1ClaimDateFormSpec  extends Specification with Tags {

  val claimDateDay = 1
  val claimDateMonth = 1
  val claimDateYear = 2014

  "Claim Date Form" should {

    "map data into case class" in {
      G1ClaimDate.form.bind(
        Map(
          "dateOfClaim.day" -> claimDateDay.toString,
          "dateOfClaim.month" -> claimDateMonth.toString,
          "dateOfClaim.year" -> claimDateYear.toString)).fold(

          formWithErrors => "This mapping should not happen." must equalTo("Error"),
          f => {
            f.dateOfClaim must equalTo(DayMonthYear(Some(claimDateDay), Some(claimDateMonth), Some(claimDateYear), None, None))

          })
    }

    "have 1 mandatory field" in {
      G1ClaimDate.form.bind(
        Map("" -> "")).fold(
          formWithErrors => {
            formWithErrors.errors.length must equalTo(1)
            formWithErrors.errors(0).message must equalTo(Mappings.errorRequired)
          },
          theirPersonalDetails => "This mapping should not happen." must equalTo("Valid"))
    }

    "reject invalid date" in {
      G1ClaimDate.form.bind(
        Map(
          "dateOfClaim.day" -> claimDateDay.toString,
          "dateOfClaim.month" -> claimDateMonth.toString,
          "dateOfClaim.year" -> "12345")).fold(
          formWithErrors => {
            formWithErrors.errors.head.message must equalTo(Mappings.errorInvalid)
            formWithErrors.errors.length must equalTo(1)
          },
          f => "This mapping should not happen." must equalTo("Valid"))
    }
  } section("unit", models.domain.YourClaimDate.id)
}
