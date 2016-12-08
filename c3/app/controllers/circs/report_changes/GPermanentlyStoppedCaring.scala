package controllers.circs.report_changes

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

object GPermanentlyStoppedCaring extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "stoppedCaringDate" -> dayMonthYear.verifying(validDate),
    "moreAboutChanges" -> optional(carersText(maxLength = CircumstancesStoppedCaring.textMaxLength))
  )(CircumstancesStoppedCaring.apply)(CircumstancesStoppedCaring.unapply))

  def present = claiming {implicit circs => implicit request => implicit request2lang =>
    track(CircumstancesStoppedCaring) {
      implicit circs => Ok(views.html.circs.report_changes.permanentlyStoppedCaring(form.fill(CircumstancesStoppedCaring)))
    }
  }
  def submit = claiming {implicit circs => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circs.report_changes.permanentlyStoppedCaring(formWithErrors)),
      f => circs.update(f) -> Redirect(controllers.circs.consent_and_declaration.routes.GCircsDeclaration.present())
    )
  }
}
