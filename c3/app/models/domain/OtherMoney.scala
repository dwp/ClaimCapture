package models.domain

import models._
import models.yesNo.YesNoWith2Text

case object OtherMoney extends Section.Identifier {
  val id = "s9"
}

case class AboutOtherMoney(yourBenefits: YesNoWith2Text = YesNoWith2Text()) extends QuestionGroup(AboutOtherMoney)

object AboutOtherMoney extends QuestionGroup.Identifier {
  val id = s"${OtherMoney.id}.g1"
}

case class MoneyPaidToSomeoneElseForYou(moneyAddedToBenefitSinceClaimDate: String) extends QuestionGroup(MoneyPaidToSomeoneElseForYou)

case object MoneyPaidToSomeoneElseForYou extends QuestionGroup.Identifier {
  val id = s"${OtherMoney.id}.g2"
}

case class PersonWhoGetsThisMoney(fullName: String = "", nationalInsuranceNumber: Option[NationalInsuranceNumber] = None, nameOfBenefit: String = "") extends QuestionGroup(PersonWhoGetsThisMoney)

case object PersonWhoGetsThisMoney extends QuestionGroup.Identifier {
  val id = s"${OtherMoney.id}.g3"
}

case class PersonContactDetails(address: Option[MultiLineAddress] = None, postcode: Option[String] = None) extends QuestionGroup(PersonContactDetails)

case object PersonContactDetails extends QuestionGroup.Identifier {
  val id = s"${OtherMoney.id}.g4"
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