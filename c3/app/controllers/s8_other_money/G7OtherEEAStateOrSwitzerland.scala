package controllers.s8_other_money

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import models.view.CachedClaim
import models.domain.{Claim, OtherEEAStateOrSwitzerland}
import controllers.Mappings._
import models.yesNo.YesNoWithText
import utils.helpers.CarersForm._

object G7OtherEEAStateOrSwitzerland extends Controller with CachedClaim {
  val benefitsFromOtherEEAStateOrSwitzerlandMapping =
    "benefitsFromOtherEEAStateOrSwitzerland" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "details" -> optional(nonEmptyText(maxLength = 200))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("required", YesNoWithText.validateOnYes _)

  val form = Form(
    mapping(
      call(routes.G7OtherEEAStateOrSwitzerland.present()),
      benefitsFromOtherEEAStateOrSwitzerlandMapping,
      "workingForOtherEEAStateOrSwitzerland" -> nonEmptyText.verifying(validYesNo)
    )(OtherEEAStateOrSwitzerland.apply)(OtherEEAStateOrSwitzerland.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(OtherEEAStateOrSwitzerland)

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s8_other_money.g7_otherEEAStateOrSwitzerland(form.fill(OtherEEAStateOrSwitzerland), completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors.replaceError("benefitsFromOtherEEAStateOrSwitzerland", FormError("benefitsFromOtherEEAStateOrSwitzerland.details", "error.required"))
        BadRequest(views.html.s8_other_money.g7_otherEEAStateOrSwitzerland(formWithErrorsUpdate, completedQuestionGroups))
      },
      benefitsFromOtherEEAStateOrSwitzerland => claim.update(benefitsFromOtherEEAStateOrSwitzerland) -> Redirect(routes.OtherMoney.completed()))
  }
}