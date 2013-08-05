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

object G3Disclaimer extends Controller with CachedClaim{
  val form = Form(
    mapping(
      call(routes.G3Disclaimer.present()),
      "read" -> nonEmptyText
    )(Disclaimer.apply)(Disclaimer.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(Disclaimer)

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s10_consent_and_declaration.g3_disclaimer(form.fill(Disclaimer), completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s10_consent_and_declaration.g3_disclaimer(formWithErrors,completedQuestionGroups)),
      disclaimer => claim.update(disclaimer) -> Redirect(routes.G4Declaration.present()))
  }
}