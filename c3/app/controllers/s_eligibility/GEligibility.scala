package controllers.s_eligibility

import controllers.CarersForms._
import play.api.Logger
import play.api.Play._

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain.Eligibility
import controllers.mappings.Mappings._
import models.view.Navigable
import play.api.i18n._
import app.ConfigProperties._

object GEligibility extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "hours.answer" -> nonEmptyText.verifying(validYesNo),
    "over16.answer" -> nonEmptyText.verifying(validYesNo),
    "origin" -> carersNonEmptyText(maxLength = 20)
  )(Eligibility.apply)(Eligibility.unapply))

  def present = claiming({ implicit claim => implicit request => implicit request2lang =>
    track(Eligibility) {
      implicit claim => Ok(views.html.s_eligibility.g_eligibility(form.fill(Eligibility)))
    }
  }, checkCookie = true)

  def submit = claiming { implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        BadRequest(views.html.s_eligibility.g_eligibility(formWithErrors))
      },
      f => {
        (getStringProperty("origin.tag"), f.origin) match {
          case ("GB-NIR", "GB") => {
            Logger.info("Claim Origin Select - NISSA site user posted selected originating country of:" + f.origin + ". Displaying NI origin error page")
            Ok(views.html.common.origin.NIoriginError(false, request2lang))
          }
          case ("GB", "NI") => {
            Logger.info("Claim Origin Select - GB site user posted selected originating country of:" + f.origin + ". Displaying GB origin error page")
            Ok(views.html.common.origin.GBoriginError(false, request2lang))
          }
          case (_, _) => {
            claim.update(f) -> Redirect(routes.CarersAllowance.approve())
          }
        }
      })
  }
}
