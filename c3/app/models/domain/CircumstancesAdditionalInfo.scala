package models.domain

import models.yesNo.YesNoWithText
import models.DayMonthYear

case object CircumstancesAdditionalInfo extends Section.Identifier {
  val id = "c2"
  //override val expectedMinTimeToCompleteInMillis: Long = 10000
}

case class CircumstancesOtherInfo(change: String = "") extends QuestionGroup(CircumstancesOtherInfo)

object CircumstancesOtherInfo extends QuestionGroup.Identifier {
  val id = s"${CircumstancesAdditionalInfo.id}.g2"
}

case class CircumstancesSelfEmployment(stillCaring: YesNoWithText = YesNoWithText("", None),
                                       startOfSelfEmployment: DayMonthYear = DayMonthYear(),
                                       typeOfBusiness: String = "",
                                       totalOverWeeklyIncomeThreshold: String = "")
  extends QuestionGroup(CircumstancesSelfEmployment)

object CircumstancesSelfEmployment extends QuestionGroup.Identifier {
  val id = s"${CircumstancesAdditionalInfo.id}.g2"
}