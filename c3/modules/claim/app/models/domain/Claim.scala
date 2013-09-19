package models.domain

import language.postfixOps
import models.view.{CachedClaim, Navigation}
import xml.DWPCAClaim
import models.DayMonthYear
import com.dwp.carers.s2.xml.validation.XmlValidatorFactory
import app.PensionPaymentFrequency._

/**
 * Represents data gathered for a claim.
 * The structure is defined in DigitalForm.
 * Here, we handle specific behaviour as the generation of XML from the data gathered and the key identifying the cache holding instances of this class.
 */
case class Claim(override val sections: List[Section] = List(), override val startDigitalFormTime: Long = System.currentTimeMillis())(implicit navigation: Navigation = Navigation()) extends DigitalForm(sections, startDigitalFormTime)(navigation) {

  def copyForm(sections: List[Section])(implicit navigation: Navigation): DigitalForm = copy(sections)(navigation)

  def cacheKey = CachedClaim.key

  def dateOfClaim: Option[DayMonthYear] = questionGroup(ClaimDate) match {
    case Some(c: ClaimDate) => Some(c.dateOfClaim)
    case _ => None
  }

  def xml(transactionId: String) = DWPCAClaim.xml(this, transactionId)

  def xmlValidator = XmlValidatorFactory.buildCaValidator()

  def honeyPot: Boolean = {
    def checkTimeOutsideUK: Boolean = {
      questionGroup[TimeOutsideUK] match {
        case Some(q) => {
          q.livingInUK.answer == "no" && (q.livingInUK.date.isDefined || q.livingInUK.text.isDefined || q.livingInUK.goBack.isDefined) // Bot given fields were not visible.
        }
        case _ => false
      }
    }

    def checkMoreAboutTheCare: Boolean = {
      questionGroup[MoreAboutTheCare] match {
        case Some(q) => {
          q.spent35HoursCaringBeforeClaim.answer == "no" && q.spent35HoursCaringBeforeClaim.date.isDefined // Bot given field spent35HoursCaringBeforeClaim.date was not visible.
        }
        case _ => false
      }
    }

    def checkNormalResidenceAndCurrentLocation: Boolean = {
      questionGroup[NormalResidenceAndCurrentLocation] match {
        case Some(q) => {
          q.whereDoYouLive.answer == "yes" && q.whereDoYouLive.text.isDefined // Bot given field whereDoYouLive.text was not visible.
        }
        case _ => false
      }
    }

    def checkPensionSchemes: Boolean = {
      questionGroup[PensionSchemes] match {
        case Some(q) => {
          if (q.payPersonalPensionScheme == "no") {
            q.howOftenPersonal match {
              case Some(f) => true // Bot given field howOftenPersonal was not visible.
              case _ => false
            }
          }
          else {
            q.howOftenPersonal match {
              case Some(f) => f.frequency != Other && f.other.isDefined // Bot given field howOftenPersonal.other was not visible.
              case _ => false
            }
          }
        }
        case _ => false
      }
    }

    def checkChildcareExpenses: Boolean = {
      questionGroup[ChildcareExpenses] match {
        case Some(q) => {
          q.howOftenPayChildCare.frequency != Other && q.howOftenPayChildCare.other.isDefined // Bot given field howOftenPayChildCare.other was not visible.
        }
        case _ => false
      }
    }

    def checkPersonYouCareForExpenses: Boolean = {
      questionGroup[PersonYouCareForExpenses] match {
        case Some(q) => {
          q.howOftenPayCare.frequency != Other && q.howOftenPayCare.other.isDefined // Bot given field howOftenPayCare.other was not visible.
        }
        case _ => false
      }
    }

    def checkChildcareExpensesWhileAtWork: Boolean = {
      questionGroup[ChildcareExpensesWhileAtWork] match {
        case Some(q) => {
          q.howOftenPayChildCare.frequency != Other && q.howOftenPayChildCare.other.isDefined // Bot given field howOftenPayChildCare.other was not visible.
        }
        case _ => false
      }
    }

    def checkExpensesWhileAtWork: Boolean = {
      questionGroup[ExpensesWhileAtWork] match {
        case Some(q) => {
          q.howOftenPayExpenses.frequency != Other && q.howOftenPayExpenses.other.isDefined // Bot given field howOftenPayExpenses.other was not visible.
        }
        case _ => false
      }
    }

    def checkAboutOtherMoney: Boolean = {
      questionGroup[AboutOtherMoney] match {
        case Some(q) => {
          q.anyPaymentsSinceClaimDate.answer == "no" && (q.whoPaysYou.isDefined || q.howMuch.isDefined || q.howOften.isDefined) // Bot given fields were not visible.
        }
        case _ => false
      }
    }

    def checkStatutorySickPay: Boolean = {
      questionGroup[StatutorySickPay] match {
        case Some(q) => {
          q.haveYouHadAnyStatutorySickPay == "no" && (q.howMuch.isDefined || q.howOften.isDefined || q.employersName.isDefined || q.employersAddress.isDefined || q.employersPostcode.isDefined) // Bot given fields were not visible.
        }
        case _ => false
      }
    }

    def checkOtherStatutoryPay: Boolean = {
      questionGroup[OtherStatutoryPay] match {
        case Some(q) => {
          q.otherPay == "no" && (q.howMuch.isDefined || q.howOften.isDefined || q.employersName.isDefined || q.employersAddress.isDefined || q.employersPostcode.isDefined) // Bot given fields were not visible.
        }
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