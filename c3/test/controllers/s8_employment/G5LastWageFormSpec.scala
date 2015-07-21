package controllers.s8_employment

import controllers.mappings.Mappings
import models.view.CachedClaim
import org.specs2.mutable.{Tags, Specification}
import app.StatutoryPaymentFrequency
import models.{PaymentFrequency, DayMonthYear}


class G5LastWageFormSpec extends Specification with Tags{

  "Employer Details - Your wages" should {

    val jobId = "1"
    val oftenGetPaid = StatutoryPaymentFrequency.Monthly
    val day = "1"
    val month = "1"
    val year = "1980"
    val grossPay = "1234"
    val yes = "yes"
    val whenGetPaid = "Monday"

    "map data to form" in {
      G5LastWage.form(models.domain.Claim(CachedClaim.key)).bind(
        Map(
          "iterationID" -> jobId,
          "oftenGetPaid.frequency" -> oftenGetPaid,
          "whenGetPaid" -> whenGetPaid,
          "lastPaidDate.day" -> day,
          "lastPaidDate.month" -> month,
          "lastPaidDate.year" -> year,
          "grossPay" -> grossPay,
          "sameAmountEachTime" -> yes,
          "employerOwesYouMoney" -> yes
        )
      ).fold(
        formWithErrors => {
          println(formWithErrors.errors)
          "This mapping should not happen." must equalTo("Error")
        },
        f => {
          f.oftenGetPaid must equalTo(PaymentFrequency(oftenGetPaid,None))
          f.whenGetPaid must equalTo(whenGetPaid)
          f.lastPaidDate must equalTo(DayMonthYear(1,1,1980))
          f.grossPay must equalTo(grossPay)
          f.sameAmountEachTime must equalTo(yes)
          f.employerOwesYouMoney must equalTo(Some(yes))
        }
      )

    }

    "have 5 mandatory fields" in {
      G5LastWage.form(models.domain.Claim(CachedClaim.key)).bind(
      Map(
        "iterationID" -> jobId)
      ).fold(
        formWithErrors => {
          formWithErrors.errors.length must equalTo(5)
          formWithErrors.errors(0).message must equalTo(Mappings.errorRequired)
          formWithErrors.errors(1).message must equalTo(Mappings.errorRequired)
          formWithErrors.errors(2).message must equalTo(Mappings.errorRequired)
          formWithErrors.errors(3).message must equalTo(Mappings.errorRequired)
          formWithErrors.errors(4).message must equalTo(Mappings.errorRequired)
        },
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  }

}
