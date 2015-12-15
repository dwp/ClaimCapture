package controllers.circs.s2_report_changes

import controllers.CarersForms._
import models.domain.CircumstancesOtherInfo
import models.view.{CachedChangeOfCircs, Navigable}
import play.api.Play._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import utils.helpers.CarersForm._
import play.api.i18n._

object G4OtherChangeInfo extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val change = "changeInCircs"

  val form = Form(mapping(
    change -> carersNonEmptyText(maxLength = 2000)
  )(CircumstancesOtherInfo.apply)(CircumstancesOtherInfo.unapply))

  def present = claimingWithCheck {implicit circs => implicit request => implicit request2lang =>
    track(CircumstancesOtherInfo) {
      implicit circs => Ok(views.html.circs.s2_report_changes.g4_otherChangeInfo(form.fill(CircumstancesOtherInfo)))
    }
  }

  def submit = claiming {implicit circs => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circs.s2_report_changes.g4_otherChangeInfo(formWithErrors)),
      f => circs.update(f) -> Redirect(controllers.circs.s3_consent_and_declaration.routes.G1Declaration.present())
    )
  }
}
