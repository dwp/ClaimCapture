package controllers.breaks_in_care

import app.BreaksInCareGatherOptions
import app.ConfigProperties._
import controllers.CarersForms._
import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.DayMonthYear
import models.domain._
import models.view.CachedClaim
import models.yesNo.YesNoWithDate
import org.joda.time.DateTime
import play.api.Play._
import play.api.data.Forms._
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}
import play.api.data.{Form, FormError}
import play.api.i18n.{I18nSupport, MMessages, MessagesApi}
import play.api.mvc.Controller
import utils.helpers.CarersForm._

import scala.util.{Failure, Success, Try}

/**
  * Created by peterwhitehead on 03/08/2016.
  */
object GBreaksInCareRespite extends Controller with CachedClaim with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val yourStayEndedMapping =
    "yourStayEnded" -> optional(mapping(
      "answer" -> nonEmptyText,
      "date" -> optional(dayMonthYear)
    )(YesNoWithDate.apply)(YesNoWithDate.unapply))

  val dpStayEndedMapping =
    "dpStayEnded" -> optional(mapping(
      "answer" -> nonEmptyText,
      "date" -> optional(dayMonthYear)
    )(YesNoWithDate.apply)(YesNoWithDate.unapply))

  val form = Form(mapping(
    "iterationID" -> carersNonEmptyText,
    "whoWasInRespite" -> carersNonEmptyText.verifying(validWhoWasAwayType),
    "whenWereYouAdmitted" -> optional(dayMonthYear),
    yourStayEndedMapping,
    "whenWasDpAdmitted" -> optional(dayMonthYear),
    dpStayEndedMapping,
    "breaksInCareStillCaring" -> optional(nonEmptyText)
  )(Break.apply)(Break.unapply)
    .verifying(requiredWhenWereYouAdmitted)
    .verifying(requiredYourStayEndedAnswer)
    .verifying(requiredYourStayEndedDate)
    .verifying(requiredWhenWasDpAdmitted)
    .verifying(requiredDpStayEndedAnswer)
    .verifying(requiredDpStayEndedDate)
    .verifying(requiredBreaksInCareStillCaring)
  )

  val backCall = routes.GBreakTypes.present()

  def present(iterationID: String) = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    //track(BreaksInCare) { implicit claim =>
      val break = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare(List())).breaks.find(_.iterationID == iterationID).getOrElse(Break())
      Ok(views.html.breaks_in_care.breaksInCareRespite(form.fill(break), backCall))
    //}
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val theirPersonalDetails = claim.questionGroup(TheirPersonalDetails).getOrElse(TheirPersonalDetails()).asInstanceOf[TheirPersonalDetails]
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "whenWereYouAdmitted", FormError("whenWereYouAdmitted", errorRequired))
          .replaceError("", "whenWereYouAdmitted.invalid", FormError("whenWereYouAdmitted", errorInvalid))
          .replaceError("", "yourStayEnded.answer", FormError("yourStayEnded.answer", errorRequired))
          .replaceError("", "yourStayEnded.date", FormError("yourStayEnded.date", errorRequired))
          .replaceError("", "yourStayEnded.date.invalid", FormError("yourStayEnded.date", errorInvalid))
          .replaceError("", "whenWasDpAdmitted", FormError("whenWasDpAdmitted", errorRequired, Seq(theirPersonalDetails.firstName + " " + theirPersonalDetails.surname)))
          .replaceError("", "whenWasDpAdmitted.invalid", FormError("whenWasDpAdmitted", errorInvalid, Seq(theirPersonalDetails.firstName + " " + theirPersonalDetails.surname)))
          .replaceError("", "dpStayEnded.answer", FormError("dpStayEnded.answer", errorRequired))
          .replaceError("", "dpStayEnded.date", FormError("dpStayEnded.date", errorRequired, Seq(theirPersonalDetails.firstName + " " + theirPersonalDetails.surname)))
          .replaceError("", "dpStayEnded.date.invalid", FormError("dpStayEnded.date", errorInvalid, Seq(theirPersonalDetails.firstName + " " + theirPersonalDetails.surname)))
          .replaceError("", "breaksInCareStillCaring", FormError("breaksInCareStillCaring", errorRequired, Seq(theirPersonalDetails.firstName + " " + theirPersonalDetails.surname)))
          .replaceError("", "breaksInCareStillCaring.invalidYesNo", FormError("breaksInCareStillCaring", invalidYesNo, Seq(theirPersonalDetails.firstName + " " + theirPersonalDetails.surname)))
        BadRequest(views.html.breaks_in_care.breaksInCareRespite(formWithErrorsUpdate, backCall))
      },
      break => {
        val updatedBreaksInCare =
          breaksInCare.update(break).breaks.size match {
            case noOfBreaks if (noOfBreaks > getIntProperty("maximumBreaksInCare")) => breaksInCare
            case _ => breaksInCare.update(break)
          }
        // Delete the answer to the question 'Have you had any breaks in care since...'
        // Otherwise, it will prepopulate the answer when asked 'Have you had any more breaks in care since...'
        Redirect(nextPage(claim.update(updatedBreaksInCare).delete(BreaksInCareSummary)))
      })
  }

  private def nextPage(claim: Claim) = {
    val breaksInCareType = claim.questionGroup(BreaksInCareType).getOrElse(BreaksInCareType()).asInstanceOf[BreaksInCareType]
    breaksInCareType.carehome.isDefined match {
      case true => routes.GBreakTypes.present() //Should goto Respite page
      case false if (breaksInCareType.other.isDefined) => routes.GBreakTypes.present() //should go to other page
      case false => routes.GBreakTypes.present() //to summary
    }
  }

  private def requiredWhenWereYouAdmitted: Constraint[Break] = Constraint[Break]("constraint.breakWhenWereYouAdmitted") { break =>
    break.whoWasInHospital match {
      case BreaksInCareGatherOptions.DP => Valid
      case _ if (!break.whenWereYouAdmitted.isDefined) => Invalid(ValidationError("whenWereYouAdmitted"))
      case _ => validateDate(break.whenWereYouAdmitted.get, "whenWereYouAdmitted.invalid")
    }
  }

  private def requiredYourStayEndedAnswer: Constraint[Break] = Constraint[Break]("constraint.breakYourStayEndedAnswer") { break =>
    break.whoWasInHospital match {
      case BreaksInCareGatherOptions.DP => Valid
      case _ if (!break.yourStayEnded.isDefined) => Invalid(ValidationError("yourStayEnded.answer"))
      case _ => Valid
    }
  }

  private def requiredYourStayEndedDate: Constraint[Break] = Constraint[Break]("constraint.breakYourStayEndedDate") { break =>
    break.whoWasInHospital match {
      case BreaksInCareGatherOptions.DP => Valid
      case _ if (break.yourStayEnded.isDefined && !YesNoWithDate.validate(break.yourStayEnded.get)) => Invalid(ValidationError("yourStayEnded.date"))
      case _ if (break.yourStayEnded.isDefined && break.yourStayEnded.get.answer == Mappings.yes) => validateDate(break.yourStayEnded.get.date.get, "yourStayEnded.date.invalid")
      case _ => Valid
    }
  }

  private def requiredWhenWasDpAdmitted: Constraint[Break] = Constraint[Break]("constraint.breakWhenWasDpAdmitted") { break =>
    break.whoWasInHospital match {
      case BreaksInCareGatherOptions.You => Valid
      case _ if (!break.whenWasDpAdmitted.isDefined) => Invalid(ValidationError("whenWasDpAdmitted"))
      case _ => validateDate(break.whenWasDpAdmitted.get, "whenWasDpAdmitted.invalid")
    }
  }

  private def requiredDpStayEndedAnswer: Constraint[Break] = Constraint[Break]("constraint.breakDpStayEndedAnswer") { break =>
    break.whoWasInHospital match {
      case BreaksInCareGatherOptions.You => Valid
      case _ if (!break.dpStayEnded.isDefined) => Invalid(ValidationError("dpStayEnded.answer"))
      case _ => Valid
    }
  }

  private def requiredDpStayEndedDate: Constraint[Break] = Constraint[Break]("constraint.breakDpStayEndedDate") { break =>
    break.whoWasInHospital match {
      case BreaksInCareGatherOptions.You => Valid
      case _ if (break.dpStayEnded.isDefined && !YesNoWithDate.validate(break.dpStayEnded.get)) => Invalid(ValidationError("dpStayEnded.date"))
      case _ if (break.dpStayEnded.isDefined && break.dpStayEnded.get.answer == Mappings.yes) => validateDate(break.dpStayEnded.get.date.get, "dpStayEnded.date.invalid")
      case _ => Valid
    }
  }

  private def requiredBreaksInCareStillCaring: Constraint[Break] = Constraint[Break]("constraint.breaksInCareStillCaring") { break =>
    break.whoWasInHospital match {
      case BreaksInCareGatherOptions.You => Valid
      case _ if (!break.breaksInCareStillCaring.isDefined) => Invalid(ValidationError("breaksInCareStillCaring"))
      case _ => break.breaksInCareStillCaring.get match {
        case `yes` => Valid
        case `no` => Valid
        case _ => Invalid(ValidationError("breaksInCareStillCaring.invalidYesNo"))
      }
    }
  }

  private def validateDate(dmy: DayMonthYear, field: String) = Try(new DateTime(dmy.year.get, dmy.month.get, dmy.day.get, 0, 0)) match {
    case Success(dt: DateTime) if dt.getYear > 9999 || dt.getYear < 999 => Invalid(ValidationError(field))
    case Success(dt: DateTime) if dt.getYear > 9999 || dt.getYear < 999 => Invalid(ValidationError(field))
    case Success(dt: DateTime) => Valid
    case Failure(_) => Invalid(ValidationError(field))
  }

  def breaksInCare(implicit claim: Claim) = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())
}
