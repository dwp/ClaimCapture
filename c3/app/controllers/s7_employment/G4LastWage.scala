package controllers.s7_employment

import models.view.CachedClaim
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.domain.{LastWage, EmployerContactDetails}
import utils.helpers.CarersForm._
import controllers.Mappings._

object G4LastWage extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "lastPaidDate" -> optional(dayMonthYear.verifying(validDate)),
      "periodFrom" -> optional(dayMonthYear.verifying(validDate)),
      "periodTo" -> optional(dayMonthYear.verifying(validDate)),
      "grossPay" -> optional(text),
      "payInclusions" -> optional(text),
      "sameAmountEachTime" -> optional(text),
      call(routes.G4LastWage.present())
    )(LastWage.apply)(LastWage.unapply))

  def present = claiming { implicit claim => implicit request =>
    Ok(views.html.s7_employment.g4_lastWage(form))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s7_employment.g4_lastWage(formWithErrors)),
      lastWage => claim.update(lastWage) -> Redirect(routes.G4LastWage.present()))
  }
}