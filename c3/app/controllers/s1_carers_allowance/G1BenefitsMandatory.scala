package controllers.s1_carers_allowance

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import models.domain.BenefitsMandatory
import controllers.Mappings._
import models.domain.Claim
import language.reflectiveCalls

object G1BenefitsMandatory extends Controller with CachedClaim {
  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(BenefitsMandatory)
  
  val form = Form(
    mapping(
      call(routes.G1BenefitsMandatory.present()),
      "answer" -> nonEmptyText.verifying(validYesNo))(BenefitsMandatory.apply)(BenefitsMandatory.unapply))

  def present = newClaim { implicit claim =>
    implicit request =>
      Ok(views.html.s1_carers_allowance.g1_benefitsMandatory(form.fill(BenefitsMandatory), completedQuestionGroups))
  }

  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s1_carers_allowance.g1_benefitsMandatory(formWithErrors, completedQuestionGroups)),
        f => claim.update(f) -> Redirect(routes.G2HoursMandatory.present()))
  }
}