package controllers.circs.s2_report_changes

import play.api.mvc.Controller
import models.view.{Navigable, CachedChangeOfCircs}
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import controllers.CarersForms._
import models.domain.{QuestionGroup, ReportChanges}
import utils.helpers.CarersForm._
import app.ReportChange._
import models.domain._

object G1ReportChanges extends Controller with CachedChangeOfCircs with Navigable {

  val form = Form(mapping(
    "reportChanges" -> nonEmptyText(maxLength = 20)
  )(ReportChanges.apply)(ReportChanges.unapply))

  def present = claiming { implicit circs => implicit request =>
    track(ReportChanges) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g1_reportChanges(form.fill(ReportChanges)))
    }
  }

  def submit = claiming { implicit circs => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circs.s2_report_changes.g1_reportChanges(formWithErrors)),
      form => {
        val c = updateCircs(form, circs)
        c
      }
    )
  }

  def updateCircs(f:ReportChanges, circs:Claim) = {
    // for qs groups under this section, if it is not reportedChange - delete
    if (f.reportChanges == StoppedCaring.name) {
      var updatedCircs = circs.delete(CircumstancesSelfEmployment)
      var updatedCircs2 = updatedCircs.delete(CircumstancesOtherInfo)
      updatedCircs2.update(f) -> Redirect(controllers.circs.s2_report_changes.routes.G3PermanentlyStoppedCaring.present())
    }
    else if (f.reportChanges == app.ReportChange.SelfEmployment.name) {
      var updatedCircs = circs.delete(CircumstancesStoppedCaring)
      var updatedCircs2 = updatedCircs.delete(CircumstancesOtherInfo)
      updatedCircs2.update(f) -> Redirect(controllers.circs.s2_report_changes.routes.G2SelfEmployment.present())
    }
    else {
      var updatedCircs = circs.delete(CircumstancesStoppedCaring)
      var updatedCircs2 = updatedCircs.delete(CircumstancesSelfEmployment)
      updatedCircs2.update(f) -> Redirect(controllers.circs.s2_report_changes.routes.G4OtherChangeInfo.present())
    }
  }
}
