package controllers.s4_care_you_provide

import models.domain.{Claim, MoreAboutTheCare}
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._
import models.view.CachedClaim
import play.api.mvc.Controller
import models.yesNo.YesNoWithDate

object G7MoreAboutTheCare extends Controller with CachedClaim {
  val careMapping =
    "beforeClaimCaring" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "date" -> optional(dayMonthYear.verifying(validDate))
    )(YesNoWithDate.apply)(YesNoWithDate.unapply)
      .verifying("required", YesNoWithDate.validate _)

  val form = Form(
    mapping(
      call(routes.G7MoreAboutTheCare.present()),
      "spent35HoursCaring" -> nonEmptyText.verifying(validYesNo),
      careMapping,
      "hasSomeonePaidYou" -> nonEmptyText.verifying(validYesNo)
    )(MoreAboutTheCare.apply)(MoreAboutTheCare.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(MoreAboutTheCare)

  def present = claiming { implicit claim => implicit request =>
    val currentForm = claim.questionGroup(MoreAboutTheCare) match {
      case Some(m: MoreAboutTheCare) => form.fill(m)
      case _ => form
    }

    Ok(views.html.s4_care_you_provide.g7_moreAboutTheCare(currentForm, completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors.replaceError("beforeClaimCaring", FormError("beforeClaimCaring.date", "error.required"))
        BadRequest(views.html.s4_care_you_provide.g7_moreAboutTheCare(formWithErrorsUpdate, completedQuestionGroups))
      },
      moreAboutTheCare => claim.update(moreAboutTheCare) -> Redirect(routes.G8OneWhoPaysPersonalDetails.present())
    )
  }
}