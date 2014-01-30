package controllers.s2_about_you

import language.reflectiveCalls
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._
import controllers.Mappings._
import models.domain._
import scala.language.postfixOps

object G9Employment extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "beenSelfEmployedSince1WeekBeforeClaim" -> nonEmptyText.verifying(validYesNo),
    "beenEmployedSince6MonthsBeforeClaim" -> nonEmptyText.verifying(validYesNo)
  )(Employment.apply)(Employment.unapply))

  def present = claiming { implicit claim => implicit request =>
    claim.questionGroup(ClaimDate) match {
      case Some(n) => track(Employment) { implicit claim => Ok(views.html.s2_about_you.g9_employment(form.fill(Employment))) }
      case _ => Redirect(startPage)
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => {
    val formWithErrorsUpdate = formWithErrors
      .replaceError("beenEmployedSince6MonthsBeforeClaim", "error.required", FormError("beenEmployedSince6MonthsBeforeClaim", "error.required",Seq(claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy =>
      (dmy - 6 months).`dd/MM/yyyy`),claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))))
        BadRequest(views.html.s2_about_you.g9_employment(formWithErrorsUpdate))}
      ,
      employment => {
        val updatedClaim = claim.showHideSection(employment.beenEmployedSince6MonthsBeforeClaim == yes, Employed)
                                .showHideSection(employment.beenSelfEmployedSince1WeekBeforeClaim == yes, SelfEmployment)

        updatedClaim.update(employment) -> Redirect(routes.AboutYou.completed())
      })
  }
}