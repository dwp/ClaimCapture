package controllers.circs.report_changes

import play.api.Play._
import play.api.mvc.Controller
import models.view.{Navigable, CachedChangeOfCircs}
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import models.domain.CircumstancesSelfEmployment
import utils.helpers.CarersForm._
import controllers.CarersForms._
import controllers.mappings.Mappings._
import models.yesNo.{YesNoWithDate, YesNoWithText}
import play.api.i18n._

object GSelfEmployment extends Controller with CachedChangeOfCircs with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val stillCaringMapping =
    "stillCaring" -> mapping(
      "answer" -> nonEmptyText.verifying(validYesNo),
      "date" -> optional(dayMonthYear.verifying(validDate))
    )(YesNoWithDate.apply)(YesNoWithDate.unapply)
      .verifying("dateRequired", YesNoWithDate.validateNo _)

  val form = Form(mapping(
    stillCaringMapping,
    "whenThisSelfEmploymentStarted" -> dayMonthYear.verifying(validDate),
    "typeOfBusiness" -> carersNonEmptyText(maxLength = 35),
    "totalOverWeeklyIncomeThreshold" -> nonEmptyText.verifying(validYesNoDontKnow),
    "moreAboutChanges" -> optional(carersText(maxLength = CircumstancesSelfEmployment.textMaxLength))
  )(CircumstancesSelfEmployment.apply)(CircumstancesSelfEmployment.unapply))

  def present = claiming {implicit circs => implicit request => implicit request2lang =>
    track(CircumstancesSelfEmployment) {
      implicit circs => Ok(views.html.circs.report_changes.selfEmployment(form.fill(CircumstancesSelfEmployment)))
    }
  }

  def submit = claiming {implicit circs => implicit request => implicit request2lang =>
    form.bindEncrypted.fold(
      formWithErrors => {
        val updatedFormWithErrors = formWithErrors.replaceError("stillCaring","dateRequired", FormError("stillCaring.date", errorRequired))
        BadRequest(views.html.circs.report_changes.selfEmployment(updatedFormWithErrors))
      },
      f => circs.update(f) -> Redirect(circsPathAfterFunction)
    )
  }
}
