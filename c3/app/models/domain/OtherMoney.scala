package models.domain

import models._
import models.yesNo.YesNoWith2Text
import play.api.mvc.Call

case object OtherMoney extends Section.Identifier {
  val id = "s8"
}

case class AboutOtherMoney(yourBenefits: YesNoWith2Text, call: Call) extends QuestionGroup(AboutOtherMoney)

object AboutOtherMoney extends QuestionGroup.Identifier {
  val id = s"${OtherMoney.id}.g1"
}

case class MoneyPaidToSomeoneElseForYou(moneyAddedToBenefitSinceClaimDate: String, call: Call) extends QuestionGroup(MoneyPaidToSomeoneElseForYou)

case object MoneyPaidToSomeoneElseForYou extends QuestionGroup.Identifier {
  val id = s"${OtherMoney.id}.g2"
}

case class PersonWhoGetsThisMoney(fullName: String, nationalInsuranceNumber: Option[NationalInsuranceNumber], nameOfBenefit: String,
                                  call: Call) extends QuestionGroup(PersonWhoGetsThisMoney)

case object PersonWhoGetsThisMoney extends QuestionGroup.Identifier {
  val id = s"${OtherMoney.id}.g3"
}

case class PersonContactDetails(address: Option[MultiLineAddress], postcode: Option[String],
                                call: Call) extends QuestionGroup(PersonContactDetails)

case object PersonContactDetails extends QuestionGroup.Identifier {
  val id = s"${OtherMoney.id}.g4"
}

case class StatutorySickPay(haveYouHadAnyStatutorySickPay: String,
                            howMuch: Option[String],
                            howOften: Option[String],
                            employersName: Option[String],
                            employersAddress: Option[MultiLineAddress],
                            employersPostcode: Option[String],
                            call: Call) extends QuestionGroup(StatutorySickPay)

case object StatutorySickPay extends QuestionGroup.Identifier {
  val id = s"${OtherMoney.id}.g5"
}