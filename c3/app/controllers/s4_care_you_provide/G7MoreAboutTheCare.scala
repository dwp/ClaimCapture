package controllers.s4_care_you_provide

import language.reflectiveCalls
import models.domain.MoreAboutTheCare
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import controllers.Mappings._
import utils.helpers.CarersForm._
import models.view.CachedClaim
import play.api.mvc.Controller
import models.yesNo.YesNoWithDate
import CareYouProvide._

object G7MoreAboutTheCare extends Controller with CachedClaim {
  val careMapping =
    "beforeClaimCaring" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "date" -> optional(dayMonthYear.verifying(validDate))
    )(YesNoWithDate.apply)(YesNoWithDate.unapply)
      .verifying("required", YesNoWithDate.validate _)

  val form = Form(
    mapping(
      "spent35HoursCaring" -> nonEmptyText.verifying(validYesNo),
      careMapping,
      "hasSomeonePaidYou" -> nonEmptyText.verifying(validYesNo)
    )(MoreAboutTheCare.apply)(MoreAboutTheCare.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s4_care_you_provide.g7_moreAboutTheCare(form.fill(MoreAboutTheCare), completedQuestionGroups(MoreAboutTheCare)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors.replaceError("beforeClaimCaring", FormError("beforeClaimCaring.date", "error.required"))
        BadRequest(views.html.s4_care_you_provide.g7_moreAboutTheCare(formWithErrorsUpdate, completedQuestionGroups(MoreAboutTheCare)))
      },
      moreAboutTheCare => claim.update(moreAboutTheCare) -> Redirect(routes.G8OneWhoPaysPersonalDetails.present())
    )
  }
}