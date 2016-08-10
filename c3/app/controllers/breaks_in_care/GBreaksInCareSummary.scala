package controllers.breaks_in_care

import controllers.IterationID
import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.domain._
import models.view.CachedClaim
import models.yesNo.DeleteId
import play.api.Play._
import play.api.data.Forms._
import play.api.i18n.{Lang, I18nSupport, MMessages, MessagesApi}
import play.api.mvc.{Request, Controller}
import play.api.data.{FormError, Form}
import utils.helpers.CarersForm._

object GBreaksInCareSummary extends Controller with CachedClaim with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val form = Form(mapping(
    "breaksummary_other" -> optional(text.verifying(validYesNo)),
    "breaksummary_answer" -> optional(nonEmptyText)
  )(BreaksInCareSummary.apply)(BreaksInCareSummary.unapply)
    .verifying("mustselectother", validateOther _)
    .verifying("mustselectone", validateOneSelected _)
  )

  def breaks(implicit claim: Claim) = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

  def present = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    Ok(views.html.breaks_in_care.breaksInCareSummary(form.fill(BreaksInCareSummary), breaks))
  }

  def submit = claiming { implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val errors = formWithErrors
          .replaceError("", "mustselectone", FormError("breaksummary_answer", errorRequired, Seq(dateForBreaks(claim, request2lang), dpname(claim))))
          .replaceError("", "mustselectother", FormError(otherError, errorRequired, Seq(dateForBreaks(claim, request2lang), dpname(claim))))
        BadRequest(views.html.breaks_in_care.breaksInCareSummary(errors, breaks))
      },
      breaksInCareSummary => {
        val b = populateBreaksInCareType(breaksInCareSummary)
        claim.update(b) -> Redirect(nextPage(b))
      })
  }

  private def populateBreaksInCareType(breaksInCareSummary: BreaksInCareSummary)(implicit claim: Claim, request: Request[_]) = {
    breaksInCareSummary.breaksummary_answer match {
      case Some(Breaks.hospital) => BreaksInCareType(hospital = Some(Mappings.yes))
      case Some(Breaks.carehome) => BreaksInCareType(carehome = Some(Mappings.yes))
      case Some(Breaks.another) => BreaksInCareType(other = Some(Mappings.yes))
      case _ => BreaksInCareType()
    }
  }

  private def nextPage(breaksInCareType: BreaksInCareType)(implicit claim: Claim, request: Request[_]) = {
    if (breaksInCareType.hospital.isDefined) routes.GBreaksInCareHospital.present(IterationID(form))
    else if (breaksInCareType.carehome.isDefined) routes.GBreaksInCareRespite.present(IterationID(form))
    // THINK WE HAVE ANOTHER TYPE OTHER ??
    else controllers.s_education.routes.GYourCourseDetails.present
  }

  private def dpname(claim: Claim) = {
    val theirPersonalDetails = claim.questionGroup(TheirPersonalDetails).getOrElse(TheirPersonalDetails()).asInstanceOf[TheirPersonalDetails]
    theirPersonalDetails.firstName + " " + theirPersonalDetails.surname
  }

  private def dateForBreaks(claim: Claim, lang: Lang) = {
    val claimDateQG = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())
    claimDateQG.dateWeRequireBreakInCareInformationFrom(lang)
  }

  private def otherError(implicit claim: Claim)={
    breaks.hasBreaks match {
      case true => "breaksummary_other_another"
      case false => "breaksummary_other_first"
    }
  }

  private def validateOther(breaksInCareSummary: BreaksInCareSummary) = breaksInCareSummary.breaksummary_other match {
    case Some(Mappings.yes) => true
    case Some(Mappings.no) => true
    case _ => false
  }

  private def validateOneSelected(breaksInCareSummary: BreaksInCareSummary) = {
    breaksInCareSummary match {
      case BreaksInCareSummary(Some(Mappings.yes), None) => false
      case _ => true
    }
  }

  val deleteForm = Form(mapping(
    "deleteId" -> nonEmptyText
  )(DeleteId.apply)(DeleteId.unapply))

  def delete = claimingWithCheck { implicit claim => implicit request => implicit request2lang =>
    deleteForm.bindEncrypted.fold(
      errors => BadRequest(views.html.breaks_in_care.breaksInCareSummary(form.fill(BreaksInCareSummary), breaks)),
      deleteForm => {
        val updatedBreaks = breaks.delete(deleteForm.id)
        if (updatedBreaks.breaks == breaks.breaks) BadRequest(views.html.breaks_in_care.breaksInCareSummary(form.fill(BreaksInCareSummary), breaks))
        else claim.update(updatedBreaks).delete(BreaksInCareSummary) -> Redirect(routes.GBreaksInCareSummary.present)
      }
    )
  }
}
