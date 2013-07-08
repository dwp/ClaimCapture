package controllers.s6_pay_details

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import models.domain.{HowWePayYou}
import play.api.data.Form
import play.api.data.Forms._
import scala.Some
import utils.helpers.CarersForm._

object G1HowWePayYou extends Controller with Routing with CachedClaim{

  override val route = HowWePayYou.id -> controllers.s6_pay_details.routes.G1HowWePayYou.present

  val form = Form(
    mapping(
      "likeToPay" -> nonEmptyText,
      "paymentFrequency" -> nonEmptyText
    )(HowWePayYou.apply)(HowWePayYou.unapply))

  def present = claiming {
    implicit claim => implicit request =>

      val currentForm: Form[HowWePayYou] = claim.questionGroup(HowWePayYou) match {
        case Some(t: HowWePayYou) => form.fill(t)
        case _ => form
      }

      Ok(views.html.s6_pay_details.g1_howWePayYou(currentForm))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s6_pay_details.g1_howWePayYou(formWithErrors)),
      howWePayYou => claim.update(howWePayYou) -> Redirect(routes.G2BankBuildingSocietyDetails.present()))
  }


}
