package controllers.s4_care_you_provide

import play.api.mvc.{Request, Controller}
import play.api.data.{FormError, Form}
import play.api.i18n.{MMessages => Messages}
import play.api.data.Forms._
import utils.helpers.CarersForm._
import controllers.Mappings._
import models.domain.{QuestionGroup, Claim, MoreAboutTheCare, BreaksInCare}
import models.view.{Navigable, CachedClaim}
import models.yesNo.{DeleteId, YesNo}
import models.DayMonthYear._
import scala.language.postfixOps
import play.api.i18n.Lang

object G10BreaksInCare extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "answer" -> nonEmptyText.verifying(validYesNo)
  )(YesNo.apply)(YesNo.unapply))

  def fillForm(implicit request:Request[_],claim:Claim)= {
    request.headers.get("referer") match {
      case Some(referer) if referer endsWith routes.G11Break.present().url => form
      case _ if claim.questionGroup[BreaksInCare].isDefined => form.fill(YesNo(no))
      case _ => form
    }
  }
  def present = claimingWithCheck { implicit claim => implicit request =>  lang =>

    track(BreaksInCare) { implicit claim => Ok(views.html.s4_care_you_provide.g10_breaksInCare(fillForm, breaksInCare)(lang)) }
  }

  def breaksInCare(implicit claim: Claim) = claim.questionGroup[BreaksInCare].getOrElse(BreaksInCare())

  def submit = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    import controllers.Mappings.yes

    def next(hasBreaks: YesNo) = hasBreaks.answer match {
      case `yes` if breaksInCare.breaks.size < 10 => Redirect(routes.G11Break.present())
      case `yes` => Redirect(routes.G10BreaksInCare.present())
      case _ => redirect(claim, lang)
    }

    form.bindEncrypted.fold(
      formWithErrors => {
        val sixMonth = claim.questionGroup(MoreAboutTheCare) match {
          case Some(m: MoreAboutTheCare) => m.spent35HoursCaringBeforeClaim.answer.toLowerCase == "yes"
          case _ => false
        }
        val formWithErrorsUpdate = formWithErrors.replaceError("answer", FormError("answer.label", "error.required",Seq(claim.dateOfClaim.fold("{NO CLAIM DATE}"){dmy =>
          val datemy = if (sixMonth) dmy - 6 months else dmy
          breaksInCare.breaks.map(_.start).sorted.find(_.isBefore(datemy)).getOrElse(datemy).`dd/MM/yyyy`
        })))
        BadRequest(views.html.s4_care_you_provide.g10_breaksInCare(formWithErrorsUpdate, breaksInCare)(lang))
      },
      hasBreaks => claim.update(breaksInCare) -> next(hasBreaks))
  }

  private def redirect(implicit claim: Claim, lang: Lang) = {
    if (completedQuestionGroups.isEmpty) Redirect(routes.G1TheirPersonalDetails.present())
    else Redirect("/education/your-course-details")
  }

  private def completedQuestionGroups(implicit claim: Claim): List[QuestionGroup] = {
    claim.completedQuestionGroups(models.domain.CareYouProvide)
  }

  val deleteForm = Form(mapping(
    "deleteId" -> nonEmptyText
  )(DeleteId.apply)(DeleteId.unapply))

  def delete = claimingWithCheck { implicit claim => implicit request =>  lang =>

    deleteForm.bindEncrypted.fold(
      errors    =>  BadRequest(views.html.s4_care_you_provide.g10_breaksInCare(fillForm, breaksInCare)(lang))),
      deleteForm=>  {
        val updatedBreaks = breaksInCare.delete(deleteForm.id)
        if (updatedBreaks.breaks == breaksInCare.breaks) BadRequest(views.html.s4_care_you_provide.g10_breaksInCare(fillForm, breaksInCare)(lang))
        else claim.update(updatedBreaks) -> Redirect(controllers.s4_care_you_provide.routes.G10BreaksInCare.present)
      }
    )
  }
}