package controllers.s2_about_you

import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import play.api.mvc.Controller
import models.view.CachedClaim
import controllers.Routing
import utils.helpers.CarersForm._

object G3TimeOutsideUK extends Controller with Routing with CachedClaim {
  override val route = TimeOutsideUK.id -> routes.G3TimeOutsideUK.present

  val form = Form(
    mapping(
      "currentlyLivingInUK" -> nonEmptyText(),
      "arrivedInUK" -> optional(dayMonthYear.verifying(validDate)),
      "originCountry" -> optional(text(maxLength = sixty)),
      "planToGoBack" -> optional(text),
      "whenPlanToGoBack" -> optional(dayMonthYear.verifying(validDate)),
      "visaReference" -> optional(text(maxLength = sixty))
    )(TimeOutsideUK.apply)(TimeOutsideUK.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(TimeOutsideUK)

  def present = claiming { implicit claim => implicit request =>
    claim.questionGroup(YourDetails.id) match {
      case Some(y: YourDetails) if y.alwaysLivedUK == "yes" => Redirect(routes.G4ClaimDate.present())
      case _ =>
        val timeOutsideUKForm: Form[TimeOutsideUK] = claim.questionGroup(TimeOutsideUK.id) match {
          case Some(t: TimeOutsideUK) => form.fill(t)
          case _ => form
        }

        Ok(views.html.s2_about_you.g3_timeOutsideUK(timeOutsideUKForm, completedQuestionGroups))
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    def livingInUK(timeOutsideUKForm: Form[TimeOutsideUK])(implicit timeOutsideUK: TimeOutsideUK): Form[TimeOutsideUK] = {
      if (timeOutsideUK.currentlyLivingInUK == "yes" && timeOutsideUK.arrivedInUK == None) timeOutsideUKForm.fill(timeOutsideUK).withError("arrivedInUK", "error.required")
      else timeOutsideUKForm
    }

    def planToGoBack(timeOutsideUKForm: Form[TimeOutsideUK])(implicit timeOutsideUK: TimeOutsideUK): Form[TimeOutsideUK] = {
      if (timeOutsideUK.planToGoBack.getOrElse("no") == "yes" && timeOutsideUK.whenPlanToGoBack == None) timeOutsideUKForm.fill(timeOutsideUK).withError("whenPlanToGoBack", "error.required")
      else timeOutsideUKForm
    }

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g3_timeOutsideUK(formWithErrors, completedQuestionGroups)),
      implicit timeOutsideUK => {
        val formValidations = livingInUK _ andThen planToGoBack _
        val timeOutsideUKFormValidated = formValidations(form)

        if (timeOutsideUKFormValidated.hasErrors) BadRequest(views.html.s2_about_you.g3_timeOutsideUK(timeOutsideUKFormValidated, completedQuestionGroups))
        else claim.update(timeOutsideUK) -> Redirect(routes.G4ClaimDate.present())
      })
  }
}