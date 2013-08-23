package controllers.s7_employment

import scala.language.reflectiveCalls
import models.view.{Navigable, CachedClaim}
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.AdditionalWageDetails
import utils.helpers.CarersForm._
import controllers.Mappings._
import Employment._

object G5AdditionalWageDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "jobID" -> nonEmptyText,
    "oftenGetPaid" -> optional(paymentFrequency),
    "whenGetPaid" -> optional(text),
    "employerOwesYouMoney" -> (nonEmptyText verifying validYesNo)
  )(AdditionalWageDetails.apply)(AdditionalWageDetails.unapply))

  def present(jobID: String) = claiming { implicit claim => implicit request =>
    track(AdditionalWageDetails) { implicit claim => Ok(views.html.s7_employment.g5_additionalWageDetails(form.fillWithJobID(AdditionalWageDetails, jobID))) }
  }

  def submit = claimingInJob { jobID => implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g5_additionalWageDetails(formWithErrors)),
      wageDetails => claim.update(jobs.update(wageDetails)) -> Redirect(routes.G7PensionSchemes.present(jobID)))
  }
}