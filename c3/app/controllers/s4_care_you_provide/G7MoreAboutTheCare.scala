package controllers.s4_care_you_provide

import language.reflectiveCalls
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.mappings.Mappings._
import utils.helpers.CarersForm._
import models.view.{Navigable, CachedClaim}
import models.domain.MoreAboutTheCare
import models.yesNo.YesNoWithDate

object G7MoreAboutTheCare extends Controller with CachedClaim with Navigable {

  val form = Form(mapping(
    "spent35HoursCaring" -> nonEmptyText.verifying(validYesNo)
  )(MoreAboutTheCare.apply)(MoreAboutTheCare.unapply))

  def present = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    track(MoreAboutTheCare) { implicit claim => Ok(views.html.s4_care_you_provide.g7_moreAboutTheCare(form.fill(MoreAboutTheCare))(lang)) }
  }

  def submit = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        BadRequest(views.html.s4_care_you_provide.g7_moreAboutTheCare(formWithErrors)(lang))
      },
      moreAboutTheCare => claim.update(moreAboutTheCare) -> Redirect(routes.G10BreaksInCare.present())
    )
  } withPreview()
}