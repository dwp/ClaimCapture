package models.domain

import models.SortCode

object PayDetails extends Section.Identifier {
  val id = "s11"
}

case class BankBuildingSocietyDetails(accountHolderName: String = "",
                                      bankFullName: String = "",
                                      sortCode: SortCode = SortCode("","",""),
                                      accountNumber: String = "",
                                      rollOrReferenceNumber: String = "")

case class HowWePayYou(likeToBePaid: String = "", paymentFrequency: String = "", bankDetails: Option[BankBuildingSocietyDetails] = Option(BankBuildingSocietyDetails())) extends QuestionGroup(HowWePayYou)

object HowWePayYou extends QuestionGroup.Identifier {
  val id = s"${PayDetails.id}.g1"
}

