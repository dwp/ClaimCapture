package models.domain

import models.DayMonthYear
import play.api.mvc.Call


case object SelfEmployment extends Section.Identifier {
  val id = "s9"
}

case object AboutSelfEmployment extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g1"
}

case class AboutSelfEmployment(call: Call,
                               areYouSelfEmployedNow: String,
                               whenDidYouStartThisJob: Option[DayMonthYear],
                               whenDidTheJobFinish: Option[DayMonthYear],
                               haveYouCeasedTrading: Option[String],
                               natureOfYourBusiness: Option[String]
                                ) extends QuestionGroup(AboutSelfEmployment)


case object SelfEmploymentYourAccounts extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g2"
}

case class SelfEmploymentYourAccounts(call: Call,
                                      whatWasOrIsYourTradingYearFrom: Option[DayMonthYear],
                                      whatWasOrIsYourTradingYearTo: Option[DayMonthYear],

                                      areAccountsPreparedOnCashFlowBasis: String,
                                      areIncomeOutgoingsProfitSimilarToTrading: Option[String],
                                      tellUsWhyAndWhenTheChangeHappened: String,
                                      doYouHaveAnAccountant: Option[String],
                                      canWeContactYourAccountant: String
                                       ) extends QuestionGroup(SelfEmploymentYourAccounts)










