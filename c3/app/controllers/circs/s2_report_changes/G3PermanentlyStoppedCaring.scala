package controllers.circs.s2_report_changes

import controllers.CarersForms._
import controllers.mappings.Mappings._
import models.domain.CircumstancesStoppedCaring
import models.view.{CachedChangeOfCircs, Navigable}
import play.api.Play._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import utils.helpers.CarersForm._
import play.api.i18n._

import scala.language.reflectiveCalls

object G3PermanentlyStoppedCaring extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "stoppedCaringDate" -> dayMonthYear.verifying(validDate),
    "moreAboutChanges" -> optional(carersText(maxLength = 300))
  )(CircumstancesStoppedCaring.apply)(CircumstancesStoppedCaring.unapply))

  def present = claimingWithCheck {implicit circs => implicit request => implicit lang => 
    track(CircumstancesStoppedCaring) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g3_permanently_stopped_caring(form.fill(CircumstancesStoppedCaring)))
    }
  }
  def submit = claiming {implicit circs => implicit request => implicit lang => 
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circs.s2_report_changes.g3_permanently_stopped_caring(formWithErrors)),
      f => circs.update(f) -> Redirect(controllers.circs.s3_consent_and_declaration.routes.G1Declaration.present())
    )
  }
}
