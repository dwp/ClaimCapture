package models.domain

import models.yesNo.YesNoWithDate
import models.DayMonthYear


case class CircumstancesSelfEmployment(stillCaring: YesNoWithDate = YesNoWithDate("", None),
                                       startOfSelfEmployment: DayMonthYear = DayMonthYear(),
                                       typeOfBusiness: String = "",
                                       totalOverWeeklyIncomeThreshold: String = "")
  extends QuestionGroup(CircumstancesSelfEmployment)

object CircumstancesSelfEmployment extends QuestionGroup.Identifier {
  val id = s"${CircumstancesAdditionalInfo.id}.g2"
}