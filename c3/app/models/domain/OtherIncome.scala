package models.domain

import models._

case object OtherIncome {
  val id = "s8"
}

case class AboutOtherMoney(yourBenefits:Option[String], partnerBenefits: Option[String]) extends QuestionGroup(AboutOtherMoney.id)

object AboutOtherMoney extends QuestionGroup(s"${OtherIncome.id}.g1")