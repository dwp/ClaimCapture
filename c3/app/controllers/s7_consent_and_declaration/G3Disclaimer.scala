package controllers.s7_consent_and_declaration

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import scala.Some
import utils.helpers.CarersForm._
import models.domain.Claim
import scala.Some
import play.api.Logger

object G3Disclaimer extends Controller with Routing with CachedClaim{

  override val route = Disclaimer.id -> controllers.s7_consent_and_declaration.routes.G3Disclaimer.present

  val form = Form(
    mapping(
      "read" -> nonEmptyText
    )(Disclaimer.apply)(Disclaimer.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(Disclaimer)

  def present = claiming {
    implicit claim => implicit request =>

      val currentForm: Form[Disclaimer] = claim.questionGroup(Disclaimer) match {
        case Some(t: Disclaimer) => form.fill(t)
        case _ => form
      }
      val completed = completedQuestionGroups
      Ok(views.html.s7_consent_and_declaration.g3_disclaimer(currentForm,completed))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_consent_and_declaration.g3_disclaimer(formWithErrors,completedQuestionGroups)),
      disclaimer => claim.update(disclaimer) -> Redirect(routes.G4Declaration.present()))
  }


}
