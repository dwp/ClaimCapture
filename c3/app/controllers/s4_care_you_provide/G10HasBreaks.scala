package controllers.s4_care_you_provide

import play.api.mvc.Controller
import play.api.data.Form
import models.view.CachedClaim
import models.domain._
import controllers.Routing
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._

object G10HasBreaks extends Controller with Routing with CachedClaim {
  override val route = HasBreaks.id -> controllers.s4_care_you_provide.routes.G10HasBreaks.present

  val form = Form(
    mapping(
      "answer" -> nonEmptyText
    )(HasBreaks.apply)(HasBreaks.unapply))

  def present = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(CareYouProvide.id).takeWhile(q => q.id != HasBreaks.id)

    val hasBreaksForm = claim.questionGroup(HasBreaks.id) match {
      case Some(h: HasBreaks) => form.fill(h)
      case _ => form
    }

    val breaksInCare = claim.questionGroup(BreaksInCare.id) match {
      case Some(b: BreaksInCare) => b
      case _ => BreaksInCare()
    }

    Ok(views.html.s4_careYouProvide.g10_hasBreaks(hasBreaksForm, breaksInCare, completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(CareYouProvide.id).takeWhile(q => q.id != HasBreaks.id)

    val breaksInCare = claim.questionGroup(BreaksInCare.id) match {
      case Some(b: BreaksInCare) => b
      case _ => BreaksInCare()
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s4_careYouProvide.g10_hasBreaks(formWithErrors, breaksInCare, completedQuestionGroups)),
      hasBreaks =>
        if (hasBreaks.answer == yes) claim.update(hasBreaks) -> Redirect(routes.G11BreaksInCare.present)
        else claim.update(hasBreaks).delete(BreaksInCare.id) -> Redirect(controllers.routes.CareYouProvide.completed))
  }
}