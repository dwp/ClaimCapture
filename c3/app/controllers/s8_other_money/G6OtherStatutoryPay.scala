package controllers.s8_other_money

import play.api.mvc.Controller
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.{Claim, OtherStatutoryPay}

object G6OtherStatutoryPay extends Controller with CachedClaim {

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(OtherStatutoryPay)

  val form = Form(
    mapping(
      "answer" -> text,
       call(routes.G6OtherStatutoryPay.present())
    )(OtherStatutoryPay.apply)(OtherStatutoryPay.unapply))

  def present = claiming { implicit claim =>
    implicit request =>
      Ok(views.html.s8_other_money.g6_otherStatutoryPay(form, completedQuestionGroups))
  }

  def submit = claiming { implicit claim =>
    implicit request =>
      Ok(views.html.s8_other_money.g6_otherStatutoryPay(form, completedQuestionGroups))
  }

}
