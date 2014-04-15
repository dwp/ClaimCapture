package controllers.s7_employment

import language.reflectiveCalls
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._
import controllers.Mappings._
import models.domain.{Employment => Emp, Employed, SelfEmployment, ClaimDate}
import scala.language.postfixOps

object G1Employment extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
      "beenSelfEmployedSince1WeekBeforeClaim" -> nonEmptyText.verifying(validYesNo),
      "beenEmployedSince6MonthsBeforeClaim" -> nonEmptyText.verifying(validYesNo)
      )(Emp.apply)(Emp.unapply))

    def present = claimingWithCheck { implicit claim => implicit request => implicit lang =>
      claim.questionGroup(ClaimDate) match {
        case Some(n) => track(Employment) { implicit claim => Ok(views.html.s7_employment.g1_employment(form.fill(Emp))) }
        case _ => Redirect("/")
      }
    }

    def submit = claimingWithCheck { implicit claim => implicit request => implicit lang =>
      form.bindEncrypted.fold(
        formWithErrors => {
          val formWithErrorsUpdate = formWithErrors
            .replaceError("beenEmployedSince6MonthsBeforeClaim", "error.required",
              FormError("aboutYou_beenEmployedSince6MonthsBeforeClaim.label",
                "error.required",
                Seq(claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy => (dmy - 6 months).`dd/MM/yyyy`),claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))))
            .replaceError("beenSelfEmployedSince1WeekBeforeClaim", "error.required",
              FormError("aboutYou_beenSelfEmployedSince1WeekBeforeClaim.label",
                "error.required",
                Seq(claim.dateOfClaim.fold("{NO CLAIM DATE}")(dmy => (dmy - 6 months).`dd/MM/yyyy`),claim.dateOfClaim.fold("{NO CLAIM DATE}")(_.`dd/MM/yyyy`))))
          BadRequest(views.html.s7_employment.g1_employment(formWithErrorsUpdate))}
        ,employment => {
          val updatedClaim = claim.showHideSection(employment.beenEmployedSince6MonthsBeforeClaim == yes, Employed)
                                  .showHideSection(employment.beenSelfEmployedSince1WeekBeforeClaim == yes, SelfEmployment)
          updatedClaim.update(employment) -> Redirect(routes.G2BeenEmployed.present())
        }
      )
    }
}
