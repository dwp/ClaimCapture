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

object G2HoursMandatory extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(HoursMandatory)
  
  val form = Form(
    mapping(
      call(routes.G2HoursMandatory.present()),
      "answer" -> nonEmptyText.verifying(validYesNo)
  )(HoursMandatory.apply)(HoursMandatory.unapply))

  def present = newClaim { implicit claim => implicit request =>
      println("****** g2 completedQuestionGroups: " + completedQuestionGroups)
      Ok(views.html.s1_carers_allowance.g2_hoursMandatory(form.fill(HoursMandatory), completedQuestionGroups))
  }

  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s1_carers_allowance.g2_hoursMandatory(formWithErrors, completedQuestionGroups)),
        f => claim.update(f) -> Redirect(routes.G3Over16Mandatory.present()))
  }
}