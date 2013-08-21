package models.domain

import models._
import models.yesNo.YesNo

case object OtherMoney extends Section.Identifier {
  val id = "s9"
}

case class AboutOtherMoney(yourBenefits: YesNo = YesNo("")) extends QuestionGroup(AboutOtherMoney)

object AboutOtherMoney extends QuestionGroup.Identifier {
  val id = s"${OtherMoney.id}.g1"
}

case class StatutorySickPay(haveYouHadAnyStatutorySickPay: String = "",
                            howMuch: Option[String] = None,
                            howOften: Option[PaymentFrequency] = None,
                            employersName: Option[String] = None,
                            employersAddress: Option[MultiLineAddress] = None,
                            employersPostcode: Option[String] = None) extends QuestionGroup(StatutorySickPay)

case object StatutorySickPay extends QuestionGroup.Identifier {
  val id = s"${OtherMoney.id}.g5"
}

case class OtherStatutoryPay(otherPay:String = "",
                             howMuch:Option[String] = None,
                             howOften:Option[PaymentFrequency] = None,
                             employersName:Option[String] = None,
                             employersAddress:Option[MultiLineAddress] = None,
                             employersPostcode:Option[String] = None) extends QuestionGroup(OtherStatutoryPay)

case object OtherStatutoryPay extends QuestionGroup.Identifier {
  val id = s"${OtherMoney.id}.g6"
}

case class OtherEEAStateOrSwitzerland(benefitsFromOtherEEAStateOrSwitzerland: String = "", workingForOtherEEAStateOrSwitzerland: String = "") extends QuestionGroup(OtherEEAStateOrSwitzerland)

object OtherEEAStateOrSwitzerland extends QuestionGroup.Identifier {
  val id = s"${OtherMoney.id}.g7"
}