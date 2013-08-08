package controllers.s9_other_money

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.CachedClaim
import models.domain.OtherEEAStateOrSwitzerland
import controllers.Mappings._
import utils.helpers.CarersForm._

object G7OtherEEAStateOrSwitzerland extends Controller with OtherMoneyRouting with CachedClaim {
  val form = Form(
    mapping(
      "benefitsFromOtherEEAStateOrSwitzerland" -> nonEmptyText.verifying(validYesNo),
      "workingForOtherEEAStateOrSwitzerland" -> nonEmptyText.verifying(validYesNo)
    )(OtherEEAStateOrSwitzerland.apply)(OtherEEAStateOrSwitzerland.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s9_other_money.g7_otherEEAStateOrSwitzerland(form.fill(OtherEEAStateOrSwitzerland), completedQuestionGroups(OtherEEAStateOrSwitzerland)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s9_other_money.g7_otherEEAStateOrSwitzerland(formWithErrors, completedQuestionGroups(OtherEEAStateOrSwitzerland))),
      benefitsFromOtherEEAStateOrSwitzerland => claim.update(benefitsFromOtherEEAStateOrSwitzerland) -> Redirect(routes.OtherMoney.completed()))
  }
}