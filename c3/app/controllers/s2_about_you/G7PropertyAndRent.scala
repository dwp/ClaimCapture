package controllers.s2_about_you

import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import controllers.Routing
import utils.helpers.CarersForm._

object G7PropertyAndRent extends Controller with Routing with CachedClaim {
  override val route = PropertyAndRent.id -> routes.G7PropertyAndRent.present

  val form = Form(
    mapping(
      "ownProperty" -> nonEmptyText,
      "hasSublet" -> nonEmptyText
    )(PropertyAndRent.apply)(PropertyAndRent.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(PropertyAndRent)

  def present = claiming { implicit claim => implicit request =>
    val propertyAndRentForm: Form[PropertyAndRent] = claim.questionGroup(PropertyAndRent.id) match {
      case Some(p: PropertyAndRent) => form.fill(p)
      case _ => form
    }

    claim.questionGroup(models.domain.ClaimDate.id) match {
      case Some(n) => Ok(views.html.s2_about_you.g7_propertyAndRent(propertyAndRentForm, completedQuestionGroups))
      case _ => Redirect(controllers.s1_carers_allowance.routes.G1Benefits.present())
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g7_propertyAndRent(formWithErrors, completedQuestionGroups)),
      propertyAndRent => claim.update(propertyAndRent) -> Redirect(routes.AboutYou.completed()))
  }
}