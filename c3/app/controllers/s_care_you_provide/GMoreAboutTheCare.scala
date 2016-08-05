package controllers.s_care_you_provide

import play.api.Play._
import utils.helpers.OriginTagHelper._

import language.reflectiveCalls
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.mappings.Mappings._
import utils.helpers.CarersForm._
import models.view.{Navigable, CachedClaim}
import models.domain.{TheirPersonalDetails, MoreAboutTheCare}
import play.api.i18n._
import controllers.CarersForms._

object GMoreAboutTheCare extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val form = Form(mapping(
    "spent35HoursCaring" -> nonEmptyText.verifying(validYesNo),
    "otherCarer" -> optional(text.verifying(validYesNo)),
    "otherCarerUc" -> optional(text.verifying(validYesNo)),
    "otherCarerUcDetails" -> optional(carersText(maxLength = MoreAboutTheCare.textMaxLength))
  )(MoreAboutTheCare.apply)(MoreAboutTheCare.unapply)
    .verifying("otherCarer.required", validateCarer _)
    .verifying("otherCarerUc.required", validateCarerUc _)
  )

  private def validateCarer(moreAboutTheCare: MoreAboutTheCare) = {
    isOriginGB match {
      case true => moreAboutTheCare.otherCarer.isDefined
      case _ => true
    }
  }

  private def validateCarerUc(moreAboutTheCare: MoreAboutTheCare) = {
    moreAboutTheCare.otherCarer match {
      case Some(`yes`) => moreAboutTheCare.otherCarerUc.isDefined
      case _ => true
    }
  }

  def present = claimingWithCheck {implicit claim => implicit request => implicit request2lang =>
    track(MoreAboutTheCare) { implicit claim => Ok(views.html.s_care_you_provide.g_moreAboutTheCare(form.fill(MoreAboutTheCare))) }
  }

  def submit = claimingWithCheck {implicit claim => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val theirPersonalDetails = claim.questionGroup(TheirPersonalDetails).getOrElse(TheirPersonalDetails()).asInstanceOf[TheirPersonalDetails]
        val formWithErrorsUpdate = formWithErrors
          .replaceError("spent35HoursCaring", errorRequired, FormError("spent35HoursCaring", errorRequired, Seq(theirPersonalDetails.firstName+" "+theirPersonalDetails.surname)))
          .replaceError("", "otherCarer.required", FormError("otherCarer", errorRequired, Seq(theirPersonalDetails.firstName+" "+theirPersonalDetails.surname)))
          .replaceError("otherCarerUc", errorRequired, FormError("otherCarerUc", errorRequired, Seq(theirPersonalDetails.firstName+" "+theirPersonalDetails.surname)))
          .replaceError("", "otherCarerUc.required", FormError("otherCarerUc", errorRequired, Seq(theirPersonalDetails.firstName+" "+theirPersonalDetails.surname)))
        BadRequest(views.html.s_care_you_provide.g_moreAboutTheCare(formWithErrorsUpdate))
      },
      moreAboutTheCare => claim.update(moreAboutTheCare) -> Redirect(controllers.s_breaks.routes.GBreaksInCare.present())
    )
  } withPreview()
}
