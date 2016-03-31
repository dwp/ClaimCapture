package models.domain

import controllers.mappings.Mappings
import models.DayMonthYear
import models.yesNo.YesNoWithText

case object SelfEmployment extends Section.Identifier {
  val id = "s9"

  def isSelfEmployed(claim: Claim): Boolean = {
    claim.questionGroup[YourIncomes] match {
      case Some(employment) => employment.beenSelfEmployedSince1WeekBeforeClaim == Mappings.yes
      case _ => false
    }
  }
}

case object AboutSelfEmployment extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g1"
}

case class AboutSelfEmployment(areYouSelfEmployedNow: String = "",
                               whenDidYouStartThisJob: DayMonthYear = DayMonthYear(None, None, None),
                               whenDidTheJobFinish: Option[DayMonthYear] = None,
                               haveYouCeasedTrading: Option[String] = None,
                               natureOfYourBusiness: String = "") extends QuestionGroup(AboutSelfEmployment)

case object SelfEmploymentYourAccounts extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g2"
}

case class SelfEmploymentYourAccounts(doYouKnowYourTradingYear: String = "",
                                      whatWasOrIsYourTradingYearFrom: Option[DayMonthYear] = None,
                                      whatWasOrIsYourTradingYearTo: Option[DayMonthYear] = None,
                                      areIncomeOutgoingsProfitSimilarToTrading: Option[String] = None,
                                      tellUsWhyAndWhenTheChangeHappened: Option[String] = None) extends QuestionGroup(SelfEmploymentYourAccounts)

case object SelfEmploymentPensionsAndExpenses extends QuestionGroup.Identifier {
  val id = s"${SelfEmployment.id}.g4"
}

case class SelfEmploymentPensionsAndExpenses(payPensionScheme: YesNoWithText = YesNoWithText("", None),
                              haveExpensesForJob: YesNoWithText = YesNoWithText("", None)
                             ) extends QuestionGroup(SelfEmploymentPensionsAndExpenses)




