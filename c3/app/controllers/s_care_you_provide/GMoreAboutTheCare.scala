package controllers.s_care_you_provide

import play.api.Play._

import language.reflectiveCalls
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.mappings.Mappings._
import utils.helpers.CarersForm._
import models.view.{Navigable, CachedClaim}
import models.domain.{TheirPersonalDetails, MoreAboutTheCare}
import models.yesNo.YesNoWithDate
import play.api.i18n._

object GMoreAboutTheCare extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "spent35HoursCaring" -> nonEmptyText.verifying(validYesNo)
  )(MoreAboutTheCare.apply)(MoreAboutTheCare.unapply))

  def present = claimingWithCheck {implicit claim => implicit request => implicit request2lang =>
    track(MoreAboutTheCare) { implicit claim => Ok(views.html.s_care_you_provide.g_moreAboutTheCare(form.fill(MoreAboutTheCare))) }
  }

  def submit = claimingWithCheck {implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val theirPersonalDetails = claim.questionGroup(TheirPersonalDetails).getOrElse(TheirPersonalDetails()).asInstanceOf[TheirPersonalDetails]
        val formWithErrorsUpdate = formWithErrors
          .replaceError("spent35HoursCaring", errorRequired, FormError("spent35HoursCaring", errorRequired, Seq(theirPersonalDetails.firstName+" "+theirPersonalDetails.surname)))
        BadRequest(views.html.s_care_you_provide.g_moreAboutTheCare(formWithErrorsUpdate))
      },
      moreAboutTheCare => claim.update(moreAboutTheCare) -> Redirect(controllers.s_breaks.routes.GBreaksInCare.present())
    )
  } withPreview()
}
