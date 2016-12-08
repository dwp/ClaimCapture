package models.domain

import models.SortCode

object PayDetails extends Identifier(id = "s13")

case class BankBuildingSocietyDetails(accountHolderName: String = "",
                                      bankFullName: String = "",
                                      sortCode: SortCode = SortCode("","",""),
                                      accountNumber: String = "",
                                      rollOrReferenceNumber: String = "")

case class HowWePayYou(likeToBePaid: String = "", bankDetails: Option[BankBuildingSocietyDetails] = Option(BankBuildingSocietyDetails()), paymentFrequency: String = "") extends QuestionGroup(HowWePayYou)

object HowWePayYou extends QGIdentifier(id = s"${PayDetails.id}.g1")


