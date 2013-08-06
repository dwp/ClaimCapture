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
import controllers.Mappings._
import CarersAllowance._

object G3Over16Mandatory extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "answer" -> nonEmptyText.verifying(validYesNo))(Over16Mandatory.apply)(Over16Mandatory.unapply))

  def present = claiming { implicit claim =>
    implicit request =>
      Ok(views.html.s1_carers_allowance.g3_over16Mandatory(form.fill(Over16Mandatory), completedQuestionGroups(Over16Mandatory)))
  }

  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s1_carers_allowance.g3_over16Mandatory(formWithErrors, completedQuestionGroups(Over16Mandatory))),
        f => claim.update(f) -> Redirect(routes.G4LivesInGBMandatory.present()))
  }
}