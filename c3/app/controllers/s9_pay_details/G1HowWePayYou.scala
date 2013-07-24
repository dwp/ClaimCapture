package controllers.s9_pay_details

import language.reflectiveCalls
import play.api.mvc.Controller
import models.view.CachedClaim
import models.domain.HowWePayYou
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._
import controllers.Mappings._

object G1HowWePayYou extends Controller with CachedClaim{
  val form = Form(
    mapping(
      call(routes.G1HowWePayYou.present()),
      "likeToPay" -> nonEmptyText(maxLength = 5),
      "paymentFrequency" -> nonEmptyText(maxLength = 15)
    )(HowWePayYou.apply)(HowWePayYou.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s9_pay_details.g1_howWePayYou(form.fill(HowWePayYou)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s9_pay_details.g1_howWePayYou(formWithErrors)),
      howWePayYou => claim.update(howWePayYou) -> Redirect(routes.G2BankBuildingSocietyDetails.present()))
  }
}