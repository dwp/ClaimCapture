package controllers.preview

import play.api.mvc.{EssentialFilter, Controller}
import models.view.{Navigable, CachedClaim}
import models.domain.PreviewModel
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._

object Preview extends Controller with CachedClaim with Navigable {

  val form = Form(mapping(
    "email" -> optional(text(60))
  )(PreviewModel.apply)(PreviewModel.unapply))

  def present = claiming {implicit claim =>  implicit request =>  lang =>
    track(models.domain.PreviewModel) { implicit claim => Ok(views.html.preview.preview(form.fill(PreviewModel))(claim,request,lang)) }
  }

  def submit = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      errors => BadRequest(views.html.preview.preview(errors)(claim,request,lang)),
      data => claim.update(data) -> Redirect(controllers.s10_information.routes.G1AdditionalInfo.present)
    )
  }
}
