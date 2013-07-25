package models.domain

import models.DayMonthYear
import play.api.mvc.Call
import models.MultiLineAddress


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


case class ChildcareProvidersContactDetails(call: Call,
                                       address: Option[MultiLineAddress], 
                                       postcode: Option[String]) extends QuestionGroup(ChildcareProvidersContactDetails)

case object ChildcareProvidersContactDetails extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g6"
}

case class ExpensesWhileAtWork(call: Call,
                                       howMuchYouPay: Option[String], 
                                       nameOfPerson: String,
                                       whatRelationIsToYou: Option[String],
                                       whatRelationIsTothePersonYouCareFor: Option[String]) extends QuestionGroup(ExpensesWhileAtWork)

case object ExpensesWhileAtWork extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g7"
}

case class CareProvidersContactDetails(call: Call,
                                       address: Option[MultiLineAddress], 
                                       postcode: Option[String]) extends QuestionGroup(CareProvidersContactDetails)

case object CareProvidersContactDetails extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g8"
}

case object SelfEmploymentYourAccounts extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g2"
}

case class SelfEmploymentYourAccounts(call: Call,
                                      whatWasOrIsYourTradingYearFrom: Option[DayMonthYear],
                                      whatWasOrIsYourTradingYearTo: Option[DayMonthYear],
                                      areAccountsPreparedOnCashFlowBasis: String,
                                      areIncomeOutgoingsProfitSimilarToTrading: Option[String],
                                      tellUsWhyAndWhenTheChangeHappened: Option[String],
                                      doYouHaveAnAccountant: Option[String],
                                      canWeContactYourAccountant: Option[String]
                                       ) extends QuestionGroup(SelfEmploymentYourAccounts)


case object SelfEmploymentAccountantContactDetails extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g3"
}

case class SelfEmploymentAccountantContactDetails(call: Call,
                                      accountantsName: String,
                                      address: MultiLineAddress,
                                      postCode: Option[String],
                                      telephoneNumber: Option[String],
                                      faxNumber: Option[String]
                                       ) extends QuestionGroup(SelfEmploymentAccountantContactDetails)








