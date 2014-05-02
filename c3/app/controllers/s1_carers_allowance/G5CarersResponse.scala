package controllers.s1_carers_allowance

import play.api.mvc.Controller
import models.view.CachedClaim
import models.view.Navigable
import play.api.data.Form
import play.api.data.Forms._
import models.domain.JSEnabled
import play.api.Logger

object G5CarersResponse extends Controller with CachedClaim with Navigable {

  val form = Form(mapping(
    "jsEnabled" -> boolean
  )(JSEnabled.apply)(JSEnabled.unapply))

  def present = claiming {
    implicit claim => implicit request => implicit lang =>
      track(models.domain.AboutYou) {
        implicit claim => Ok(views.html.s1_carers_allowance.g5_carersResponse())
      }
  }

  def submit = claiming {
    implicit claim => implicit request => implicit lang =>
      form.bindFromRequest.fold(
        formWithErrors => {
          NotFound
        },
        f => {
          if (!f.jsEnabled) {
            Logger.info(s"No JS - Start ${claim.key}")
          }
          Redirect("/about-you/your-details")
        })
  }
}