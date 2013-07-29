package models.domain

import models.SortCode
import play.api.mvc.Call

case class PayDetails(howWePayYou: HowWePayYou,bankBuildingSocietyDetails: BankBuildingSocietyDetails )

object PayDetails extends Section.Identifier {
  val id = "s10"
}

case class HowWePayYou(call: Call, likeToBePaid: String, paymentFrequency: String) extends QuestionGroup(HowWePayYou)

object HowWePayYou extends QuestionGroup.Identifier {
  val id = s"${PayDetails.id}.g1"
}

case class BankBuildingSocietyDetails(call: Call,
                                      accountHolderName: String, bankFullName: String, sortCode: SortCode, accountNumber: String, rollOrReferenceNumber: String) extends QuestionGroup(BankBuildingSocietyDetails)

object BankBuildingSocietyDetails extends QuestionGroup.Identifier {
  val id = s"${PayDetails.id}.g2"
}
