package controllers.s_claim_date

import play.api.Play._

import language.reflectiveCalls
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.mappings.Mappings._
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._
import models.domain._
import models.yesNo.YesNoWithDate
import play.api.i18n._

object GClaimDate extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val careMapping =
    "beforeClaimCaring" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "date" -> optional(dayMonthYear.verifying(validDate))
    )(YesNoWithDate.apply)(YesNoWithDate.unapply)
      .verifying("required", YesNoWithDate.validate _)

  val form = Form(mapping(
    "dateOfClaim" -> dayMonthYear.verifying(validDate),
    careMapping
  )(ClaimDate.apply)(ClaimDate.unapply))

  def present = claiming {implicit claim => implicit request => implicit lang => 
    track(ClaimDate) { implicit claim => Ok(views.html.s_claim_date.g_claimDate(form.fill(ClaimDate))) }
  }

  def submit = claiming { implicit claim => implicit request => implicit lang => 
    form.bindEncrypted.fold(
      formWithErrors => {
        val formWithErrorsUpdate = formWithErrors.replaceError("beforeClaimCaring", FormError("beforeClaimCaring.date", errorRequired))
        BadRequest(views.html.s_claim_date.g_claimDate(formWithErrorsUpdate))
      },
      claimDate => claim.update(claimDate) -> Redirect("/about-you/your-details"))
  } withPreview()
}
