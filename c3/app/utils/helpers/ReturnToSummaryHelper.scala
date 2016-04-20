package utils.helpers

import models.domain._

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
    val statutorySickPayChanged = hasStatutorySickPayPaymentsChanged(claim, yourIncomes, previousYourIncomes)
    val statutorySickChanged = hasStatutoryMaternityPaternityAdoptionPaymentsChanged(claim, yourIncomes, previousYourIncomes)
    val fosteringAllowanceChanged = hasFosteringAllowancePaymentsChanged(claim, yourIncomes, previousYourIncomes)
    val directPaymentChanged = hasDirectPaymentsChanged(claim, yourIncomes, previousYourIncomes)
    val otherPaymentsChanged = hasOtherPaymentsChanged(claim, yourIncomes, previousYourIncomes)
    !(statutorySickPayChanged || statutorySickChanged || fosteringAllowanceChanged || directPaymentChanged || otherPaymentsChanged)
  }

  def haveOtherPaymentsFromStatutorySickPayChanged(claim: Claim) = {
    val previousYourIncomes = if (claim.navigation.beenInPreview)claim.checkYAnswers.previouslySavedClaim.get.questionGroup[YourIncomes].get else YourIncomes()
    val yourIncomes = claim.questionGroup[YourIncomes].get
    val statutorySickChanged = hasStatutoryMaternityPaternityAdoptionPaymentsChanged(claim, yourIncomes, previousYourIncomes)
    val fosteringAllowanceChanged = hasFosteringAllowancePaymentsChanged(claim, yourIncomes, previousYourIncomes)
    val directPaymentChanged = hasDirectPaymentsChanged(claim, yourIncomes, previousYourIncomes)
    val otherPaymentsChanged = hasOtherPaymentsChanged(claim, yourIncomes, previousYourIncomes)
    !(statutorySickChanged || fosteringAllowanceChanged || directPaymentChanged || otherPaymentsChanged)
  }

  def haveOtherPaymentsFromStatutoryPayChanged(claim: Claim) = {
    val previousYourIncomes = if (claim.navigation.beenInPreview)claim.checkYAnswers.previouslySavedClaim.get.questionGroup[YourIncomes].get else YourIncomes()
    val yourIncomes = claim.questionGroup[YourIncomes].get
    val fosteringAllowanceChanged = hasFosteringAllowancePaymentsChanged(claim, yourIncomes, previousYourIncomes)
    val directPaymentChanged = hasDirectPaymentsChanged(claim, yourIncomes, previousYourIncomes)
    val otherPaymentsChanged = hasOtherPaymentsChanged(claim, yourIncomes, previousYourIncomes)
    !(fosteringAllowanceChanged || directPaymentChanged || otherPaymentsChanged)
  }

  def haveOtherPaymentsFromFosteringAllowanceChanged(claim: Claim) = {
    val previousYourIncomes = if (claim.navigation.beenInPreview)claim.checkYAnswers.previouslySavedClaim.get.questionGroup[YourIncomes].get else YourIncomes()
    val yourIncomes = claim.questionGroup[YourIncomes].get
    val directPaymentChanged = hasDirectPaymentsChanged(claim, yourIncomes, previousYourIncomes)
    val otherPaymentsChanged = hasOtherPaymentsChanged(claim, yourIncomes, previousYourIncomes)
    !(directPaymentChanged || otherPaymentsChanged)
  }

  def haveOtherPaymentsFromDirectPaymentChanged(claim: Claim) = {
    val previousYourIncomes = if (claim.navigation.beenInPreview)claim.checkYAnswers.previouslySavedClaim.get.questionGroup[YourIncomes].get else YourIncomes()
    val yourIncomes = claim.questionGroup[YourIncomes].get
    val otherPaymentsChanged = hasOtherPaymentsChanged(claim, yourIncomes, previousYourIncomes)
    !(otherPaymentsChanged)
  }

  def hasStatutorySickPayPaymentsChanged(claim: Claim, yourIncomes: YourIncomes, previousYourIncomes: YourIncomes): Boolean = {
    (!previousYourIncomes.yourIncome_sickpay.isDefined && yourIncomes.yourIncome_sickpay.isDefined) || (yourIncomes.yourIncome_sickpay.isDefined && claim.questionGroup[StatutorySickPay].getOrElse(StatutorySickPay()).whoPaidYouThisPay.isEmpty)
  }

  def hasStatutoryMaternityPaternityAdoptionPaymentsChanged(claim: Claim, yourIncomes: YourIncomes, previousYourIncomes: YourIncomes): Boolean = {
    (!previousYourIncomes.yourIncome_patmatadoppay.isDefined && yourIncomes.yourIncome_patmatadoppay.isDefined) || (yourIncomes.yourIncome_patmatadoppay.isDefined && claim.questionGroup[StatutoryMaternityPaternityAdoptionPay].getOrElse(StatutoryMaternityPaternityAdoptionPay()).whoPaidYouThisPay.isEmpty)
  }

  def hasFosteringAllowancePaymentsChanged(claim: Claim, yourIncomes: YourIncomes, previousYourIncomes: YourIncomes): Boolean = {
    (!previousYourIncomes.yourIncome_fostering.isDefined && yourIncomes.yourIncome_fostering.isDefined) || (yourIncomes.yourIncome_fostering.isDefined && claim.questionGroup[FosteringAllowance].getOrElse(FosteringAllowance()).whoPaidYouThisPay.isEmpty)
  }

  def hasDirectPaymentsChanged(claim: Claim, yourIncomes: YourIncomes, previousYourIncomes: YourIncomes): Boolean = {
    (!previousYourIncomes.yourIncome_directpay.isDefined && yourIncomes.yourIncome_directpay.isDefined) || (yourIncomes.yourIncome_directpay.isDefined && claim.questionGroup[DirectPayment].getOrElse(DirectPayment()).whoPaidYouThisPay.isEmpty)
  }

  def hasOtherPaymentsChanged(claim: Claim, yourIncomes: YourIncomes, previousYourIncomes: YourIncomes): Boolean = {
    (!previousYourIncomes.yourIncome_anyother.isDefined && yourIncomes.yourIncome_anyother.isDefined) || (yourIncomes.yourIncome_anyother.isDefined && claim.questionGroup[OtherPayments].getOrElse(OtherPayments()).otherPaymentsInfo.isEmpty)
  }
}