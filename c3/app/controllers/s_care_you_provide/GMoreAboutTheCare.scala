package controllers.s_care_you_provide

import controllers.s_breaks.GBreaksInCare

import language.reflectiveCalls
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.mappings.Mappings._
import utils.helpers.CarersForm._
import models.view.{Navigable, CachedClaim}
import models.domain.MoreAboutTheCare
import models.yesNo.YesNoWithDate

object GMoreAboutTheCare extends Controller with CachedClaim with Navigable {

  val form = Form(mapping(
    "spent35HoursCaring" -> nonEmptyText.verifying(validYesNo)
  )(MoreAboutTheCare.apply)(MoreAboutTheCare.unapply))

  def present = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    track(MoreAboutTheCare) { implicit claim => Ok(views.html.s_care_you_provide.g_moreAboutTheCare(form.fill(MoreAboutTheCare))(lang)) }
  }

  def submit = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        BadRequest(views.html.s_care_you_provide.g_moreAboutTheCare(formWithErrors)(lang))
      },
      moreAboutTheCare => claim.update(moreAboutTheCare) -> Redirect(controllers.s_breaks.routes.GBreaksInCare.present())
    )
  } withPreview()
}