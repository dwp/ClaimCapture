package controllers.circs.s1_identification

import play.filters.csrf.CSRF

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
  val theirFullName = "theirFullName"
  val theirRelationshipToYou = "theirRelationshipToYou"

  val form = Form(mapping(
    "jsEnabled" -> boolean,
    fullName -> carersNonEmptyText(maxLength = 35),
    nationalInsuranceNumber -> nino.verifying(filledInNino).verifying(validNino),
    dateOfBirth -> dayMonthYear.verifying(validDate),
    theirFullName -> carersNonEmptyText(maxLength = 35),
    theirRelationshipToYou -> carersNonEmptyText(maxLength = 35)
  )(CircumstancesReportChange.apply)(CircumstancesReportChange.unapply))

  def present = newClaim {
    implicit circs => implicit request => implicit lang =>
      Logger.info(s"Starting new $cacheKey - ${circs.uuid}")
      track(CircumstancesReportChange) {
        implicit circs => Ok(views.html.circs.s1_identification.g1_reportAChangeInYourCircumstances(form.fill(CircumstancesReportChange)))
      }
  }

  def submit = claiming {
    implicit circs => implicit request => implicit lang =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.circs.s1_identification.g1_reportAChangeInYourCircumstances(formWithErrors)),
        f => circs.update(f) -> {
          if (!f.jsEnabled) {
            Logger.info(s"No JS - Start ${circs.key} ${circs.uuid} User-Agent : ${request.headers.get("User-Agent").orNull}")
          }
          Redirect(controllers.circs.s2_report_changes.routes.G1ReportChanges.present())
        }
      )
  }
}
