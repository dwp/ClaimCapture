package models.domain

import models.DayMonthYear
import models.yesNo.YesNoWithDate


object YourClaimDate extends Section.Identifier {
  val id = "s2"
}


case class ClaimDate(dateOfClaim: DayMonthYear = DayMonthYear(), spent35HoursCaringBeforeClaim:YesNoWithDate = YesNoWithDate("", None)) extends QuestionGroup(ClaimDate)

object ClaimDate extends QuestionGroup.Identifier {
  val id = s"${YourClaimDate.id}.g1"
}
