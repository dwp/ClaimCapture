package models.domain

import models._

case object OtherMoney {
  val id = "s8"
}

case class AboutOtherMoney(yourBenefits:Option[String], partnerBenefits: Option[String]) extends QuestionGroup(AboutOtherMoney.id)

object AboutOtherMoney extends QuestionGroup(s"${OtherMoney.id}.g1")