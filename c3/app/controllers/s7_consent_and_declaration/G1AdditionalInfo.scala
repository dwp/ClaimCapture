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

object G1AdditionalInfo extends Controller with Routing with CachedClaim{

  override val route = AdditionalInfo.id -> controllers.s7_consent_and_declaration.routes.G1AdditionalInfo.present

  val form = Form(
    mapping(
      "anythingElse" -> optional(text(maxLength = 2000)),
      "welshCommunication" -> nonEmptyText
    )(AdditionalInfo.apply)(AdditionalInfo.unapply))


  def present = claiming {
    implicit claim => implicit request =>

      val currentForm: Form[AdditionalInfo] = claim.questionGroup(AdditionalInfo) match {
        case Some(t: AdditionalInfo) => form.fill(t)
        case _ => form
      }

      Ok(views.html.s7_consent_and_declaration.g1_additionalInfo(currentForm))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_consent_and_declaration.g1_additionalInfo(formWithErrors)),
      additionalInfo => claim.update(additionalInfo) -> Redirect(routes.G2Consent.present()))
  }


}
