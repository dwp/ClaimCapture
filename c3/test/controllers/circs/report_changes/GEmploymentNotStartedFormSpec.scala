package controllers.circs.report_changes

import utils.WithApplication
import org.specs2.mutable._
import models.DayMonthYear

class GEmploymentNotStartedFormSpec extends Specification {
  val yes = "yes"
  val no = "no"
  val amountPaid = "£199.98"
  val paydateDay = 10
  val paydateMonth = 11
  val paydateYear = 2012
  val weekly = "weekly"
  val monthly = "monthly"
  val monthlyPayDay = "2nd Thursday every month"
  val other = "other"
  val otherText = "some other text"
  val willYouPayIntoPensionText = "pension text"
  val willYouPayForThingsText = "Some things needed to do the job"
  val moreInfo = "more information"

  section("unit", models.domain.CircumstancesSelfEmployment.id)
  "Report an Employment change in your circumstances where employment has not started - Employment Form" should {
    "map weekly paid with no pension/expenses paid when been paid set to 'yes'" in new WithApplication {
      GEmploymentPay.form.bind(
        Map(
          "pastpresentfuture" -> "future",
          "paid" -> yes,
          "howmuch" -> amountPaid,
          "paydate.day" -> paydateDay.toString,
          "paydate.month" -> paydateMonth.toString,
          "paydate.year" -> paydateYear.toString,
          "howOften.frequency" -> weekly,
          "sameAmount" -> no
        )
      ).fold(
          formWithErrors => {
            "This mapping should not happen." must equalTo("Error")
          },
          f => {
            f.paid.get must equalTo(yes)
            f.payDate.get must equalTo(DayMonthYear(Some(paydateDay), Some(paydateMonth), Some(paydateYear), None, None))
            f.howOften.frequency must equalTo(weekly)
            f.sameAmount.get must equalTo(no)
          }
        )
    }
  }
  section("unit", models.domain.CircumstancesSelfEmployment.id)
}
