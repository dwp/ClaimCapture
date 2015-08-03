package controllers.s_about_you

import controllers.CarersForms._
import controllers.mappings.Mappings._
import controllers.s_about_you.GNationalityAndResidency._
import models.domain.NationalityAndResidency
import models.view.{Navigable, CachedClaim}
import models.yesNo.YesNoWithText
import models.domain.MaritalStatus
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.mvc.Controller
import utils.helpers.CarersForm._


object GMaritalStatus extends Controller with CachedClaim with Navigable {

  val form = Form(mapping(
    "maritalStatus" -> carersNonEmptyText
  )(MaritalStatus.apply)(MaritalStatus.unapply))

  def present = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    track(MaritalStatus) { implicit claim =>
      Ok(views.html.s_about_you.g_maritalStatus(form.fill(MaritalStatus))(lang))
    }
  }

  def submit = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors =>
        BadRequest(views.html.s_about_you.g_maritalStatus(formWithErrors)(lang)),
      maritalStatus => claim.update(maritalStatus) -> Redirect(routes.GContactDetails.present()))
  } withPreview()
}
