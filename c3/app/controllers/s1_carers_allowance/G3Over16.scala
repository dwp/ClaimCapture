package controllers.s1_carers_allowance

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain.Over16
import controllers.Mappings._
import models.domain.Claim
import language.reflectiveCalls
import controllers.Mappings._
import CarersAllowance._

object G3Over16 extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "answer" -> nonEmptyText.verifying(validYesNo))(Over16.apply)(Over16.unapply))

  def present = claiming { implicit claim =>
    implicit request =>
      Ok(views.html.s1_carers_allowance.g3_over16(form.fill(Over16), completedQuestionGroups(Over16)))
  }

  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s1_carers_allowance.g3_over16(formWithErrors, completedQuestionGroups(Over16))),
        f => claim.update(f) -> Redirect(routes.G4LivesInGBMandatory.present()))
  }
}