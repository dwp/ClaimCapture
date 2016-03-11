package controllers.circs.report_changes

import controllers.CarersForms._
import models.domain.CircumstancesOtherInfo
import models.view.{CachedChangeOfCircs, Navigable}
import play.api.Play._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import utils.helpers.CarersForm._
import play.api.i18n._

object GOtherChangeInfo extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val change = "changeInCircs"

  val form = Form(mapping(
    change -> carersNonEmptyText(maxLength = CircumstancesOtherInfo.textMaxLength)
  )(CircumstancesOtherInfo.apply)(CircumstancesOtherInfo.unapply))

  def present = claiming {implicit circs => implicit request => implicit request2lang =>
    track(CircumstancesOtherInfo) {
      implicit circs => Ok(views.html.circs.report_changes.otherChangeInfo(form.fill(CircumstancesOtherInfo)))
    }
  }

  def submit = claiming {implicit circs => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circs.report_changes.otherChangeInfo(formWithErrors)),
      f => circs.update(f) -> Redirect(circsPathAfterFunction)
    )
  }
}
