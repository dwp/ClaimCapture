package controllers.s7_consent_and_declaration

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import models.domain.{Consent, HowWePayYou}
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._

object G1Consent extends Controller with Routing with CachedClaim{

  override val route = Consent.id -> controllers.s7_consent_and_declaration.routes.G1Consent.present

  val form = Form(
    mapping(
      "informationFromEmployer" -> nonEmptyText,
      "why" -> nonEmptyText,
      "informationFromPerson" -> nonEmptyText
    )(Consent.apply)(Consent.unapply))

  def present = claiming {
    implicit claim => implicit request =>

      val currentForm: Form[Consent] = claim.questionGroup(Consent) match {
        case Some(t: Consent) => form.fill(t)
        case _ => form
      }

      Ok(views.html.s7_consent_and_declaration.g1_consent(currentForm))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_consent_and_declaration.g1_consent(formWithErrors)),
      consent => claim.update(consent) -> Redirect(routes.G2Disclaimer.present()))
  }


}
