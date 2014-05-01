package controllers.circs.s3_consent_and_declaration

import play.api.mvc._
import models.view.{Navigable, CachedChangeOfCircs}
import play.api.data.Form
import play.api.data.Forms._
import utils.helpers.CarersForm._
import models.domain.{CircumstancesDeclaration, CircumstancesOtherInfo}
import controllers.CarersForms._
import controllers.submission.AsyncSubmittable
import monitoring.ChangeBotChecking
import play.api.data.FormError

abstract class G1Declaration extends Controller with CachedChangeOfCircs with Navigable {
  val form = Form(mapping(
    "furtherInfoContact" -> carersNonEmptyText(maxLength = 35),
    "obtainInfoAgreement" -> nonEmptyText,
    "obtainInfoWhy" -> optional(carersNonEmptyText(maxLength = 2000)),
    "confirm" -> nonEmptyText,
    "circsSomeOneElse" -> optional(carersText),
    "nameOrOrganisation" -> optional(carersNonEmptyText(maxLength = 60))
  )(CircumstancesDeclaration.apply)(CircumstancesDeclaration.unapply)
    .verifying("obtainInfoWhy", CircumstancesDeclaration.validateWhy _)
    .verifying("nameOrOrganisation", CircumstancesDeclaration.validateNameOrOrganisation _)
  )

  def present = claiming {
    implicit circs => implicit request => implicit lang =>
      track(CircumstancesOtherInfo) {
        implicit circs => Ok(views.html.circs.s3_consent_and_declaration.g1_declaration(form.fill(CircumstancesDeclaration)))
      }
  }

  def submit: Action[AnyContent]
}

class G1SyncDeclaration extends G1Declaration {
  override def submit: Action[AnyContent] = claiming {
    implicit circs => implicit request => implicit lang =>
      form.bindEncrypted.fold(
        formWithErrors => {
          val formWithErrorsUpdate = formWithErrors
            .replaceError("", "obtainInfoWhy", FormError("obtainInfoWhy", "error.required"))
            .replaceError("", "nameOrOrganisation", FormError("nameOrOrganisation", "error.required"))
          BadRequest(views.html.circs.s3_consent_and_declaration.g1_declaration(formWithErrorsUpdate))
        },
        f => circs.update(f) -> {
          Redirect(controllers.circs.s3_consent_and_declaration.routes.G2Submitting.present())
        }
      )
  }
}

class G1AsyncDeclaration extends G1Declaration with AsyncSubmittable with ChangeBotChecking {
  def submit: Action[AnyContent] = claiming {
    implicit circs => implicit request => implicit lang =>
      form.bindEncrypted.fold(
        formWithErrors => {
          val formWithErrorsUpdate = formWithErrors
            .replaceError("", "obtainInfoWhy", FormError("obtainInfoWhy", "error.required"))
            .replaceError("", "nameOrOrganisation", FormError("nameOrOrganisation", "error.required"))
          BadRequest(views.html.circs.s3_consent_and_declaration.g1_declaration(formWithErrorsUpdate))
        },
        f => circs.update(f) -> {
          submit(circs, request)
        }
      )
  }
}
