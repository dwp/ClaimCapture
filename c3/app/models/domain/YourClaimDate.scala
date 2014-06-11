package models.domain

import models.DayMonthYear


object YourClaimDate extends Section.Identifier {
  val id = "s2"
}


case class ClaimDate(dateOfClaim: DayMonthYear = DayMonthYear()) extends QuestionGroup(ClaimDate)

object ClaimDate extends QuestionGroup.Identifier {
  val id = s"${YourClaimDate.id}.g1"
}
