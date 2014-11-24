package controllers.preview

import play.api.mvc.{Call, EssentialFilter, Controller}
import models.view.{Navigable, CachedClaim}
import models.domain.PreviewModel
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._

object Preview extends Controller with CachedClaim with Navigable {

  val form = Form(mapping(
    "email" -> optional(text(60))
  )(PreviewModel.apply)(PreviewModel.unapply))

  def present = claiming { implicit claim => implicit request => lang =>
    track(models.domain.PreviewModel, beenInPreview = true) { implicit claim => Ok(views.html.preview.preview(form.fill(PreviewModel))(lang)) }
  }

  def back = claiming { implicit claim => implicit request => implicit lang =>
    resetPreviewState { implicit claim => Redirect(claim.navigation.previousIgnorePreview.toString) }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => lang =>
    form.bindEncrypted.fold(
      errors => BadRequest(views.html.preview.preview(errors)(lang)),
      data => claim.update(data) -> Redirect(controllers.s12_consent_and_declaration.routes.G2Disclaimer.present)
    )
  }
}
