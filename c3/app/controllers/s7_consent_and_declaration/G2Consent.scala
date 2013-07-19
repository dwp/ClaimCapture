package controllers.s7_consent_and_declaration

import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain.Consent._
import models.domain.{Claim, Consent}
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._

object G2Consent extends Controller with CachedClaim{
  val form = Form(
    mapping(
      "call" -> ignored(routes.G2Consent.present()),
      "informationFromEmployer" -> nonEmptyText,
      "why" -> optional(text),
      "informationFromPerson" -> nonEmptyText,
      "whyPerson" -> optional(text)
    )(Consent.apply)(Consent.unapply)
      .verifying("why", validateWhy _)
      .verifying("whyPerson", validateWhyPerson _))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(Consent)

  def present = claiming { implicit claim => implicit request =>
    val currentForm: Form[Consent] = claim.questionGroup(Consent) match {
      case Some(c: Consent) => form.fill(c)
      case _ => form
    }

    Ok(views.html.s7_consent_and_declaration.g2_consent(currentForm,completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_consent_and_declaration.g2_consent(formWithErrors,completedQuestionGroups)),
      consent => claim.update(consent) -> Redirect(routes.G3Disclaimer.present()))
  }
}