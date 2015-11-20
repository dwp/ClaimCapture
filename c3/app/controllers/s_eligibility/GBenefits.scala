package controllers.s_eligibility

import play.api.Play._

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain._
import models.view.Navigable
import play.api.Logger
import play.api.i18n._

object GBenefits extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "benefitsAnswer" -> nonEmptyText
  )(Benefits.apply)(Benefits.unapply))

  def present = newClaim {implicit claim => implicit request => implicit lang => 
    Logger.debug(s"Starting new $cacheKey - ${claim.uuid} with language $lang")
    track(Benefits) { implicit claim => Ok(views.html.s_eligibility.g_benefits(form.fill(Benefits))) }
  }

  def submit = claiming ({implicit claim => implicit request => implicit lang => 
    form.bindEncrypted.fold(
      formWithErrors => {
        BadRequest(views.html.s_eligibility.g_benefits(formWithErrors))
      },
      f => claim.update(f) -> Redirect(routes.GEligibility.present()))
  }, checkCookie=true)
}
