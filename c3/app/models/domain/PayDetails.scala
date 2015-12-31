package models.domain

import models.SortCode

object PayDetails extends Section.Identifier {
  val id = "s13"
}

case class BankBuildingSocietyDetails(accountHolderName: String = "",
                                      bankFullName: String = "",
                                      sortCode: SortCode = SortCode("","",""),
                                      accountNumber: String = "",
                                      rollOrReferenceNumber: String = "")

case class HowWePayYou(likeToBePaid: String = "", bankDetails: Option[BankBuildingSocietyDetails] = Option(BankBuildingSocietyDetails()), paymentFrequency: String = "") extends QuestionGroup(HowWePayYou)

object HowWePayYou extends QuestionGroup.Identifier {
  val id = s"${PayDetails.id}.g1"
}

