package controllers.s1_carers_allowance

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain.Over16Mandatory
import controllers.Mappings._
import models.domain.Claim
import language.reflectiveCalls

object G3Over16Mandatory extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(Over16Mandatory)

  val form = Form(
    mapping(
      call(routes.G3Over16Mandatory.present()),
      "answer" -> nonEmptyText.verifying(validYesNo))(Over16Mandatory.apply)(Over16Mandatory.unapply))

  def present = claiming { implicit claim =>
    implicit request =>
      Ok(views.html.s1_carers_allowance.g3_over16Mandatory(form.fill(Over16Mandatory), completedQuestionGroups))
  }

  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s1_carers_allowance.g3_over16Mandatory(formWithErrors, completedQuestionGroups)),
        f => claim.update(f) -> Redirect(routes.G4LivesInGBMandatory.present()))
  }
}