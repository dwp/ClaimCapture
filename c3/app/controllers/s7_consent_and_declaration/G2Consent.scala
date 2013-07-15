package controllers.s7_consent_and_declaration

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import models.domain.{Disclaimer, Claim, Consent, HowWePayYou}
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._

object G2Consent extends Controller with Routing with CachedClaim{

  override val route = Consent.id -> controllers.s7_consent_and_declaration.routes.G2Consent.present

  val form = Form(
    mapping(
      "informationFromEmployer" -> nonEmptyText,
      "why" -> nonEmptyText,
      "informationFromPerson" -> nonEmptyText
    )(Consent.apply)(Consent.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(Consent)

  def present = claiming {
    implicit claim => implicit request =>

      val currentForm: Form[Consent] = claim.questionGroup(Consent) match {
        case Some(t: Consent) => form.fill(t)
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
