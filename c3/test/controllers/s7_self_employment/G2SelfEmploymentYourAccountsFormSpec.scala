package controllers.s7_self_employment

import org.specs2.mutable.{Tags, Specification}

class G2SelfEmploymentYourAccountsFormSpec extends Specification with Tags {

  "About Self Employment - Your Accounts" should {

    "map data into case class" in {
      G2SelfEmploymentYourAccounts.form.bind(
        Map("whatWasOrIsYourTradingYearFrom.day" -> "11",
          "whatWasOrIsYourTradingYearFrom.month" -> "11",
          "whatWasOrIsYourTradingYearFrom.year" -> "2011",
          "whatWasOrIsYourTradingYearTo.day" -> "11",
          "whatWasOrIsYourTradingYearTo.month" -> "11",
          "whatWasOrIsYourTradingYearTo.year" -> "2018",
          "areIncomeOutgoingsProfitSimilarToTrading" -> "yes",
          "tellUsWhyAndWhenTheChangeHappened" -> "A year back")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.tellUsWhyAndWhenTheChangeHappened must equalTo(Some("A year back"))
        }
      )
    }

    "reject if tellUsWhyAndWhenTheChangeHappened is not filled" in {
      G2SelfEmploymentYourAccounts.form.bind(
        Map("areAccountsPreparedOnCashFlowBasis" -> "yes",
          "areIncomeOutgoingsProfitSimilarToTrading" -> "no")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }
  } section("unit", models.domain.SelfEmployment.id)
}