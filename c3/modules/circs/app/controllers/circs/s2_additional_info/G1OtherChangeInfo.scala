package controllers.circs.s2_additional_info

import play.api.mvc.Controller
import models.view.{Navigable, CachedCircs}
import play.api.data.Form
import play.api.data.Forms._
import models.domain.CircumstancesOtherInfo
import utils.helpers.CarersForm._

object G1OtherChangeInfo extends Controller with CachedCircs with Navigable {

  val change = "changeInCircs"

  val form = Form(mapping(
    change -> text(maxLength = 2000)
  )(CircumstancesOtherInfo.apply)(CircumstancesOtherInfo.unapply))

  def present = executeOnForm { implicit claim => implicit request =>
    track(CircumstancesOtherInfo) { implicit claim => Ok(views.html.circs.s2_additional_info.g1_otherChangeInfo(form.fill(CircumstancesOtherInfo))) }
  }

  def submit = executeOnForm { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circs.s2_additional_info.g1_otherChangeInfo(formWithErrors)),
      data => claim.update(data) -> Redirect(controllers.circs.s3_consent_and_declaration.routes.G1Declaration.present()))
  }

}
