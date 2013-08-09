package controllers.s11_consent_and_declaration

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain.Consent._
import models.domain.Consent
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._

object G2Consent extends Controller with ConsentAndDeclarationRouting with CachedClaim{
  val form = Form(
    mapping(
      "informationFromEmployer" -> nonEmptyText,
      "why" -> optional(text(maxLength = 300)),
      "informationFromPerson" -> nonEmptyText,
      "whyPerson" -> optional(text(maxLength = 300))
    )(Consent.apply)(Consent.unapply)
      .verifying("why", validateWhy _)
      .verifying("whyPerson", validateWhyPerson _))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s11_consent_and_declaration.g2_consent(form.fill(Consent), completedQuestionGroups(Consent)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s11_consent_and_declaration.g2_consent(formWithErrors,completedQuestionGroups(Consent))),
      consent => claim.update(consent) -> Redirect(routes.G3Disclaimer.present()))
  }
}