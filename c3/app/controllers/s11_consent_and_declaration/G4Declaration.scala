package controllers.s11_consent_and_declaration

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._

object G4Declaration extends Controller with ConsentAndDeclarationRouting with CachedClaim{
  val form = Form(mapping(
    "confirm" -> nonEmptyText,
    "someoneElse" -> optional(text)
  )(Declaration.apply)(Declaration.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s11_consent_and_declaration.g4_declaration(form.fill(Declaration), completedQuestionGroups(Declaration)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s11_consent_and_declaration.g4_declaration(formWithErrors,completedQuestionGroups(Declaration))),
      declaration => claim.update(declaration) -> Redirect(routes.G5Submit.present()))
  }
}