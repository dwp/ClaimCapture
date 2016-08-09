package controllers.breaks_in_care

import controllers.IterationID
import controllers.mappings.Mappings
import models.view.{CachedClaim, Navigable}
import play.api.Play._
import play.api.mvc.{Request, Controller}
import play.api.data.Forms._
import controllers.mappings.Mappings._
import play.api.data.{FormError, Form}
import models.domain._
import utils.helpers.CarersForm._
import play.api.i18n._

import scala.language.postfixOps

object GBreaksInCareType extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "breaktype_hospital" -> optional(nonEmptyText),
    "breaktype_carehome" -> optional(nonEmptyText),
    "breaktype_none" -> optional(nonEmptyText),
    "breaktype_other" -> optional(text.verifying(validYesNo))
  )(BreaksInCareType.apply)(BreaksInCareType.unapply)
    .verifying("selectother", validateOther _)
    .verifying("selectone", validateAnySelected _)
    .verifying("deselectnone", validateNoneNotallowed _)
  )

  def present = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    track(BreaksInCare) { implicit claim =>
      Ok(views.html.breaks_in_care.breaksInCareType(form.fill(BreaksInCareType)))
    }
  }

  def breaksInCare(implicit claim: Claim) = claim.questionGroup[BreaksInCareType].getOrElse(BreaksInCareType())

  def submit = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val errors = formWithErrors
          .replaceError("", "deselectnone", FormError("breaktype", "breaks.breaktype.deselectnone", Seq(dateForBreaks(claim, request2lang), dpname(claim))))
          .replaceError("", "selectone", FormError("breaktype", "breaks.breaktype.selectone", Seq(dateForBreaks(claim, request2lang), dpname(claim))))
          .replaceError("", "selectother", FormError("breaktype_other", errorRequired, Seq(dpname(claim))))
        BadRequest(views.html.breaks_in_care.breaksInCareType(errors))
      },
      breaksInCareType => {
        claim.update(breaksInCareType) -> Redirect(nextPage(breaksInCareType))
      })
  }

  private def nextPage(breaksInCareType: BreaksInCareType)(implicit claim: Claim, request: Request[_]) = {
    if (breaksInCareType.hospital.isDefined) routes.GBreaksInCareHospital.present(IterationID(form))
    else if (breaksInCareType.carehome.isDefined) routes.GBreaksInCareRespite.present(IterationID(form))
    else if (breaksInCareType.other.isDefined && breaksInCareType.other.get == Mappings.yes) routes.GBreaksInCareOther.present(IterationID(form))
    else controllers.s_education.routes.GYourCourseDetails.present
  }

  def delete = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    Ok(views.html.breaks_in_care.breaksInCareType(form.fill(BreaksInCareType)))
  }

  private def dpname(claim: Claim) = {
    val theirPersonalDetails = claim.questionGroup(TheirPersonalDetails).getOrElse(TheirPersonalDetails()).asInstanceOf[TheirPersonalDetails]
    theirPersonalDetails.firstName + " " + theirPersonalDetails.surname
  }

  private def dateForBreaks(claim: Claim, lang: Lang) = {
    val claimDateQG = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())
    claimDateQG.dateWeRequireBreakInCareInformationFrom(lang)
  }

  private def validateOther(breaksInCareType: BreaksInCareType) = breaksInCareType.other match{
    case Some(Mappings.yes) => true
    case Some(Mappings.no) => true
    case _ => false
  }

  private def validateAnySelected(breaksInCareType: BreaksInCareType) = breaksInCareType match {
    case BreaksInCareType(None, None, None, _) => false
    case _ => true
  }

  private def validateNoneNotallowed(breaksInCareType: BreaksInCareType) = someTrue match {
    case (breaksInCareType.hospital | breaksInCareType.carehome) if (breaksInCareType.none == someTrue) => false
    case _ => true
  }
}
