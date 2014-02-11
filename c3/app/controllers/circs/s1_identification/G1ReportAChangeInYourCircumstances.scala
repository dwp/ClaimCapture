package controllers.circs.s1_identification

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.{Navigable, CachedChangeOfCircs}
import controllers.Mappings._
import models.domain.CircumstancesReportChange
import utils.helpers.CarersForm._
import controllers.CarersForms._
import play.api.Logger

object G1ReportAChangeInYourCircumstances extends Controller with CachedChangeOfCircs with Navigable {

  val fullName = "fullName"
  val nationalInsuranceNumber = "nationalInsuranceNumber"
  val dateOfBirth = "dateOfBirth"

  val form = Form(mapping(
    fullName -> carersNonEmptyText(maxLength = 35),
    nationalInsuranceNumber -> nino.verifying(filledInNino, validNino),
    dateOfBirth -> dayMonthYear.verifying(validDate)
  )(CircumstancesReportChange.apply)(CircumstancesReportChange.unapply))

  def present = newClaim { implicit circs => implicit request =>
    Logger.info(s"Starting new $cacheKey")
    track(CircumstancesReportChange) { implicit circs => Ok(views.html.circs.s1_identification.g1_reportAChangeInYourCircumstances(form.fill(CircumstancesReportChange))) }
  }

  def submit = claiming { implicit circs => implicit request =>

    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circs.s1_identification.g1_reportAChangeInYourCircumstances(formWithErrors)),
      f => circs.update(f) -> Redirect(controllers.circs.s2_report_changes.routes.G1ReportChanges.present())
    )
  }
}
