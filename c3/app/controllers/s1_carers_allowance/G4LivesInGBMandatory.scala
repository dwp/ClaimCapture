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
import controllers.Mappings._
import CarersAllowance._

object G4LivesInGBMandatory extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "answer" -> nonEmptyText.verifying(validYesNo))(LivesInGBMandatory.apply)(LivesInGBMandatory.unapply))

  def present = claiming { implicit claim =>
    implicit request =>
      Ok(views.html.s1_carers_allowance.g4_livesInGBMandatory(form.fill(LivesInGBMandatory), completedQuestionGroups(LivesInGBMandatory)))
  }

  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s1_carers_allowance.g4_livesInGBMandatory(formWithErrors, completedQuestionGroups(LivesInGBMandatory))),
        f => claim.update(f) -> Redirect(routes.CarersAllowance.approve()))
  }
}