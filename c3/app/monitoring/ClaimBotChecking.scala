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
      "s1" -> getProperty("speed.s1", 5000L),
      "s2" -> getProperty("speed.s2", 5000L),
      "s3" -> getProperty("speed.s3", 5000L),
      "s4" -> getProperty("speed.s4", 5000L),
      "s6" -> getProperty("speed.s6", 5000L),
      "s7" -> getProperty("speed.s7", 5000L),
      "s8" -> getProperty("speed.s8", 5000L),
      "s9" -> getProperty("speed.s9", 5000L),
      "s10" -> getProperty("speed.s10", 5000L),
      "s11" -> getProperty("speed.s11", 5000L)
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
      claim.questionGroup[MoreAboutTheCare] match {
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

    def checkAboutOtherMoney: Boolean = {
      claim.questionGroup[AboutOtherMoney] match {
        case Some(q) =>
          q.anyPaymentsSinceClaimDate.answer == "no" && (q.whoPaysYou.isDefined || q.howMuch.isDefined || q.howOften.isDefined) && q.otherStatutoryPay.answer == "no" && q.otherStatutoryPay.answer == "no"// Bot given fields were not visible.
        case _ => false
      }
    }

    def checkStatutorySickPay: Boolean = {
      claim.questionGroup[AboutOtherMoney] match {
        case Some(q) =>
          q.statutorySickPay.answer == "no" && (q.statutorySickPay.howMuch.isDefined || q.statutorySickPay.howOften.isDefined || q.statutorySickPay.employersName.isDefined || q.statutorySickPay.address.isDefined || q.statutorySickPay.postCode.isDefined) // Bot given fields were not visible.

        case _ => false
      }
    }

    def checkOtherStatutoryPay: Boolean = {
      claim.questionGroup[AboutOtherMoney] match {
        case Some(q) =>
          q.otherStatutoryPay.answer == "no" && (q.otherStatutoryPay.howMuch.isDefined || q.otherStatutoryPay.howOften.isDefined || q.otherStatutoryPay.employersName.isDefined || q.otherStatutoryPay.address.isDefined || q.otherStatutoryPay.postCode.isDefined) // Bot given fields were not visible.

        case _ => false
      }
    }

    val moreAboutTheCare = checkMoreAboutTheCare
    val aboutExpenses = checkAboutExpenses
    val selfEmploymentAboutExpenses = checkSelfEmploymentAboutExpenses
    val aboutOtherMoney = checkAboutOtherMoney
    val statutorySickPay = checkStatutorySickPay
    val otherStatutoryPay = checkOtherStatutoryPay

    if (moreAboutTheCare) Logger.warn("Honeypot triggered : moreAboutTheCare")
    if (aboutExpenses) Logger.warn("Honeypot triggered : employment aboutExpenses")
    if (selfEmploymentAboutExpenses) Logger.warn("Honeypot triggered : selfEmploymentAboutExpenses")
    if (aboutOtherMoney) Logger.warn("Honeypot triggered : aboutOtherMoney")
    if (statutorySickPay) Logger.warn("Honeypot triggered : statutorySickPay")
    if (otherStatutoryPay) Logger.warn("Honeypot triggered : otherStatutoryPay")

    moreAboutTheCare ||
      aboutExpenses ||
      selfEmploymentAboutExpenses ||
      aboutOtherMoney ||
      statutorySickPay ||
      otherStatutoryPay
  }

}
