package controllers.s2_about_you

import language.reflectiveCalls
import models.domain._
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import controllers.Mappings._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.yesNo.YesNoWithDate
import models.LivingInUK

object G3TimeOutsideUK extends Controller with AboutYouRouting with CachedClaim {
  val goBackMapping =
    "goBack" -> optional(
      mapping(
        "answer" -> nonEmptyText.verifying(validYesNo),
        "date" -> optional(dayMonthYear.verifying(validDateOnly))
      )(YesNoWithDate.apply)(YesNoWithDate.unapply))

  val livingInUKMapping =
    "livingInUK" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "arrivalDate" -> optional(dayMonthYear.verifying(validDate)),
      "originCountry" -> optional(text(maxLength = sixty)),
      goBackMapping
    )(LivingInUK.apply)(LivingInUK.unapply)
      .verifying("arrivalDate", LivingInUK.validateDate _)
      .verifying("goBack", LivingInUK.validateGoBack _)

  val form = Form(
    mapping(
      livingInUKMapping,
      "visaReference" -> optional(text(maxLength = sixty))
    )(TimeOutsideUK.apply)(TimeOutsideUK.unapply))

  def present = claiming { implicit claim => implicit request =>
    claim.questionGroup(YourDetails) match {
      case Some(y: YourDetails) if y.alwaysLivedUK == "yes" => claim.delete(TimeOutsideUK) -> Redirect(routes.G4ClaimDate.present())
      case _ => Ok(views.html.s2_about_you.g3_timeOutsideUK(form.fill(TimeOutsideUK), completedQuestionGroups(TimeOutsideUK)))
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors
          .replaceError("livingInUK", "arrivalDate", FormError("livingInUK.arrivalDate", "error.required"))
          .replaceError("livingInUK", "goBack", FormError("livingInUK.goBack.answer", "error.required"))
        BadRequest(views.html.s2_about_you.g3_timeOutsideUK(formWithErrorsUpdate, completedQuestionGroups(TimeOutsideUK)))
      },
      timeOutsideUK => claim.update(timeOutsideUK) -> Redirect(routes.G4ClaimDate.present())
    )
  }
}