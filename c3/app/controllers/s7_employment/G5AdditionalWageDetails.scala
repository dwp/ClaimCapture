package controllers.s7_employment

import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{AdditionalWageDetails, LastWage}
import utils.helpers.CarersForm._
import controllers.Mappings._
import Employment._

object G5AdditionalWageDetails extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "jobID" -> nonEmptyText,
      "oftenGetPaid" -> optional(paymentFrequency),
      "whenGetPaid" -> optional(text),
      "holidaySickPay" -> optional(text verifying validYesNo),
      "anyOtherMoney" -> (nonEmptyText verifying validYesNo),
      "otherMoney" -> optional(text),
      "employeeOwesYouMoney" -> (nonEmptyText verifying validYesNo),
      call(routes.G5AdditionalWageDetails.present())
    )(AdditionalWageDetails.apply)(AdditionalWageDetails.unapply)
    .verifying("oftenGetPaid", AdditionalWageDetails.validateOftenGetPaid _)
    .verifying("otherMoney", AdditionalWageDetails.validateOtherMoney _)
  )


  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s7_employment.g5_additionalWageDetails(form, completedQuestionGroups(LastWage)))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g5_additionalWageDetails(formWithErrors, completedQuestionGroups(LastWage))),
      lastWage => claim.update(lastWage) -> Redirect(routes.G5AdditionalWageDetails.present()))
  }
}