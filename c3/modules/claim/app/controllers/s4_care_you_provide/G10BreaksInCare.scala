package controllers.s4_care_you_provide

import play.api.mvc.Controller
import play.api.data.Form
import play.api.i18n.Messages
import play.api.data.Forms._
import utils.helpers.CarersForm._
import controllers.Mappings._
import models.domain.BreaksInCare
import models.view.{Navigable, CachedClaim}
import models.yesNo.YesNo
import CareYouProvide.breaksInCare

object G10BreaksInCare extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "answer" -> nonEmptyText.verifying(validYesNo)
  )(YesNo.apply)(YesNo.unapply))

  def present = executeOnForm {implicit claim => implicit request =>
    val filledForm = request.headers.get("referer") match {
      case Some(referer) if referer endsWith routes.G11Break.present().url => form
      case _ if claim.questionGroup[BreaksInCare].isDefined => form.fill(YesNo(no))
      case _ => form
    }

    track(BreaksInCare) { implicit claim => Ok(views.html.s4_care_you_provide.g10_breaksInCare(filledForm, breaksInCare)) }
  }

  def submit = executeOnForm {implicit claim => implicit request =>
    import controllers.Mappings.yes

    def next(hasBreaks: YesNo) = hasBreaks.answer match {
      case `yes` if breaksInCare.breaks.size < 10 => Redirect(routes.G11Break.present())
      case `yes` => Redirect(routes.G10BreaksInCare.present())
      case _ => Redirect(routes.CareYouProvide.completed())
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g10_breaksInCare(formWithErrors, breaksInCare)),
      hasBreaks => claim.update(breaksInCare) -> next(hasBreaks))
  }

  def delete(id: String) = executeOnForm {implicit claim => implicit request =>
    import play.api.libs.json.Json

    claim.questionGroup(BreaksInCare) match {
      case Some(b: BreaksInCare) => {
        val updatedBreaksInCare = b.delete(id)

        if (updatedBreaksInCare.breaks.isEmpty) claim.update(updatedBreaksInCare) -> Ok(Json.obj("answer" -> Messages("answer.label")))
        else claim.update(updatedBreaksInCare) -> Ok(Json.obj("answer" -> Messages("answer.more.label")))
      }

      case _ => BadRequest(s"""Failed to delete break with ID "$id" as claim currently has no breaks""")
    }
  }
}