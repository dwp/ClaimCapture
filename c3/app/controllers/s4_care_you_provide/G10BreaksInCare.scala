package controllers.s4_care_you_provide

import controllers.IterationID
import play.api.mvc.Controller
import play.api.data.{FormError, Form}
import play.api.i18n.{MMessages => Messages}
import play.api.data.Forms._
import utils.helpers.CarersForm._
import controllers.mappings.Mappings._
import models.domain._
import models.view.{Navigable, CachedClaim}
import scala.language.postfixOps
import play.api.i18n.Lang
import controllers.mappings.Mappings
import models.domain.Claim
import models.yesNo.DeleteId

object G10BreaksInCare extends Controller with CachedClaim with Navigable {

  val form = Form(mapping(
    "answer" -> nonEmptyText.verifying(validYesNo)
  )(BreaksInCareSummary.apply)(BreaksInCareSummary.unapply))


  def present = claimingWithCheck { implicit claim => implicit request =>  lang =>

    track(BreaksInCare) { implicit claim => Ok(views.html.s4_care_you_provide.g10_breaksInCare(form, breaksInCare)(lang)) }
  }

  def breaksInCare(implicit claim: Claim) = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

  def submit = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    import controllers.mappings.Mappings.yes

    def next(hasBreaks:String) = hasBreaks match {
      case `yes` if breaksInCare.breaks.size < 10 => Redirect(routes.G11Break.present(IterationID(form)))
      case _ => redirect(claim, lang)
    }

    form.bindEncrypted.fold(
      formWithErrors => {
        val sixMonth = claim.questionGroup(MoreAboutTheCare) match {
          case Some(m: MoreAboutTheCare) => m.spent35HoursCaringBeforeClaim.answer.toLowerCase == "yes"
          case _ => false
        }
        val formWithErrorsUpdate = formWithErrors.replaceError("answer", FormError("answer.label", Mappings.errorRequired,Seq(claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy =>
          if (sixMonth) (dmy - 6 months).`dd/MM/yyyy` else dmy.`dd/MM/yyyy`))))
        BadRequest(views.html.s4_care_you_provide.g10_breaksInCare(formWithErrorsUpdate, breaksInCare)(lang))
      },
      hasBreaks => claim.update(hasBreaks) -> next(hasBreaks.answer))
  }.withPreviewConditionally[BreaksInCareSummary](breaksInCareSummary => breaksInCareSummary._2.answer == Mappings.no)

  private def redirect(implicit claim: Claim, lang: Lang) = {
    if (completedQuestionGroups.isEmpty) Redirect(routes.G1TheirPersonalDetails.present())
    else Redirect(controllers.s6_education.routes.G1YourCourseDetails.present)
  }

  private def completedQuestionGroups(implicit claim: Claim): List[QuestionGroup] = {
    claim.completedQuestionGroups(models.domain.CareYouProvide)
  }

  val deleteForm = Form(mapping(
    "deleteId" -> nonEmptyText
  )(DeleteId.apply)(DeleteId.unapply))

  def delete = claimingWithCheck { implicit claim => implicit request =>  lang =>

    deleteForm.bindEncrypted.fold(
      errors    =>  BadRequest(views.html.s4_care_you_provide.g10_breaksInCare(form, breaksInCare)(lang)),
      deleteForm=>  {
        val updatedBreaks = breaksInCare.delete(deleteForm.id)
        if (updatedBreaks.breaks == breaksInCare.breaks) BadRequest(views.html.s4_care_you_provide.g10_breaksInCare(form, breaksInCare)(lang))
        else claim.update(updatedBreaks) -> Redirect(controllers.s4_care_you_provide.routes.G10BreaksInCare.present)
      }
    )
  }
}