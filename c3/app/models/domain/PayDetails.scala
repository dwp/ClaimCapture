package models.domain

import app.XMLValues._
import models.SortCode

object PayDetails extends Section.Identifier {
  val id = "s11"
}

case class HowWePayYou(likeToBePaid: String = "", paymentFrequency: String = "") extends QuestionGroup(HowWePayYou)

object HowWePayYou extends QuestionGroup.Identifier {
  val id = s"${PayDetails.id}.g1"
}

case class BankBuildingSocietyDetails(accountHolderName: String = "",
                                      whoseNameIsTheAccountIn: String = "",
                                      bankFullName: String = "",
                                      sortCode: SortCode = SortCode("","",""),
                                      accountNumber: String = "",
                                      rollOrReferenceNumber: String = "") extends QuestionGroup(BankBuildingSocietyDetails)

object BankBuildingSocietyDetails extends QuestionGroup.Identifier {
  val id = s"${PayDetails.id}.g2"
}