package controllers.s8_self_employment

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
          "areAccountsPreparedOnCashFlowBasis" -> "yes",
          "areIncomeOutgoingsProfitSimilarToTrading" -> "yes",
          "tellUsWhyAndWhenTheChangeHappened" -> "A year back",
          "doYouHaveAnAccountant" -> "yes",
          "canWeContactYourAccountant" -> "yes")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.tellUsWhyAndWhenTheChangeHappened must equalTo(Some("A year back"))
        }
      )
    }

    "reject if areAccountsPreparedOnCashFlowBasis is not filled" in {
      G2SelfEmploymentYourAccounts.form.bind(
        Map("tellUsWhyAndWhenTheChangeHappened" -> "A year back",
          "canWeContactYourAccountant" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("error.required"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if tellUsWhyAndWhenTheChangeHappened is not filled" in {
      G2SelfEmploymentYourAccounts.form.bind(
        Map("areAccountsPreparedOnCashFlowBasis" -> "yes",
          "areIncomeOutgoingsProfitSimilarToTrading" -> "no",
          "doYouHaveAnAccountant" -> "yes",
          "canWeContactYourAccountant" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("tellUsWhyAndWhenTheChangeHappened"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "reject if canWeContactYourAccountant is not filled" in {
      G2SelfEmploymentYourAccounts.form.bind(
        Map("areAccountsPreparedOnCashFlowBasis" -> "yes",
          "areIncomeOutgoingsProfitSimilarToTrading" -> "yes",
          "doYouHaveAnAccountant" -> "yes")
      ).fold(
        formWithErrors => formWithErrors.errors.head.message must equalTo("canWeContactYourAccountant"),
        f => "This mapping should not happen." must equalTo("Valid")
      )
    }

    "Self Employment Your Accounts - Allow optional fields to be left blank" in {
      G2SelfEmploymentYourAccounts.form.bind(
        Map("areAccountsPreparedOnCashFlowBasis" -> "yes",
          "tellUsWhyAndWhenTheChangeHappened" -> "A year back",
          "canWeContactYourAccountant" -> "yes")
      ).fold(
        formWithErrors => "This mapping should not happen." must equalTo("Error"),
        f => {
          f.areAccountsPreparedOnCashFlowBasis must equalTo("yes")
        }
      )
    }
  } section("unit", models.domain.SelfEmployment.id)
}