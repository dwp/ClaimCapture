package controllers.s5_time_spent_abroad

import play.api.mvc.Controller
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import models.view.CachedClaim
import controllers.Routing
import models.domain.{Claim, OtherEEAStateOrSwitzerland}
import controllers.Mappings._
import models.yesNo.YesNoWithText
import utils.helpers.CarersForm._

object G5otherEEAStateOrSwitzerland extends Controller with Routing with CachedClaim {
  override val route = OtherEEAStateOrSwitzerland.id -> routes.G5otherEEAStateOrSwitzerland.present

  val benefitsFromOtherEEAStateOrSwitzerlandMapping =
    "benefitsFromOtherEEAStateOrSwitzerland" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "details" -> optional(nonEmptyText(maxLength = 200))
    )(YesNoWithText.apply)(YesNoWithText.unapply)
      .verifying("required", YesNoWithText.validateOnNo _)

  val form = Form(
    mapping(
      benefitsFromOtherEEAStateOrSwitzerlandMapping,
      "workingForOtherEEAStateOrSwitzerland" -> nonEmptyText.verifying(validYesNo)
    )(OtherEEAStateOrSwitzerland.apply)(OtherEEAStateOrSwitzerland.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(OtherEEAStateOrSwitzerland)

  def present = claiming { implicit claim => implicit request =>
    val currentForm: Form[OtherEEAStateOrSwitzerland] = claim.questionGroup(OtherEEAStateOrSwitzerland) match {
      case Some(o: OtherEEAStateOrSwitzerland) => form.fill(o)
      case _ => form
    }

    Ok(views.html.s5_time_spent_abroad.g5_otherEEAStateOrSwitzerland(currentForm, completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors.replaceError("benefitsFromOtherEEAStateOrSwitzerland", FormError("benefitsFromOtherEEAStateOrSwitzerland.details", "error.required"))
        BadRequest(views.html.s5_time_spent_abroad.g5_otherEEAStateOrSwitzerland(formWithErrorsUpdate, completedQuestionGroups))
      },
      benefitsFromOtherEEAStateOrSwitzerland => claim.update(benefitsFromOtherEEAStateOrSwitzerland) -> Redirect(routes.G5otherEEAStateOrSwitzerland.present())) // TODO COMPLETED
  }
}