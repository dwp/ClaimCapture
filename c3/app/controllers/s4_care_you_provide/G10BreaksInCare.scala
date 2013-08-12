package controllers.s4_care_you_provide

import play.api.mvc.Controller
import play.api.data.Form
import models.view.CachedClaim
import play.api.data.Forms._
import utils.helpers.CarersForm._
import play.api.i18n.Messages
import controllers.Mappings._
import models.domain.BreaksInCare
import models.yesNo.YesNo
import CareYouProvide.breaksInCare

object G10BreaksInCare extends Controller with CareYouProvideRouting with CachedClaim {
  val clearHasBreaks = "clearHasBreaks"

  val form = Form(
    mapping(
      "answer" -> nonEmptyText.verifying(validYesNo)
  )(YesNo.apply)(YesNo.unapply))

  def present = claiming { implicit claim => implicit request =>
    val filledForm = claim.questionGroup[BreaksInCare] match {
      case Some(bs) if request.flash.get(clearHasBreaks).getOrElse(no) == no => form.fill(YesNo(no))
      case _ => form
    }

    Ok(views.html.s4_care_you_provide.g10_breaksInCare(filledForm, breaksInCare, completedQuestionGroups(BreaksInCare)))
  }

  def submit = claiming { implicit claim => implicit request =>
    import controllers.Mappings.yes

    def next(hasBreaks: YesNo) = hasBreaks.answer match {
      case `yes` if breaksInCare.breaks.size < 10 => Redirect(routes.G11Break.present())
      case `yes` => Redirect(routes.G10BreaksInCare.present())
      case _ => Redirect(routes.CareYouProvide.completed())
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_care_you_provide.g10_breaksInCare(formWithErrors, breaksInCare, completedQuestionGroups(BreaksInCare))),
      hasBreaks => claim.update(breaksInCare) -> next(hasBreaks))
  }

  def delete(id: String) = claiming { implicit claim => implicit request =>
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