package controllers.circs.s2_report_changes

import play.api.mvc.Controller
import models.view.{Navigable, CachedChangeOfCircs}
import models.domain.CircumstancesStartedEmploymentAndOngoing
import play.api.data.{Form, FormError}
import controllers.Mappings._
import play.api.data.Forms._
import utils.helpers.CarersForm._

object G10StartedEmploymentAndOngoing extends Controller with CachedChangeOfCircs with Navigable {
  val form = Form(mapping(
    "howMuchPaid" -> nonEmptyText(maxLength = 20)
  )(CircumstancesStartedEmploymentAndOngoing.apply)(CircumstancesStartedEmploymentAndOngoing.unapply))

  def present = claiming { implicit circs => implicit request => implicit lang =>
    track(CircumstancesStartedEmploymentAndOngoing) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g10_startedEmploymentAndOngoing(form.fill(CircumstancesStartedEmploymentAndOngoing)))
    }
  }

  def submit = claiming { implicit circs => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        BadRequest(views.html.circs.s2_report_changes.g10_startedEmploymentAndOngoing(formWithErrors))
      },
      f => circs.update(f) -> Redirect(controllers.circs.s3_consent_and_declaration.routes.G1Declaration.present())
    )
  }
}
