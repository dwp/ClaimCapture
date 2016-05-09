package monitoring

import app.ConfigProperties._
import models.domain._
import play.api.Logger

trait ClaimBotChecking extends BotChecking {
  private def verifyAboutExpenses(job: Iteration): Boolean = {
    job.questionGroup[PensionAndExpenses] match {
      case Some(q) =>
        if(q.payPensionScheme.answer == "no") {
          q.payPensionScheme.text match {
            case Some(f) => return true; // Bot given field pension expenses was not visible.
            case _ => false
          }
        }
        if(q.payForThings.answer == "no") {
          q.payForThings.text match {
            case Some(f) => return true; // Bot given field pay for things was not visible.
            case _ => false
          }
        }
        if(q.haveExpensesForJob.answer == "no") {
          q.haveExpensesForJob.text match {
            case Some(f) => return true; // Bot given field whatExpensesForJob was not visible.
            case _ => false
          }
        }
      case _ => false
    }
    false
  }

  def checkTimeToCompleteAllSections(claim: Claim, currentTime: Long) = {
    val sectionExpectedTimes = Map[String, Long](
      "s0" -> getIntProperty("speed.s0").toLong,
      "s2" -> getIntProperty("speed.s2").toLong,
      "s3" -> getIntProperty("speed.s3").toLong,
      "s4" -> getIntProperty("speed.s4").toLong,
      "s6" -> getIntProperty("speed.s6").toLong,
      "s7" -> getIntProperty("speed.s7").toLong,
      "s8" -> getIntProperty("speed.s8").toLong,
      "s9" -> getIntProperty("speed.s9").toLong,
      "s10" -> getIntProperty("speed.s10").toLong,
      "s11" -> getIntProperty("speed.s11").toLong
    )
    evaluateTimeToCompleteAllSections(claim, currentTime, sectionExpectedTimes)
  }

  def calculateActualTimeToCompleteAllSections(currentTime: Long, created: Long): Long = {
    val actualTimeToCompleteAllSections: Long = currentTime - created
    Histograms.recordClaimSubmissionTime(actualTimeToCompleteAllSections)
    actualTimeToCompleteAllSections
  }

