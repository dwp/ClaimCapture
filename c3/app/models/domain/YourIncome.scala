package models.domain

import app.{PaymentTypes, StatutoryPaymentFrequency}
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

object StatutorySickPay extends QuestionGroup.Identifier with OtherIncomes {
  val id = s"${YourIncomeStatutorySickPay.id}.g1"
}

case class StatutorySickPay(
                            override val stillBeingPaidThisPay: String = "",
                            override val whenDidYouLastGetPaid: Option[DayMonthYear] = None,
                            override val whoPaidYouThisPay: String = "",
                            override val amountOfThisPay: String = "",
                            override val howOftenPaidThisPay: String = "",
                            override val howOftenPaidThisPayOther: Option[String] = None
                           ) extends QuestionGroup(StatutorySickPay) with OtherIncomes

object YourIncomeStatutoryMaternityPaternityAdoptionPay extends Section.Identifier {
  val id = "s18"
}

object StatutoryMaternityPaternityAdoptionPay extends QuestionGroup.Identifier with OtherIncomes {
  val id = s"${YourIncomeStatutoryMaternityPaternityAdoptionPay.id}.g1"
}

case class StatutoryMaternityPaternityAdoptionPay(
                                                  override val paymentTypesForThisPay: String = "",
                                                  override val stillBeingPaidThisPay: String = "",
                                                  override val whenDidYouLastGetPaid: Option[DayMonthYear] = None,
                                                  override val whoPaidYouThisPay: String = "",
                                                  override val amountOfThisPay: String = "",
                                                  override val howOftenPaidThisPay: String = "",
                                                  override val howOftenPaidThisPayOther: Option[String] = None
                                                 ) extends QuestionGroup(StatutoryMaternityPaternityAdoptionPay) with OtherIncomes

object YourIncomeFosteringAllowance extends Section.Identifier {
  val id = "s19"
}

object FosteringAllowance extends QuestionGroup.Identifier with OtherIncomes {
  val id = s"${YourIncomeFosteringAllowance.id}.g1"
}

case class FosteringAllowance(
                              override val paymentTypesForThisPay: String = "",
                              override val paymentTypesForThisPayOther: Option[String] = None,
                              override val stillBeingPaidThisPay: String = "",
                              override val whenDidYouLastGetPaid: Option[DayMonthYear] = None,
                              override val whoPaidYouThisPay: String = "",
                              override val amountOfThisPay: String = "",
                              override val howOftenPaidThisPay: String = "",
                              override val howOftenPaidThisPayOther: Option[String] = None
                             ) extends QuestionGroup(FosteringAllowance) with OtherIncomes

object YourIncomeDirectPayment extends Section.Identifier {
  val id = "s20"
}

object DirectPayment extends QuestionGroup.Identifier with OtherIncomes {
  val id = s"${YourIncomeDirectPayment.id}.g1"
}

case class DirectPayment(
                         override val stillBeingPaidThisPay: String = "",
                         override val whenDidYouLastGetPaid: Option[DayMonthYear] = None,
                         override val whoPaidYouThisPay: String = "",
                         override val amountOfThisPay: String = "",
                         override val howOftenPaidThisPay: String = "",
                         override val howOftenPaidThisPayOther: Option[String] = None
                        ) extends QuestionGroup(DirectPayment) with OtherIncomes

object YourIncomeAnyOtherIncome extends Section.Identifier {
  val id = "s21"
}

object AnyOtherIncome extends QuestionGroup.Identifier {
  val id = s"${YourIncomeAnyOtherIncome.id}.g1"
}

case class AnyOtherIncome() extends QuestionGroup(AnyOtherIncome)


trait OtherIncomes {
  val paymentTypesForThisPay: String = ""
  val paymentTypesForThisPayOther: Option[String] = None
  val stillBeingPaidThisPay: String = ""
  val whenDidYouLastGetPaid: Option[DayMonthYear] = None
  val whoPaidYouThisPay: String = ""
  val amountOfThisPay: String = ""
  val howOftenPaidThisPay: String = ""
  val howOftenPaidThisPayOther: Option[String] = None

  def howOftenPaidThisPayItVariesRequired: Constraint[OtherIncomes] = Constraint[OtherIncomes]("constraint.howOftenPaidThisPay") {
    income =>
      if (income.howOftenPaidThisPay == StatutoryPaymentFrequency.ItVaries) {
        income.howOftenPaidThisPayOther match {
          case Some(howOften) => Valid
          case _ => Invalid(ValidationError("howOftenPaidThisPay.required"))
        }
      }
      else Valid
  }

  def whenDidYouLastGetPaidRequired: Constraint[OtherIncomes] = Constraint[OtherIncomes]("constraint.whenDidYouLastGetPaid") {
    income =>
      if (income.stillBeingPaidThisPay == Mappings.no) {
        income.whenDidYouLastGetPaid match {
          case Some(whenLastPaid) => Valid
          case _ => Invalid(ValidationError("whenDidYouLastGetPaid.required"))
        }
      }
      else Valid
  }

  def paymentTypesForThisPayOtherRequired: Constraint[OtherIncomes] = Constraint[OtherIncomes]("constraint.paymentTypesForThisPay") {
    income =>
      if (income.paymentTypesForThisPay == PaymentTypes.Other) {
        income.paymentTypesForThisPayOther match {
          case Some(howOften) => Valid
          case _ => Invalid(ValidationError("paymentTypesForThisPay.required"))
        }
      }
      else Valid
  }
}
