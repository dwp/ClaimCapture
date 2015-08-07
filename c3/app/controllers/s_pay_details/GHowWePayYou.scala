package controllers.s_pay_details

import controllers.CarersForms._

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain.HowWePayYou
import utils.helpers.CarersForm._
import PayDetails._

object GHowWePayYou extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "likeToPay" -> carersNonEmptyText(maxLength = 60),
    "paymentFrequency" -> carersNonEmptyText(maxLength = 20)
  )(HowWePayYou.apply)(HowWePayYou.unapply))

  def present = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
    presentConditionally {
      track(HowWePayYou) { implicit claim => Ok(views.html.s_pay_details.g_howWePayYou(form.fill(HowWePayYou))(lang)) }
    }
  }

  def submit = claimingWithCheck { implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s_pay_details.g_howWePayYou(formWithErrors)(lang)),
      howWePayYou => claim.update(howWePayYou) -> Redirect(routes.GBankBuildingSocietyDetails.present()))
  }
}