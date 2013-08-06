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
      "answer" -> nonEmptyText.verifying(validYesNo)
  )(Over16Mandatory.apply)(Over16Mandatory.unapply))

  def present = newClaim { implicit claim => implicit request =>
      Ok(<p>Hello</p>)//Ok(views.html.s1_carers_allowance.g2_hoursMandatory(form.fill(HoursMandatory), completedQuestionGroups))
  }

  def submit = claiming { implicit claim =>
    implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(<p>hello world</p>),//BadRequest(views.html.s1_carers_allowance.g2_hoursMandatory(formWithErrors, completedQuestionGroups)),
        f => claim.update(f) -> Redirect(routes.G3Over16.present()))
  }
}