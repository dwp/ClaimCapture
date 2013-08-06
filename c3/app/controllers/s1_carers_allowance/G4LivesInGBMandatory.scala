package controllers.s1_carers_allowance

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain.LivesInGBMandatory
import controllers.Mappings._
import models.domain.Claim
import language.reflectiveCalls

object G4LivesInGBMandatory extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(LivesInGBMandatory)

  val form = Form(
    mapping(
      call(routes.G4LivesInGBMandatory.present()),
      "answer" -> nonEmptyText.verifying(validYesNo))(LivesInGBMandatory.apply)(LivesInGBMandatory.unapply))

  def present = claiming { implicit claim =>
    implicit request =>
      Ok(views.html.s1_carers_allowance.g4_livesInGBMandatory(form.fill(LivesInGBMandatory), completedQuestionGroups))
  }

  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s1_carers_allowance.g4_livesInGBMandatory(formWithErrors, completedQuestionGroups)),
        f => claim.update(f) -> Redirect(routes.CarersAllowance.approve()))
  }
}