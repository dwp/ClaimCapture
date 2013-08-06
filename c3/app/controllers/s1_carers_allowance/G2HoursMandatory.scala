package controllers.s1_carers_allowance

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain.HoursMandatory
import controllers.Mappings._
import models.domain.Claim
import language.reflectiveCalls
import controllers.Mappings._
import CarersAllowance._

object G2HoursMandatory extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "answer" -> nonEmptyText.verifying(validYesNo))(HoursMandatory.apply)(HoursMandatory.unapply))

  def present = claiming { implicit claim =>
    implicit request =>
      Ok(views.html.s1_carers_allowance.g2_hoursMandatory(form.fill(HoursMandatory), completedQuestionGroups(HoursMandatory)))
  }

  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s1_carers_allowance.g2_hoursMandatory(formWithErrors, completedQuestionGroups(HoursMandatory))),
        f => claim.update(f) -> Redirect(routes.G3Over16Mandatory.present()))
  }
}