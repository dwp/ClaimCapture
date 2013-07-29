package controllers.s8_other_money

import org.specs2.mutable.{Tags, Specification}

class G2MoneyPaidToSomeoneElseForYouFormSpec extends Specification with Tags {
  "Money Paid To Someone Else For You" should {
    val moneyAddedToBenefitSinceClaimDate = "yes"

    "map data into case class" in {
      G2MoneyPaidToSomeoneElseForYou.form.bind(
        Map("moneyAddedToBenefitSinceClaimDate" -> moneyAddedToBenefitSinceClaimDate
        )
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.moneyAddedToBenefitSinceClaimDate must equalTo(moneyAddedToBenefitSinceClaimDate)
        }
      )
    }

    "reject invalid yesNo answer" in {
      G2MoneyPaidToSomeoneElseForYou.form.bind(
        Map("moneyAddedToBenefitSinceClaimDate" -> "INVALID")
      ).fold(
        formWithErrors => {
          formWithErrors.errors.head.message must equalTo("yesNo.invalid")
          formWithErrors.errors.length must equalTo(1)
        },
        f => "This mapping should not happen." must equalTo("Valid"))
    }
  }
}