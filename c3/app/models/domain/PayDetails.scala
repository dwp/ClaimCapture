package models.domain

import models.SortCode

object PayDetails extends Section.Identifier {
  val id = "s10"
}

case class HowWePayYou(likeToBePaid: String = "Not asked", paymentFrequency: String = "Not asked") extends QuestionGroup(HowWePayYou)

object HowWePayYou extends QuestionGroup.Identifier {
  val id = s"${PayDetails.id}.g1"
}

case class BankBuildingSocietyDetails(accountHolderName: String = "",
                                      bankFullName: String = "",
                                      sortCode: SortCode = SortCode("","",""),
                                      accountNumber: String = "",
                                      rollOrReferenceNumber: String = "") extends QuestionGroup(BankBuildingSocietyDetails)

object BankBuildingSocietyDetails extends QuestionGroup.Identifier {
  val id = s"${PayDetails.id}.g2"
}