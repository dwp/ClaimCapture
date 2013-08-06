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

case class AboutSelfEmployment(call: Call = NoRouting,
                               areYouSelfEmployedNow: String = "",
                               whenDidYouStartThisJob: Option[DayMonthYear] = None,
                               whenDidTheJobFinish: Option[DayMonthYear] = None,
                               haveYouCeasedTrading: Option[String] = None,
                               natureOfYourBusiness: Option[String] = None
                                ) extends QuestionGroup(AboutSelfEmployment)

case object SelfEmploymentYourAccounts extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g2"
}

case class SelfEmploymentYourAccounts(call: Call = NoRouting,
                                      whatWasOrIsYourTradingYearFrom: Option[DayMonthYear] = None,
                                      whatWasOrIsYourTradingYearTo: Option[DayMonthYear] = None,
                                      areAccountsPreparedOnCashFlowBasis: String = "",
                                      areIncomeOutgoingsProfitSimilarToTrading: Option[String] = None,
                                      tellUsWhyAndWhenTheChangeHappened: Option[String] = None,
                                      doYouHaveAnAccountant: Option[String] = None,
                                      canWeContactYourAccountant: Option[String] = None
                                       ) extends QuestionGroup(SelfEmploymentYourAccounts)


case class SelfEmploymentAccountantContactDetails(call: Call = NoRouting,
                                                  accountantsName: String = "",
                                                  address: MultiLineAddress = MultiLineAddress(None, None, None),
                                                  postcode: Option[String] = None,
                                                  telephoneNumber: Option[String] = None,
                                                  faxNumber: Option[String] = None
                                                   ) extends QuestionGroup(SelfEmploymentAccountantContactDetails)


case object SelfEmploymentAccountantContactDetails extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g3"
}


case object SelfEmploymentPensionsAndExpenses extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g4"
}

case class SelfEmploymentPensionsAndExpenses(call: Call = NoRouting,
                                             pensionSchemeMapping: YesNoWithText = YesNoWithText(answer="", text=None),
                                             doYouPayToLookAfterYourChildren: String = "",
                                             didYouPayToLookAfterThePersonYouCaredFor: String = ""
                                              ) extends QuestionGroup(SelfEmploymentPensionsAndExpenses)

case class ChildcareExpensesWhileAtWork(call: Call = NoRouting,
                               howMuchYouPay: Option[String] = None,
                               nameOfPerson: String = "",
                               whatRelationIsToYou: Option[String] = None,
                               relationToPartner: Option[String] = None,
                               whatRelationIsTothePersonYouCareFor: Option[String] = None) extends QuestionGroup(ChildcareExpensesWhileAtWork)

case object ChildcareExpensesWhileAtWork extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g5"
}


case class ChildcareProvidersContactDetails(call: Call = NoRouting,
                                            address: Option[MultiLineAddress] = None,
                                            postcode: Option[String] = None) extends QuestionGroup(ChildcareProvidersContactDetails)

case object ChildcareProvidersContactDetails extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g6"
}

case class ExpensesWhileAtWork(call: Call = NoRouting,
                               howMuchYouPay: Option[String] = None,
                               nameOfPerson: String = "",
                               whatRelationIsToYou: Option[String] = None,
                               whatRelationIsTothePersonYouCareFor: Option[String] = None) extends QuestionGroup(ExpensesWhileAtWork)

case object ExpensesWhileAtWork extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g7"
}

case class CareProvidersContactDetails(call: Call = NoRouting,
                                       address: Option[MultiLineAddress] = None,
                                       postcode: Option[String] = None) extends QuestionGroup(CareProvidersContactDetails)

case object CareProvidersContactDetails extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g8"
}








