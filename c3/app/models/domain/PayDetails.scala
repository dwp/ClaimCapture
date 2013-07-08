package models.domain

import models.SortCode

object PayDetails {
  val id = "s6"
}


case class HowWePayYou(likeToBePaid:String,paymentFrequency: String) extends QuestionGroup(HowWePayYou.id)

object HowWePayYou extends QuestionGroup(s"${PayDetails.id}.g1")

case class BankBuildingSocietyDetails(accountHolderName :String ,bankFullName :String ,sortCode:SortCode ,accountName:String, rollOrReferenceNumber:String) extends QuestionGroup(BankBuildingSocietyDetails.id)

object BankBuildingSocietyDetails extends QuestionGroup(s"${PayDetails.id}.g2")
