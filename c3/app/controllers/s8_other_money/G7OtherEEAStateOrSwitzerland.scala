package controllers.s8_other_money

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.CachedClaim
import models.domain.{Claim, OtherEEAStateOrSwitzerland}
import controllers.Mappings._
import utils.helpers.CarersForm._

object G7OtherEEAStateOrSwitzerland extends Controller with CachedClaim {
  val form = Form(
    mapping(
      call(routes.G7OtherEEAStateOrSwitzerland.present()),
      "benefitsFromOtherEEAStateOrSwitzerland" -> nonEmptyText.verifying(validYesNo),
      "workingForOtherEEAStateOrSwitzerland" -> nonEmptyText.verifying(validYesNo)
    )(OtherEEAStateOrSwitzerland.apply)(OtherEEAStateOrSwitzerland.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(OtherEEAStateOrSwitzerland)

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s8_other_money.g7_otherEEAStateOrSwitzerland(form.fill(OtherEEAStateOrSwitzerland), completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s8_other_money.g7_otherEEAStateOrSwitzerland(formWithErrors, completedQuestionGroups)),
      benefitsFromOtherEEAStateOrSwitzerland => claim.update(benefitsFromOtherEEAStateOrSwitzerland) -> Redirect(routes.OtherMoney.completed()))
  }
}