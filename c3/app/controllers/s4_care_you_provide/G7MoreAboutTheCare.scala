package controllers.s4_care_you_provide

import language.reflectiveCalls
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import utils.helpers.CarersForm._
import models.view.{Navigable, CachedClaim}
import models.domain.MoreAboutTheCare
import models.yesNo.YesNoWithDate

object G7MoreAboutTheCare extends Controller with CachedClaim with Navigable {
  val careMapping =
    "beforeClaimCaring" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "date" -> optional(dayMonthYear.verifying(validDate))
    )(YesNoWithDate.apply)(YesNoWithDate.unapply)
      .verifying("required", YesNoWithDate.validate _)

  val form = Form(mapping(
    "spent35HoursCaring" -> nonEmptyText.verifying(validYesNo),
    careMapping
  )(MoreAboutTheCare.apply)(MoreAboutTheCare.unapply))

  def present = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    track(MoreAboutTheCare) { implicit claim => Ok(views.html.s4_care_you_provide.g7_moreAboutTheCare(form.fill(MoreAboutTheCare))) }
  }

  def submit = claimingWithCheck {implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors.replaceError("beforeClaimCaring", FormError("beforeClaimCaring.date", "error.required"))
        BadRequest(views.html.s4_care_you_provide.g7_moreAboutTheCare(formWithErrorsUpdate))
      },
      moreAboutTheCare => claim.update(moreAboutTheCare) -> Redirect(routes.G10BreaksInCare.present())
    )
  }
}