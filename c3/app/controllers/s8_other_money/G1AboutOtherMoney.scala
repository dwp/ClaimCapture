package controllers.s8_other_money

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{ Claim, AboutOtherMoney }
import utils.helpers.CarersForm._
import controllers.Mappings._

object G1AboutOtherMoney extends Controller with Routing with CachedClaim {
  override val route = AboutOtherMoney.id -> routes.G1AboutOtherMoney.present

  val form = Form(
    mapping(
      "yourBenefits" -> optional(text),
      "partnerBenefits" -> optional(text))(AboutOtherMoney.apply)(AboutOtherMoney.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(AboutOtherMoney)

  def present = claiming {
    implicit claim =>
      implicit request =>
        val currentForm: Form[AboutOtherMoney] = claim.questionGroup(AboutOtherMoney) match {
          case Some(m: AboutOtherMoney) => form.fill(m)
          case _ => form
        }

        Ok(views.html.s8_other_money.g1_aboutOtherMoney(currentForm, completedQuestionGroups))
  }

  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s8_other_money.g1_aboutOtherMoney(formWithErrors, completedQuestionGroups)),
        f => claim.update(f) -> Redirect(controllers.routes.ThankYou.present())) // TODO replace with next page to go to
  }
}