package controllers.circs.breaks_in_care

import controllers.CarersForms._
import controllers.IterationID
import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.domain._
import models.view.{CachedChangeOfCircs}
import models.yesNo.YesNoDontKnowWithDates
import play.api.Play._
import play.api.data.Forms._
import play.api.data.validation.{Valid, ValidationError, Invalid, Constraint}
import play.api.data.{Form, FormError}
import play.api.i18n.{I18nSupport, MMessages, MessagesApi}
import play.api.mvc.{Controller, Request}
import utils.helpers.CarersForm._
import controllers.CarersForms._

object GBreaksInCareHospital extends Controller with CachedChangeOfCircs with I18nSupport with BreaksGatherChecks {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val yourStayEndedMapping = "yourStayEnded" -> optional(yesNoWithDate)

  val dpStayEndedMapping = "dpStayEnded" -> optional(yesNoWithDate)

  val expectToCareAgainMapping = "expectToCareAgain" -> optional(yesNoDontKnowWithDates)
  val expectToCareAgainMapping2 = "expectToCareAgain2" -> optional(yesNoDontKnowWithDates)

  val form = Form(mapping(
    "iterationID" -> carersNonEmptyText,
    "typeOfCare" -> default(carersNonEmptyText, Breaks.hospital),
    "whoWasInHospital" -> carersNonEmptyText.verifying(validWhoWasAwayType),
    "whenWereYouAdmitted" -> optional(dayMonthYear),
    yourStayEndedMapping,
    "whenWasDpAdmitted" -> optional(dayMonthYear),
    dpStayEndedMapping,
    "breaksInCareStillCaring" -> optional(nonEmptyText),
    "yourMedicalProfessional" -> default(optional(nonEmptyText), None),
    "dpMedicalProfessional" -> default(optional(nonEmptyText), None),
    "caringEnded" -> default(optional(dayMonthYear), None),
    "caringStarted" -> default(optional(yesNoWithDate), None),
    expectToCareAgainMapping,
    expectToCareAgainMapping2,
    "whereWasDp" -> default(optional(radioWithText), None),
    "whereWereYou" -> default(optional(radioWithText), None),
    "whenWereYouAdmitted.time" -> default(optional(carersNonEmptyText), None),
    "caringEnded.time" -> default(optional(carersNonEmptyText), None)
  )(CircsBreak.apply)(CircsBreak.unapply)
    .verifying(requiredWhenWereYouAdmitted)
    .verifying(requiredYourStayEndedAnswer)
    .verifying(requiredYourStayEndedDate)
    .verifying(requiredWhenWasDpAdmitted)
    .verifying(requiredDpStayEndedAnswer)
    .verifying(requiredDpStayEndedDate)
    .verifying(requiredBreaksInCareStillCaring)
    .verifying(requiredStartDateNotAfterEndDate)
    .verifying(validateExpectToCareAgain)
    .verifying(validateExpectToCareAgain2)
  )

  val backCall = routes.GBreaksInCareSummary.present()

  def present(iterationID: String) = claimingWithCheck { implicit circs => implicit request => implicit request2lang =>
    val break = circs.questionGroup[CircsBreaksInCare].getOrElse(CircsBreaksInCare(List())).breaks.find(_.iterationID == iterationID).getOrElse(CircsBreak())
    Ok(views.html.circs.breaks_in_care.breaksInCareHospital(form.fill(break), backCall))
  }

