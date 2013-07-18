package controllers.s8_other_money

import play.api.mvc.Controller
import controllers.Routing
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{ Claim, MoneyPaidToSomeoneElseForYou }
import utils.helpers.CarersForm._
import controllers.Mappings._
import models.domain.PersonWhoGetsThisMoney
import models.domain.AboutOtherMoney

object G3PersonWhoGetsThisMoney extends Controller with Routing with CachedClaim {
  override val route = PersonWhoGetsThisMoney.id -> routes.G3PersonWhoGetsThisMoney.present

  val form = Form(
    mapping(
      "fullName" -> nonEmptyText(maxLength = sixty),
      "nationalInsuranceNumber" -> optional(nino.verifying(validNinoOnly)),
      "nameOfBenefit" -> nonEmptyText(maxLength = sixty))(PersonWhoGetsThisMoney.apply)(PersonWhoGetsThisMoney.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(PersonWhoGetsThisMoney)
      
  def present = claiming {
    implicit claim => implicit request =>
      OtherMoney.whenVisible(claim)(() => {
        val currentForm: Form[PersonWhoGetsThisMoney] = claim.questionGroup(PersonWhoGetsThisMoney) match {
          case Some(t: PersonWhoGetsThisMoney) => form.fill(t)
          case _ => form
        }
        Ok(views.html.s8_other_money.g3_personWhoGetsThisMoney(currentForm, completedQuestionGroups))
      })
  }
  
  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s8_other_money.g3_personWhoGetsThisMoney(formWithErrors, completedQuestionGroups)),
        f => claim.update(f) -> Redirect(controllers.routes.ThankYou.present())) // TODO replace with next page to go to
  }
}