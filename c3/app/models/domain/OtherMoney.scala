package models.domain

import models._
import models.yesNo.YesNoWithText

case object OtherMoney {
  val id = "s8"
}

case class AboutOtherMoney(yourBenefits: YesNoWithText) extends QuestionGroup(AboutOtherMoney.id)

object AboutOtherMoney extends QuestionGroup(s"${OtherMoney.id}.g1")