package controllers.s7_employment

import scala.language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import models.view.{Navigable, CachedClaim}
import models.domain.AdditionalWageDetails
import utils.helpers.CarersForm._
import controllers.Mappings._
import Employment._
import controllers.CarersForms._

object G5AdditionalWageDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "jobID" -> nonEmptyText,
    "oftenGetPaid" -> optional(paymentFrequency verifying validPaymentFrequencyOnly),
    "whenGetPaid" -> optional(carersText),
    "employerOwesYouMoney" -> (nonEmptyText verifying validYesNo)
  )(AdditionalWageDetails.apply)(AdditionalWageDetails.unapply))

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    track(AdditionalWageDetails) { implicit claim => Ok(views.html.s7_employment.g5_additionalWageDetails(form.fillWithJobID(AdditionalWageDetails, jobID))) }
  }

  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val newForm = formWithErrors.replaceError("oftenGetPaid.frequency.other","error.maxLength",FormError("oftenGetPaid","error.maxLength"))
        BadRequest(views.html.s7_employment.g5_additionalWageDetails(newForm))
      },
      wageDetails => claim.update(jobs.update(wageDetails)) -> Redirect(routes.G7PensionSchemes.present(jobID)))
  }
}