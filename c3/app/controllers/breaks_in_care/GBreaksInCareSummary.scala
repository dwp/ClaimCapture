package controllers.breaks_in_care

import controllers.mappings.Mappings
import controllers.mappings.Mappings._
import models.domain._
import models.view.CachedClaim
import play.api.Play._
import play.api.data.Forms._
import play.api.i18n.{Lang, I18nSupport, MMessages, MessagesApi}
import play.api.mvc.Controller
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
          .replaceError("", "mustselectother", FormError("breaksummary_other", errorRequired, Seq(dateForBreaks(claim, request2lang), dpname(claim))))
        BadRequest(views.html.breaks_in_care.breaksInCareSummary(errors, breaks))
      },
      breaksInCareSummary => {
        claim.update(breaksInCareSummary) -> Redirect(routes.GBreaksInCareSummary.present())
      })
  }

  private def dpname(claim: Claim) = {
    val theirPersonalDetails = claim.questionGroup(TheirPersonalDetails).getOrElse(TheirPersonalDetails()).asInstanceOf[TheirPersonalDetails]
    theirPersonalDetails.firstName + " " + theirPersonalDetails.surname
  }

  private def dateForBreaks(claim: Claim, lang: Lang) = {
    val claimDateQG = claim.questionGroup[ClaimDate].getOrElse(ClaimDate())
    claimDateQG.dateWeRequireBreakInCareInformationFrom(lang)
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
}
