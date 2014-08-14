package controllers.s2_about_you

import language.reflectiveCalls
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._
import controllers.Mappings.validYesNo
import controllers.Mappings._
import models.domain._
import play.api.Logger

object G8MoreAboutYou extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "maritalStatus" -> nonEmptyText
  )(MoreAboutYou.apply)(MoreAboutYou.unapply))

  def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    claim.questionGroup(OtherEEAStateOrSwitzerland) match {
      case Some(n) => track(MoreAboutYou) { implicit claim => Ok(views.html.s2_about_you.g8_moreAboutYou(form.fill(MoreAboutYou))) }
      case _ => Redirect(startPage)
    }
  }

  def submit = claimingWithCheck { implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g8_moreAboutYou(formWithErrors)),
      moreAboutYou =>  {
        claim.update(moreAboutYou) -> Redirect(controllers.s3_your_partner.routes.G1YourPartnerPersonalDetails.present())
      }
    )
  }
}