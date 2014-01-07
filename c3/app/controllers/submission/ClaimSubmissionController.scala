package controllers.submission

import play.api.mvc._
import play.api.Logger
import com.google.inject._
import models.view.CachedClaim
import services.UnavailableTransactionIdException
import models.domain._
import app.PensionPaymentFrequency._
import jmx.inspectors.{FastSubmissionNotifier, SubmissionNotifier}
import app.ConfigProperties._
import models.domain.Claim
import scala.Some
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

@Singleton
class ClaimSubmissionController @Inject()(submitter: Submitter) extends SubmissionController(submitter) with CachedClaim{

  def submit = submitting { implicit claim => implicit request =>
    processSubmit(claim, request, errorPage)
  }

  private def verifyPensionScheme (job:Job) : Boolean = {
    job.questionGroup[PensionSchemes] match {
      case Some(q) =>
        if (q.payPersonalPensionScheme == "no") {
          q.howOftenPersonal match {
            case Some(f) => return true; // Bot given field howOftenPersonal was not visible.
            case _ => false
          }
        }
        else {
          q.howOftenPersonal match {
            case Some(f) => if(f.frequency != Other && f.other.isDefined) return true  // Bot given field howOftenPersonal.other was not visible.
            case _ => false
          }
        }
      case _ => false
    }
    false
  }

  private def verifyChildCareExpenses (job:Job) : Boolean = {
    job.questionGroup[ChildcareExpenses] match {
      case Some(q) =>
        if(q.howOftenPayChildCare.frequency != Other && q.howOftenPayChildCare.other.isDefined) return true // Bot given field howOftenPayChildCare.other was not visible.
      case _ => false
    }
    false
  }

  private def verifyPersonYouCareForExpenses (job:Job) : Boolean = {
    job.questionGroup[PersonYouCareForExpenses] match {
      case Some(q) =>
        if(q.howOftenPayCare.frequency != Other && q.howOftenPayCare.other.isDefined) return true // Bot given field howOftenPayCare.other was not visible.
      case _ => false
    }
    false
  }

  def checkTimeToCompleteAllSections(claim: Claim with Claimable, currentTime: Long) = {
    val sectionExpectedTimes = Map[String, Long](
      "s1" -> getProperty("speed.s1",5000L),
      "s2" -> getProperty("speed.s2",5000L),
      "s3" -> getProperty("speed.s3",5000L),
      "s4" -> getProperty("speed.s4",5000L),
      "s5" -> getProperty("speed.s5",5000L),
      "s6" -> getProperty("speed.s6",5000L),
      "s7" -> getProperty("speed.s7",5000L),
      "s8" -> getProperty("speed.s8",5000L),
      "s9" ->  getProperty("speed.s9",5000L),
      "s10" -> getProperty("speed.s10",5000L),
      "s11" -> getProperty("speed.s11",5000L)
    )
    evaluateTimeToCompleteAllSections(claim, currentTime, sectionExpectedTimes)
  }

  def honeyPot(claim: Claim): Boolean = {
    def checkTimeOutsideUK: Boolean = {
      claim.questionGroup[TimeOutsideUK] match {
        case Some(q) =>
          q.livingInUK.answer == "no" && (q.livingInUK.date.isDefined || q.livingInUK.text.isDefined || q.livingInUK.goBack.isDefined) // Bot given fields were not visible.

        case _ => false
      }
    }

    def checkMoreAboutTheCare: Boolean = {
      claim.questionGroup[MoreAboutTheCare] match {
        case Some(q) =>
          q.spent35HoursCaringBeforeClaim.answer == "no" && q.spent35HoursCaringBeforeClaim.date.isDefined // Bot given field spent35HoursCaringBeforeClaim.date was not visible.

        case _ => false
      }
    }

    def checkNormalResidenceAndCurrentLocation: Boolean = {
      claim.questionGroup[NormalResidenceAndCurrentLocation] match {
        case Some(q) =>
          q.whereDoYouLive.answer == "yes" && q.whereDoYouLive.text.isDefined // Bot given field whereDoYouLive.text was not visible.

        case _ => false
      }
    }

    def checkPensionSchemes:Boolean = {
      checkEmploymentCriteria(verifyPensionScheme)
    }

    def checkEmploymentCriteria (executeFunction : (Job) => Boolean) : Boolean = {
      claim.questionGroup[Jobs].map {
        jobs =>
          for (job <- jobs) {
            if (executeFunction(job)) return true
          }
      }
      false
    }

    def checkChildcareExpenses: Boolean = {
      checkEmploymentCriteria(verifyChildCareExpenses)
    }

    def checkPersonYouCareForExpenses: Boolean = {
      checkEmploymentCriteria(verifyPersonYouCareForExpenses)
    }

    def checkChildcareExpensesWhileAtWork: Boolean = {
      claim.questionGroup[ChildcareExpensesWhileAtWork] match {
        case Some(q) =>
          q.howOftenPayChildCare.frequency != Other && q.howOftenPayChildCare.other.isDefined // Bot given field howOftenPayChildCare.other was not visible.

        case _ => false
      }
    }

    def checkExpensesWhileAtWork: Boolean = {
      claim.questionGroup[ExpensesWhileAtWork] match {
        case Some(q) =>
          q.howOftenPayExpenses.frequency != Other && q.howOftenPayExpenses.other.isDefined // Bot given field howOftenPayExpenses.other was not visible.

        case _ => false
      }
    }

    def checkAboutOtherMoney: Boolean = {
      claim.questionGroup[AboutOtherMoney] match {
        case Some(q) =>
          q.anyPaymentsSinceClaimDate.answer == "no" && (q.whoPaysYou.isDefined || q.howMuch.isDefined || q.howOften.isDefined) // Bot given fields were not visible.

        case _ => false
      }
    }

    def checkStatutorySickPay: Boolean = {
      claim.questionGroup[StatutorySickPay] match {
        case Some(q) =>
          q.haveYouHadAnyStatutorySickPay == "no" && (q.howMuch.isDefined || q.howOften.isDefined || q.employersName.isDefined || q.employersAddress.isDefined || q.employersPostcode.isDefined) // Bot given fields were not visible.

        case _ => false
      }
    }

    def checkOtherStatutoryPay: Boolean = {
      claim.questionGroup[OtherStatutoryPay] match {
        case Some(q) =>
          q.otherPay == "no" && (q.howMuch.isDefined || q.howOften.isDefined || q.employersName.isDefined || q.employersAddress.isDefined || q.employersPostcode.isDefined) // Bot given fields were not visible.

        case _ => false
      }
    }

    checkTimeOutsideUK ||
    checkMoreAboutTheCare ||
    checkNormalResidenceAndCurrentLocation ||
    checkPensionSchemes ||
    checkChildcareExpenses ||
    checkPersonYouCareForExpenses ||
    checkChildcareExpensesWhileAtWork ||
    checkExpensesWhileAtWork ||
    checkAboutOtherMoney ||
    checkStatutorySickPay ||
    checkOtherStatutoryPay
  }
}