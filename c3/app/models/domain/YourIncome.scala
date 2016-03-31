package models.domain

import app.StatutoryPaymentFrequency
import controllers.mappings.Mappings
import models.DayMonthYear
import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}

object YourIncome extends Section.Identifier {
  val id = "s16"

  val ssp = "sickpay"
  val spmp = "patmatadoppay"
  val fa = "fostering"
  val dp = "directpay"
  val ao = "anyother"
  val n = "none"
}

case class YourIncomes(beenSelfEmployedSince1WeekBeforeClaim: String = "",
                      beenEmployedSince6MonthsBeforeClaim: String = "",
                      yourIncome_sickpay: Option[String] = None,
                      yourIncome_patmatadoppay: Option[String] = None,
                      yourIncome_fostering: Option[String] = None,
                      yourIncome_directpay: Option[String] = None,
                      yourIncome_anyother: Option[String] = None,
                      yourIncome_none: Option[String] = None
                     ) extends QuestionGroup(YourIncomes)

object YourIncomes extends QuestionGroup.Identifier {
  val id = s"${YourIncome.id}.g0"
}



object YourIncomeStatutorySickPay extends Section.Identifier {
  val id = "s17"
}

object StatutorySickPay extends QuestionGroup.Identifier {
  val id = s"${YourIncomeStatutorySickPay.id}.g1"

  def howOftenPaidStatutorySickPayItVariesRequired: Constraint[StatutorySickPay] = Constraint[StatutorySickPay]("constraint.statutorySickPay.howOftenPaidStatutorySickPay") {
    statutorySickPay =>
      if (statutorySickPay.howOftenPaidStatutorySickPay == StatutoryPaymentFrequency.ItVaries) {
        statutorySickPay.howOftenPaidStatutorySickPayOther match {
          case Some(howOften) => Valid
          case _ => Invalid(ValidationError("statutorySickPay.howOftenPaidStatutorySickPay.required"))
        }
      }
      else Valid
  }

  def whenDidYouLastGetPaidRequired: Constraint[StatutorySickPay] = Constraint[StatutorySickPay]("constraint.statutorySickPay.whenDidYouLastGetPaid") {
    statutorySickPay =>
      if (statutorySickPay.stillBeingPaidStatutorySickPay == Mappings.no) {
        statutorySickPay.whenDidYouLastGetPaid match {
          case Some(whenLastPaid) => Valid
          case _ => Invalid(ValidationError("statutorySickPay.whenDidYouLastGetPaid.required"))
        }
      }
      else Valid
  }
}

case class StatutorySickPay(stillBeingPaidStatutorySickPay: String = "",
                            whenDidYouLastGetPaid: Option[DayMonthYear] = None,
                            whoPaidYouStatutorySickPay: String = "",
                            amountOfStatutorySickPay: String = "",
                            howOftenPaidStatutorySickPay: String = "",
                            howOftenPaidStatutorySickPayOther: Option[String] = None
 ) extends QuestionGroup(StatutorySickPay)

object YourIncomeStatutoryMaternityAdoptionPay extends Section.Identifier {
  val id = "s18"
}

object StatutoryMaternityAdoptionPay extends QuestionGroup.Identifier {
  val id = s"${YourIncomeStatutoryMaternityAdoptionPay.id}.g1"
}

case class StatutoryMaternityAdoptionPay() extends QuestionGroup(StatutoryMaternityAdoptionPay)

object YourIncomeFosteringAllowance extends Section.Identifier {
  val id = "s19"
}

object FosteringAllowance extends QuestionGroup.Identifier {
  val id = s"${YourIncomeFosteringAllowance.id}.g1"
}

case class FosteringAllowance() extends QuestionGroup(FosteringAllowance)

object YourIncomeDirectPayment extends Section.Identifier {
  val id = "s20"
}

object DirectPayment extends QuestionGroup.Identifier {
  val id = s"${YourIncomeDirectPayment.id}.g1"
}

case class DirectPayment() extends QuestionGroup(DirectPayment)

object YourIncomeAnyOtherIncome extends Section.Identifier {
  val id = "s21"
}

object AnyOtherIncome extends QuestionGroup.Identifier {
  val id = s"${YourIncomeAnyOtherIncome.id}.g1"
}

case class AnyOtherIncome() extends QuestionGroup(AnyOtherIncome)



