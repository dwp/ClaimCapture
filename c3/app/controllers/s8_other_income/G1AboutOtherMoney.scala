package controllers.s8_other_income

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{Claim, AboutOtherMoney}
import utils.helpers.CarersForm._
import controllers.Mappings._

object G1AboutOtherMoney extends Controller with Routing with CachedClaim {
  override val route = AboutOtherMoney.id -> routes.G1AboutOtherMoney.present

  val form = Form(
    mapping(
      "yourBenefits" -> optional(text),
      "partnerBenefits" -> optional(text)
    )(AboutOtherMoney.apply)(AboutOtherMoney.unapply))
    
  def present = claiming {
    implicit claim => implicit request =>

      ???
  }
}