package models.domain

import models._
import models.yesNo.YesNoWithText
import play.api.mvc.Call

case object OtherMoney extends Section.Identifier {
  val id = "s9"
}

case class AboutOtherMoney(yourBenefits: YesNoWithText, call: Call = NoRouting) extends QuestionGroup(AboutOtherMoney)

object AboutOtherMoney extends QuestionGroup.Identifier {
  val id = s"${OtherMoney.id}.g1"
}

case class MoneyPaidToSomeoneElseForYou(moneyAddedToBenefitSinceClaimDate: String, call: Call = NoRouting) extends QuestionGroup(MoneyPaidToSomeoneElseForYou)

case object MoneyPaidToSomeoneElseForYou extends QuestionGroup.Identifier {
  val id = s"${OtherMoney.id}.g2"
}

case class PersonWhoGetsThisMoney(fullName: String, nationalInsuranceNumber: Option[NationalInsuranceNumber] = None, nameOfBenefit: String,
                                  call: Call = NoRouting) extends QuestionGroup(PersonWhoGetsThisMoney)

case object PersonWhoGetsThisMoney extends QuestionGroup.Identifier {
  val id = s"${OtherMoney.id}.g3"
}

case class PersonContactDetails(address: Option[MultiLineAddress] = None, postcode: Option[String] = None,
                                call: Call = NoRouting) extends QuestionGroup(PersonContactDetails)

case object PersonContactDetails extends QuestionGroup.Identifier {
  val id = s"${OtherMoney.id}.g4"
}

case class StatutorySickPay(haveYouHadAnyStatutorySickPay: String,
                            howMuch: Option[String] = None,
                            howOften: Option[PaymentFrequency] = None,
                            employersName: Option[String] = None,
                            employersAddress: Option[MultiLineAddress] = None,
                            employersPostcode: Option[String] = None,
                            call: Call = NoRouting) extends QuestionGroup(StatutorySickPay)

case object StatutorySickPay extends QuestionGroup.Identifier {
  val id = s"${OtherMoney.id}.g5"
}

case class OtherStatutoryPay(otherPay:String,
                             howMuch:Option[String] = None,
                             howOften:Option[PaymentFrequency] = None,
                             employersName:Option[String] = None,
                             employersAddress:Option[MultiLineAddress] = None,
                             employersPostcode:Option[String] = None,
                             call:Call = NoRouting) extends QuestionGroup(OtherStatutoryPay)

case object OtherStatutoryPay extends QuestionGroup.Identifier {
  val id = s"${OtherMoney.id}.g6"
}

case class OtherEEAStateOrSwitzerland(call: Call = NoRouting,
                                      benefitsFromOtherEEAStateOrSwitzerland: String, workingForOtherEEAStateOrSwitzerland: String) extends QuestionGroup(OtherEEAStateOrSwitzerland)

object OtherEEAStateOrSwitzerland extends QuestionGroup.Identifier {
  val id = s"${OtherMoney.id}.g7"
}