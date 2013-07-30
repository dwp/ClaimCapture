package models.domain

import models.DayMonthYear
import play.api.mvc.Call
import models.MultiLineAddress
import models.yesNo.YesNoWithText


case object SelfEmployment extends Section.Identifier {
  val id = "s8"
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
                                      tellUsWhyAndWhenTheChangeHappened: Option[String],
                                      doYouHaveAnAccountant: Option[String],
                                      canWeContactYourAccountant: Option[String]
                                       ) extends QuestionGroup(SelfEmploymentYourAccounts)


case object SelfEmploymentAccountantContactDetails extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g3"
}


case object SelfEmploymentPensionsAndExpenses extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g4"
}

case class SelfEmploymentPensionsAndExpenses(call: Call,
                                             pensionSchemeMapping: YesNoWithText,
                                             lookAfterChildrenMapping: YesNoWithText,
                                             lookAfterCaredForMapping: YesNoWithText
                                              ) extends QuestionGroup(SelfEmploymentPensionsAndExpenses)



case class ChildcareExpensesWhileAtWork(call: Call,
                               howMuchYouPay: Option[String],
                               nameOfPerson: String,
                               whatRelationIsToYou: Option[String],
                               relationToPartner: Option[String],
                               whatRelationIsTothePersonYouCareFor: Option[String]) extends QuestionGroup(ChildcareExpensesWhileAtWork)

case object ChildcareExpensesWhileAtWork extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g5"
}

case class SelfEmploymentAccountantContactDetails(call: Call,
                                      accountantsName: String,
                                      address: MultiLineAddress,
                                      postcode: Option[String],
                                      telephoneNumber: Option[String],
                                      faxNumber: Option[String]
                                       ) extends QuestionGroup(SelfEmploymentAccountantContactDetails)

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