  def honeyPot(claim: Claim): Boolean = {
    def checkMoreAboutTheCare: Boolean = {
      claim.questionGroup[ClaimDate] match {
        case Some(q) =>
          q.spent35HoursCaringBeforeClaim.answer == "no" && q.spent35HoursCaringBeforeClaim.date.isDefined // Bot given field spent35HoursCaringBeforeClaim.date was not visible.

        case _ => false
      }
    }

    def checkSelfEmploymentAboutExpenses: Boolean = {
      claim.questionGroup[SelfEmploymentPensionsAndExpenses] match {
        case Some(q) =>
          if(q.payPensionScheme.answer == "no") {
            q.payPensionScheme.text match {
              case Some(f) => return true; // Bot given field pension expenses was not visible.
              case _ => false
            }
          }
          if(q.haveExpensesForJob.answer == "no") {
            q.haveExpensesForJob.text match {
              case Some(f) => return true; // Bot given field whatExpensesForJob was not visible.
              case _ => false
            }
          }
        case _ => false
      }
      false
    }

    def checkAboutExpenses: Boolean = {
      checkEmploymentCriteria(verifyAboutExpenses)
    }

    def checkEmploymentCriteria(executeFunction: (Iteration) => Boolean): Boolean = {
      claim.questionGroup[Jobs].map {
        jobs =>
          for (job <- jobs) {
            if (executeFunction(job)) return true
          }
      }
      false
    }

    def checkStatutorySickPay: Boolean = {
      val yourIncomes = claim.questionGroup[YourIncomes].getOrElse(new YourIncomes())
      claim.questionGroup[StatutorySickPay] match {
        case Some(q) =>
          (q.stillBeingPaidThisPay == "yes" && q.whenDidYouLastGetPaid.isDefined) || (q.howOftenPaidThisPay != "other"  && q.howOftenPaidThisPayOther.isDefined) || (!yourIncomes.yourIncome_sickpay.isDefined && !(q.stillBeingPaidThisPay.isEmpty || q.amountOfThisPay.isEmpty || q.howOftenPaidThisPay.isEmpty || q.whoPaidYouThisPay.isEmpty))  // Bot given fields were not visible.
        case _ => false
      }
    }

    def checkStatutoryPay: Boolean = {
      val yourIncomes = claim.questionGroup[YourIncomes].getOrElse(new YourIncomes())
      claim.questionGroup[StatutoryMaternityPaternityAdoptionPay] match {
        case Some(q) =>
          (q.stillBeingPaidThisPay == "yes" && q.whenDidYouLastGetPaid.isDefined) || (q.howOftenPaidThisPay != "other"  && q.howOftenPaidThisPayOther.isDefined) || (!yourIncomes.yourIncome_sickpay.isDefined && !(q.paymentTypesForThisPay.isEmpty || q.stillBeingPaidThisPay.isEmpty || q.amountOfThisPay.isEmpty || q.howOftenPaidThisPay.isEmpty || q.whoPaidYouThisPay.isEmpty))  // Bot given fields were not visible.
        case _ => false
      }
    }

    def checkFosteringAllowance: Boolean = {
      val yourIncomes = claim.questionGroup[YourIncomes].getOrElse(new YourIncomes())
      claim.questionGroup[FosteringAllowance] match {
        case Some(q) =>
          (q.stillBeingPaidThisPay == "yes" && q.whenDidYouLastGetPaid.isDefined) || (q.howOftenPaidThisPay != "other"  && q.howOftenPaidThisPayOther.isDefined) || (q.paymentTypesForThisPay != "other"  && q.paymentTypesForThisPayOther.isDefined) || (!yourIncomes.yourIncome_sickpay.isDefined && !(q.paymentTypesForThisPay.isEmpty || q.stillBeingPaidThisPay.isEmpty || q.amountOfThisPay.isEmpty || q.howOftenPaidThisPay.isEmpty || q.whoPaidYouThisPay.isEmpty))  // Bot given fields were not visible.
        case _ => false
      }
    }

    def checkOtherPayments: Boolean = {
      val yourIncomes = claim.questionGroup[YourIncomes].getOrElse(new YourIncomes())
      claim.questionGroup[OtherPayments] match {
        case Some(q) =>
          (!yourIncomes.yourIncome_anyother.isDefined && !(q.otherPaymentsInfo.isEmpty))  // Bot given fields were not visible.
        case _ => false
      }
    }

    def checkDirectPay: Boolean = {
      val yourIncomes = claim.questionGroup[YourIncomes].getOrElse(new YourIncomes())
      claim.questionGroup[DirectPayment] match {
        case Some(q) =>
          (q.stillBeingPaidThisPay == "yes" && q.whenDidYouLastGetPaid.isDefined) || (q.howOftenPaidThisPay != "other"  && q.howOftenPaidThisPayOther.isDefined) || (!yourIncomes.yourIncome_sickpay.isDefined && !(q.stillBeingPaidThisPay.isEmpty || q.amountOfThisPay.isEmpty || q.howOftenPaidThisPay.isEmpty || q.whoPaidYouThisPay.isEmpty))  // Bot given fields were not visible.
        case _ => false
      }
    }

    val moreAboutTheCare = checkMoreAboutTheCare
    val aboutExpenses = checkAboutExpenses
    val selfEmploymentAboutExpenses = checkSelfEmploymentAboutExpenses
    val statutorySickPay = checkStatutorySickPay
    val statutoryPay = checkStatutoryPay
    val fosteringAllowance = checkFosteringAllowance
    val directPay = checkDirectPay
    val otherPayments = checkOtherPayments

    if (moreAboutTheCare) Logger.warn("Honeypot triggered : moreAboutTheCare")
    if (aboutExpenses) Logger.warn("Honeypot triggered : employment aboutExpenses")
    if (selfEmploymentAboutExpenses) Logger.warn("Honeypot triggered : selfEmploymentAboutExpenses")
    if (statutorySickPay) Logger.warn("Honeypot triggered : statutorySickPay")
    if (statutoryPay) Logger.warn("Honeypot triggered : statutoryPay")
    if (fosteringAllowance) Logger.warn("Honeypot triggered : fosteringAllowance")
    if (directPay) Logger.warn("Honeypot triggered : directPay")
    if (otherPayments) Logger.warn("Honeypot triggered : otherPayments")

    moreAboutTheCare ||
    aboutExpenses ||
    selfEmploymentAboutExpenses ||
    statutorySickPay ||
    statutoryPay ||
    fosteringAllowance ||
    directPay ||
    otherPayments
  }
}
