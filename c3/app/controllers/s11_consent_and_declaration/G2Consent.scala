package controllers.s11_consent_and_declaration

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain.Consent._
import models.domain.Consent
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._
import models.yesNo.YesNoWithText
import play.api.data.FormError

object G2Consent extends Controller with ConsentAndDeclarationRouting with CachedClaim {
  val informationFromEmployerMapping =
    "gettingInformationFromAnyEmployer" -> mapping(
      "informationFromEmployer" -> nonEmptyText,
      "why" -> optional(nonEmptyText(maxLength = 300)))(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("required", YesNoWithText.validateOnNo _)

  val informationFromPersonMapping =
    "tellUsWhyEmployer" -> mapping(
      "informationFromPerson" -> nonEmptyText,
      "whyPerson" -> optional(nonEmptyText(maxLength = 300)))(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("required", YesNoWithText.validateOnNo _)
      
  val form = Form(mapping(
    informationFromEmployerMapping,
    informationFromPersonMapping
  )(Consent.apply)(Consent.unapply)
    .verifying("why", validateWhy _)
    .verifying("whyPerson", validateWhyPerson _))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s11_consent_and_declaration.g2_consent(form.fill(Consent), completedQuestionGroups(Consent)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
            .replaceError("gettingInformationFromAnyEmployer", FormError("gettingInformationFromAnyEmployer.why", "error.required"))
            .replaceError("tellUsWhyEmployer", FormError("tellUsWhyEmployer.whyPerson", "error.required"))
        BadRequest(views.html.s11_consent_and_declaration.g2_consent(formWithErrorsUpdate, completedQuestionGroups(Consent)))},
      consent => claim.update(consent) -> Redirect(routes.G3Disclaimer.present()))
  }
}