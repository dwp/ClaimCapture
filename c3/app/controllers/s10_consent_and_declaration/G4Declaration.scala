package controllers.s10_consent_and_declaration

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._
import models.domain.Claim
import controllers.Mappings._

object G4Declaration extends Controller with CachedClaim{
  val form = Form(
    mapping(
      call(routes.G4Declaration.present()),
      "confirm" -> nonEmptyText,
      "someoneElse" -> optional(text)
    )(Declaration.apply)(Declaration.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(Declaration)

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s10_consent_and_declaration.g4_declaration(form.fill(Declaration), completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s10_consent_and_declaration.g4_declaration(formWithErrors,completedQuestionGroups)),
      declaration => claim.update(declaration) -> Redirect(routes.G5Submit.present()))
  }
}