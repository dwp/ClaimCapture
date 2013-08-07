package controllers.s2_about_you

import language.reflectiveCalls
import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import controllers.Mappings._
import controllers.s2_about_you.AboutYou._

object G7PropertyAndRent extends Controller with CachedClaim {
  val form = Form(
    mapping(
      "ownProperty" -> nonEmptyText.verifying(validYesNo),
      "hasSublet" -> nonEmptyText.verifying(validYesNo)
    )(PropertyAndRent.apply)(PropertyAndRent.unapply))

  def present = claiming { implicit claim => implicit request =>
    claim.questionGroup(models.domain.ClaimDate) match {
      case Some(n) => Ok(views.html.s2_about_you.g7_propertyAndRent(form.fill(PropertyAndRent), completedQuestionGroups(PropertyAndRent)))
      case _ => Redirect(controllers.s1_carers_allowance.routes.G1BenefitsMandatory.present())
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g7_propertyAndRent(formWithErrors, completedQuestionGroups(PropertyAndRent))),
      propertyAndRent => claim.update(propertyAndRent) -> Redirect(routes.AboutYou.completed()))
  }
}