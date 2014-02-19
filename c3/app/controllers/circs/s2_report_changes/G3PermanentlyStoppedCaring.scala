package controllers.circs.s2_report_changes

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.{CachedChangeOfCircs, Navigable, CachedClaim}
import models.domain.CircumstancesStoppedCaring
import utils.helpers.CarersForm._
import controllers.Mappings._
import controllers.CarersForms._

object G3PermanentlyStoppedCaring extends Controller with CachedChangeOfCircs with Navigable {
  val form = Form(mapping(
    "stoppedCaringDate" -> dayMonthYear.verifying(validDate),
    "moreAboutChanges" -> optional(carersText(maxLength = 300))
  )(CircumstancesStoppedCaring.apply)(CircumstancesStoppedCaring.unapply))

  def present = claiming { implicit circs => implicit request => implicit lang =>
    track(CircumstancesStoppedCaring) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g3_permanently_stopped_caring(form.fill(CircumstancesStoppedCaring)))
    }
  }
  def submit = claiming { implicit circs => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circs.s2_report_changes.g3_permanently_stopped_caring(formWithErrors)),
      f => circs.update(f) -> Redirect(controllers.circs.s3_consent_and_declaration.routes.G1Declaration.present())
    )
  }
}