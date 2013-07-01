package controllers.s4_care_you_provide

import play.api.mvc.Controller
import play.api.data.Form
import models.view.CachedClaim
import models.domain._
import controllers.Routing
import play.api.data.Forms._
import utils.helpers.CarersForm._
import play.api.i18n.Messages

object G10BreaksInCare extends Controller with Routing with CachedClaim {
  override val route = BreaksInCare.id -> routes.G10BreaksInCare.present

  val form = Form(
    mapping(
      "answer" -> nonEmptyText
    )(HasBreaks.apply)(HasBreaks.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.CareYouProvide.id).takeWhile(q => q.id != BreaksInCare.id)

  def present = claiming { implicit claim => implicit request =>
    val breaksInCare = claim.questionGroup(BreaksInCare.id) match {
      case Some(b: BreaksInCare) => b
      case _ => BreaksInCare()
    }

    Ok(views.html.s4_careYouProvide.g10_breaksInCare(form, breaksInCare, completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    val breaksInCare = claim.questionGroup(BreaksInCare.id) match {
      case Some(b: BreaksInCare) => b
      case _ => BreaksInCare()
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_careYouProvide.g10_breaksInCare(formWithErrors, breaksInCare, completedQuestionGroups)),
      hasBreaks => hasBreaks.answer match {
        case "yes" if breaksInCare.breaks.size < 10 => claim.update(breaksInCare) -> Redirect(routes.G11Break.present)
        case "yes" => claim.update(breaksInCare) -> Redirect(routes.G10BreaksInCare.present)
        case _ => claim.update(breaksInCare) -> Redirect(routes.CareYouProvide.completed)
      }
    )
  }

  def delete(id: String) = claiming { implicit claim => implicit request =>
    import play.api.libs.json.Json

    claim.questionGroup(BreaksInCare.id) match {
      case Some(b: BreaksInCare) => {
        val updatedBreaksInCare = b.delete(id)
        val dateOfClaim = claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.ddMMyyyy)

        if (updatedBreaksInCare.breaks.isEmpty) claim.update(updatedBreaksInCare) -> Ok(Json.obj("answer" -> Messages("answer.label", dateOfClaim)))
        else claim.update(updatedBreaksInCare) -> Ok(Json.obj("answer" -> Messages("answer.more.label", dateOfClaim)))
      }

      case _ => BadRequest(s"""Failed to delete break with ID "$id" as claim currently has no breaks""")
    }
  }
}