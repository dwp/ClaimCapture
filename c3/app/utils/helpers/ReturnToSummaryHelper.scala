package utils.helpers

import models.domain.{YourIncomes, Claim}

/**
 * Created by peterwhitehead on 25/01/2016.
 */
object ReturnToSummaryHelper {
  def displayReturnToSummaryFromEmploymentAdditionInfo(claim: Claim): Option[String] = {
    if (claim.navigation.beenInPreview && !haveOtherPaymentsChanged(claim)) Some("form.next")
    else None
  }

  def displayReturnToSummaryFromStatutorySickPayInfo(claim: Claim): Option[String] = {
    if (claim.navigation.beenInPreview && !haveOtherPaymentsFromStatutorySickPayChanged(claim)) Some("form.next")
    else None
  }

  def displayReturnToSummaryFromStatutoryPayInfo(claim: Claim): Option[String] = {
    if (claim.navigation.beenInPreview && !haveOtherPaymentsFromStatutoryPayChanged(claim)) Some("form.next")
    else None
  }

  def displayReturnToSummaryFromFosteringAllowanceInfo(claim: Claim): Option[String] = {
    if (claim.navigation.beenInPreview && !haveOtherPaymentsFromFosteringAllowanceChanged(claim)) Some("form.next")
    else None
  }

  def displayReturnToSummaryFromDirectPaymentInfo(claim: Claim): Option[String] = {
    if (claim.navigation.beenInPreview && !haveOtherPaymentsFromDirectPaymentChanged(claim)) Some("form.next")
    else None
  }

  def haveOtherPaymentsChanged(claim: Claim) = {
    val previousYourIncomes = if (claim.navigation.beenInPreview)claim.checkYAnswers.previouslySavedClaim.get.questionGroup[YourIncomes].get else YourIncomes()
    val yourIncomes = claim.questionGroup[YourIncomes].get
    val statutorySickPayChanged = !previousYourIncomes.yourIncome_sickpay.isDefined && yourIncomes.yourIncome_sickpay.isDefined
    val statutorySickChanged = !previousYourIncomes.yourIncome_patmatadoppay.isDefined && yourIncomes.yourIncome_patmatadoppay.isDefined
    val fosteringAllowanceChanged = !previousYourIncomes.yourIncome_fostering.isDefined && yourIncomes.yourIncome_fostering.isDefined
    val directPaymentChanged = !previousYourIncomes.yourIncome_directpay.isDefined && yourIncomes.yourIncome_directpay.isDefined
    val otherPaymentsChanged = !previousYourIncomes.yourIncome_anyother.isDefined && yourIncomes.yourIncome_anyother.isDefined
    !(statutorySickPayChanged || statutorySickChanged || fosteringAllowanceChanged || directPaymentChanged || otherPaymentsChanged)
  }

  def haveOtherPaymentsFromStatutorySickPayChanged(claim: Claim) = {
    val previousYourIncomes = if (claim.navigation.beenInPreview)claim.checkYAnswers.previouslySavedClaim.get.questionGroup[YourIncomes].get else YourIncomes()
    val yourIncomes = claim.questionGroup[YourIncomes].get
    val statutorySickChanged = !previousYourIncomes.yourIncome_patmatadoppay.isDefined && yourIncomes.yourIncome_patmatadoppay.isDefined
    val fosteringAllowanceChanged = !previousYourIncomes.yourIncome_fostering.isDefined && yourIncomes.yourIncome_fostering.isDefined
    val directPaymentChanged = !previousYourIncomes.yourIncome_directpay.isDefined && yourIncomes.yourIncome_directpay.isDefined
    val otherPaymentsChanged = !previousYourIncomes.yourIncome_anyother.isDefined && yourIncomes.yourIncome_anyother.isDefined
    !(statutorySickChanged || fosteringAllowanceChanged || directPaymentChanged || otherPaymentsChanged)
  }

  def haveOtherPaymentsFromStatutoryPayChanged(claim: Claim) = {
    val previousYourIncomes = if (claim.navigation.beenInPreview)claim.checkYAnswers.previouslySavedClaim.get.questionGroup[YourIncomes].get else YourIncomes()
    val yourIncomes = claim.questionGroup[YourIncomes].get
    val fosteringAllowanceChanged = !previousYourIncomes.yourIncome_fostering.isDefined && yourIncomes.yourIncome_fostering.isDefined
    val directPaymentChanged = !previousYourIncomes.yourIncome_directpay.isDefined && yourIncomes.yourIncome_directpay.isDefined
    val otherPaymentsChanged = !previousYourIncomes.yourIncome_anyother.isDefined && yourIncomes.yourIncome_anyother.isDefined
    !(fosteringAllowanceChanged || directPaymentChanged || otherPaymentsChanged)
  }

  def haveOtherPaymentsFromFosteringAllowanceChanged(claim: Claim) = {
    val previousYourIncomes = if (claim.navigation.beenInPreview)claim.checkYAnswers.previouslySavedClaim.get.questionGroup[YourIncomes].get else YourIncomes()
    val yourIncomes = claim.questionGroup[YourIncomes].get
    val directPaymentChanged = !previousYourIncomes.yourIncome_directpay.isDefined && yourIncomes.yourIncome_directpay.isDefined
    val otherPaymentsChanged = !previousYourIncomes.yourIncome_anyother.isDefined && yourIncomes.yourIncome_anyother.isDefined
    !(directPaymentChanged || otherPaymentsChanged)
  }

  def haveOtherPaymentsFromDirectPaymentChanged(claim: Claim) = {
    val previousYourIncomes = if (claim.navigation.beenInPreview)claim.checkYAnswers.previouslySavedClaim.get.questionGroup[YourIncomes].get else YourIncomes()
    val yourIncomes = claim.questionGroup[YourIncomes].get
    val otherPaymentsChanged = !previousYourIncomes.yourIncome_anyother.isDefined && yourIncomes.yourIncome_anyother.isDefined
    !(otherPaymentsChanged)
  }
}