  def submit = claimingWithCheck { implicit circs => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val dp = circsDpName(circs)
        val formWithErrorsUpdate = formWithErrors
          .replaceError("", "whenWereYouAdmitted", FormError("whenWereYouAdmitted", errorRequired))
          .replaceError("", "whenWereYouAdmitted.invalid", FormError("whenWereYouAdmitted", errorInvalid))
          .replaceError("", "yourStayEnded.answer", FormError("yourStayEnded.answer", errorRequired))
          .replaceError("", "yourStayEnded.date", FormError("yourStayEnded.date", errorRequired))
          .replaceError("", "yourStayEnded.date.invalid", FormError("yourStayEnded.date", errorInvalid))
          .replaceError("", "yourStayEnded.invalidDateRange", FormError("yourStayEnded.date", errorInvalidDateRange))
          .replaceError("", "whenWasDpAdmitted", FormError("whenWasDpAdmitted", errorRequired, Seq(dp)))
          .replaceError("", "whenWasDpAdmitted.invalid", FormError("whenWasDpAdmitted", errorInvalid, Seq(dp)))
          .replaceError("", "dpStayEnded.answer", FormError("dpStayEnded.answer", errorRequired))
          .replaceError("", "dpStayEnded.date", FormError("dpStayEnded.date", errorRequired, Seq(dp)))
          .replaceError("", "dpStayEnded.date.invalid", FormError("dpStayEnded.date", errorInvalid, Seq(dp)))
          .replaceError("", "dpStayEnded.invalidDateRange", FormError("dpStayEnded.date", errorInvalidDateRange, Seq(dp)))
          .replaceError("", "breaksInCareStillCaring", FormError("breaksInCareStillCaring", errorRequired, Seq(dp)))
          .replaceError("", "breaksInCareStillCaring.invalidYesNo", FormError("breaksInCareStillCaring", invalidYesNo, Seq(dp)))
          .replaceError("", "expectToCareAgain", FormError("expectToCareAgain.answer", errorRequired))
          .replaceError("", "expectToCareAgain.yesdate", FormError("expectToCareAgain.yesdate", errorRequired))
          .replaceError("", "expectToCareAgain.yesdate.invalid", FormError("expectToCareAgain.yesdate", errorInvalid))
          .replaceError("", "expectToCareAgain.nodate", FormError("expectToCareAgain.nodate", errorRequired))
          .replaceError("", "expectToCareAgain.nodate.invalid", FormError("expectToCareAgain.nodate", errorInvalid))
          .replaceError("", "expectToCareAgain2", FormError("expectToCareAgain2.answer", errorRequired))
          .replaceError("", "expectToCareAgain2.yesdate", FormError("expectToCareAgain2.yesdate", errorRequired))
          .replaceError("", "expectToCareAgain2.yesdate.invalid", FormError("expectToCareAgain2.yesdate", errorInvalid))
          .replaceError("", "expectToCareAgain2.nodate", FormError("expectToCareAgain2.nodate", errorRequired))
          .replaceError("", "expectToCareAgain2.nodate.invalid", FormError("expectToCareAgain2.nodate", errorInvalid))
        BadRequest(views.html.circs.breaks_in_care.breaksInCareHospital(formWithErrorsUpdate, backCall))
      },
      break => {
        val updatedBreaksInCare = breaksInCare.update(break).breaks.size match {
          case noOfBreaks if (noOfBreaks > Breaks.maximumBreaks) => breaksInCare
          case _ => breaksInCare.update(break)
        }
        val updatedClaim = updateClaim(updatedBreaksInCare)
        updatedClaim -> Redirect(nextPage)
      })
  }

  private def updateClaim(newbreaks: CircsBreaksInCare)(implicit circs: Claim) = {
    val updatedBreaks = updatedBreakTypesObject
    val updatedClaim = circs.update(updatedBreaks).update(newbreaks)
    updatedClaim
  }

  private def updatedBreakTypesObject(implicit circs: Claim) = {
    // Delete the hospital answer from claim. Otherwise, it will prepopulate the answer when return to Summary page
    breaksTypes.copy(hospital = None)
  }

  private def nextPage(implicit circs: Claim, request: Request[_]) = {
    val breaksInCareType = circs.questionGroup(CircsBreaksInCareType).getOrElse(CircsBreaksInCareType()).asInstanceOf[CircsBreaksInCareType]
    (breaksInCareType.carehome.isDefined, breaksInCareType.other.isDefined) match {
      case (true, _) => controllers.circs.breaks_in_care.routes.GBreaksInCareRespite.present(IterationID(form))
      case (_, true) if (breaksInCareType.other.get == Mappings.yes) => controllers.circs.breaks_in_care.routes.GBreaksInCareOther.present(IterationID(form))
      case (_, _) => controllers.circs.breaks_in_care.routes.GBreaksInCareSummary.present()
    }
  }

  private def validateExpectToCareAgain: Constraint[CircsBreak] = Constraint[CircsBreak]("constraint.breakCaringStartedDate") { break =>
    if(break.yourStayEnded.isDefined && break.yourStayEnded.get.answer.equals(Mappings.no)){
      break.expectToCareAgain match{
        case None=> Invalid(ValidationError("expectToCareAgain"))
        case Some(YesNoDontKnowWithDates(Some(Mappings.yes),None,_))=> Valid
        case Some(YesNoDontKnowWithDates(Some(Mappings.yes),Some(dmy),_))=> validateDate(break.expectToCareAgain.get.yesdate.get, "expectToCareAgain.yesdate.invalid")
        case Some(YesNoDontKnowWithDates(Some(Mappings.no),_,None))=> Invalid(ValidationError("expectToCareAgain.nodate"))
        case Some(YesNoDontKnowWithDates(Some(Mappings.no),_,Some(dmy)))=> validateDate(break.expectToCareAgain.get.nodate.get, "expectToCareAgain.nodate.invalid")
        case _ => Valid
      }
    }
    else{
      Valid
    }
  }

  private def validateExpectToCareAgain2: Constraint[CircsBreak] = Constraint[CircsBreak]("constraint.breakCaringStartedDate") { break =>
    if(break.dpStayEnded.isDefined && break.dpStayEnded.get.answer.equals(Mappings.no)){
      break.expectToCareAgain2 match{
        case None=> Invalid(ValidationError("expectToCareAgain2"))
        case Some(YesNoDontKnowWithDates(Some(Mappings.yes),None,_))=> Valid
        case Some(YesNoDontKnowWithDates(Some(Mappings.yes),Some(dmy),_))=> validateDate(break.expectToCareAgain2.get.yesdate.get, "expectToCareAgain2.yesdate.invalid")
        case Some(YesNoDontKnowWithDates(Some(Mappings.no),_,None))=> Invalid(ValidationError("expectToCareAgain2.nodate"))
        case Some(YesNoDontKnowWithDates(Some(Mappings.no),_,Some(dmy)))=> validateDate(break.expectToCareAgain2.get.nodate.get, "expectToCareAgain2.nodate.invalid")
        case _ => Valid
      }
    }
    else{
      Valid
    }
  }

  def breaksInCare(implicit circs: Claim) = circs.questionGroup[CircsBreaksInCare].getOrElse(CircsBreaksInCare())

  def breaksTypes(implicit circs: Claim) = circs.questionGroup[CircsBreaksInCareType].getOrElse(CircsBreaksInCareType())
}
