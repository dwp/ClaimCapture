package controllers.s11_consent_and_declaration

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._

object G3Disclaimer extends Controller with ConsentAndDeclarationRouting with CachedClaim{
  val form = Form(
    mapping(
      "read" -> nonEmptyText
    )(Disclaimer.apply)(Disclaimer.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s11_consent_and_declaration.g3_disclaimer(form.fill(Disclaimer), completedQuestionGroups(Disclaimer)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s11_consent_and_declaration.g3_disclaimer(formWithErrors,completedQuestionGroups(Disclaimer))),
      disclaimer => claim.update(disclaimer) -> Redirect(routes.G4Declaration.present()))
  }
}