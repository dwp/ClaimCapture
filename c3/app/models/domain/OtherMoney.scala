package models.domain

import models._
import models.yesNo.{YesNoWithEmployerAndMoney, YesNo}

case object OtherMoney extends Section.Identifier {
  val id = "s10"
}

case class AboutOtherMoney(anyPaymentsSinceClaimDate: YesNo = YesNo(""),
                        whoPaysYou: Option[String] = None,
                        howMuch: Option[String] = None,
                        howOften: Option[PaymentFrequency] = None,
                        statutorySickPay: YesNoWithEmployerAndMoney = models.yesNo.YesNoWithEmployerAndMoney("", None, None, None, None, None),
                        otherStatutoryPay: YesNoWithEmployerAndMoney = models.yesNo.YesNoWithEmployerAndMoney("", None, None, None, None, None)) extends QuestionGroup(AboutOtherMoney)

object AboutOtherMoney extends QuestionGroup.Identifier {
  val id = s"${OtherMoney.id}.g1"
}
