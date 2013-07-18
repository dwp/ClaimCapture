package models.domain

import models._
import models.yesNo.YesNoWith2Text

case object OtherMoney {
  val id = "s8"
}

case class AboutOtherMoney(yourBenefits: YesNoWith2Text) extends QuestionGroup(AboutOtherMoney.id)

object AboutOtherMoney extends QuestionGroup(s"${OtherMoney.id}.g1")

case class MoneyPaidToSomeoneElseForYou(moneyAddedToBenefitSinceClaimDate: String) extends QuestionGroup(MoneyPaidToSomeoneElseForYou.id)

case object MoneyPaidToSomeoneElseForYou extends QuestionGroup(s"${OtherMoney.id}.g2")

case class PersonWhoGetsThisMoney(fullName: String, nationalInsuranceNumber: Option[NationalInsuranceNumber], nameOfBenefit: String) extends QuestionGroup(PersonWhoGetsThisMoney.id)

case object PersonWhoGetsThisMoney extends QuestionGroup(s"${OtherMoney.id}.g3")