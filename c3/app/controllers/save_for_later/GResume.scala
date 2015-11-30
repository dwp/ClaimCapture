package controllers.save_for_later

import models.domain.{ResumeClaim}
import models.view.{CachedClaim, Navigable}
import play.api.Play._
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.i18n._
import play.api.mvc.{Controller}
import scala.language.reflectiveCalls
import controllers.CarersForms._
import controllers.mappings.Mappings._
import controllers.mappings.NINOMappings._

object GResume extends Controller with CachedClaim with Navigable with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]

  val form=Form(mapping(
    "firstName" -> carersNonEmptyText(maxLength = 17),
    "surname" -> carersNonEmptyText(maxLength = Name.maxLength),
    "nationalInsuranceNumber" -> nino.verifying(filledInNino,validNino),
    "dateOfBirth" -> dayMonthYear.verifying(validDate)
  )(ResumeClaim.apply)(ResumeClaim.unapply))

  def present = claiming { implicit claim => implicit request => implicit lang =>
    track(ResumeClaim) { implicit claim => Ok(views.html.save_for_later.resumeClaim(form)) }
  }

  def submit = claiming { implicit claim => implicit request => implicit lang =>
    BadRequest(views.html.save_for_later.resumeClaim(ResumeClaim))
  }
}

