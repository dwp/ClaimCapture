package models.domain

case object YourIncomeStatutorySickPay extends Section.Identifier {
  val id = "s16"
}

case object StatutorySickPay extends QuestionGroup.Identifier {
  val id = s"${YourIncomeStatutorySickPay.id}.g1"
}

case class StatutorySickPay() extends QuestionGroup(StatutorySickPay)

case object YourIncomeStatutoryMaternityAdoptionPay extends Section.Identifier {
  val id = "s17"
}

case object StatutoryMaternityAdoptionPay extends QuestionGroup.Identifier {
  val id = s"${YourIncomeStatutoryMaternityAdoptionPay.id}.g1"
}

case class StatutoryMaternityAdoptionPay() extends QuestionGroup(StatutoryMaternityAdoptionPay)

case object YourIncomeFosteringAllowance extends Section.Identifier {
  val id = "s18"
}

case object FosteringAllowance extends QuestionGroup.Identifier {
  val id = s"${YourIncomeFosteringAllowance.id}.g1"
}

case class FosteringAllowance() extends QuestionGroup(FosteringAllowance)

case object YourIncomeDirectPayment extends Section.Identifier {
  val id = "s19"
}

case object DirectPayment extends QuestionGroup.Identifier {
  val id = s"${YourIncomeDirectPayment.id}.g1"
}

case class DirectPayment() extends QuestionGroup(DirectPayment)

case object YourIncomeAnyOtherIncome extends Section.Identifier {
  val id = "s20"
}

case object AnyOtherIncome extends QuestionGroup.Identifier {
  val id = s"${YourIncomeAnyOtherIncome.id}.g1"
}

case class AnyOtherIncome() extends QuestionGroup(AnyOtherIncome)



