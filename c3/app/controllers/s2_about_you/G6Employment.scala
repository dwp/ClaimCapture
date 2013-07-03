package controllers.s2_about_you

import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import controllers.Routing
import utils.helpers.CarersForm._

object G6Employment extends Controller with Routing with CachedClaim {
  override val route = Employment.id -> routes.G6Employment.present

  val form = Form(
    mapping(
      "beenSelfEmployedSince1WeekBeforeClaim" -> nonEmptyText,
      "beenEmployedSince6MonthsBeforeClaim" -> nonEmptyText
    )(Employment.apply)(Employment.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.AboutYou.id).takeWhile(_.id != Employment.id)

  def present = claiming {
    implicit claim => implicit request =>
      val employmentForm: Form[Employment] = claim.questionGroup(Employment.id) match {
        case Some(e: Employment) => form.fill(e)
        case _ => form
      }

      claim.questionGroup(models.domain.ClaimDate.id) match {
        case Some(n) => Ok(views.html.s2_about_you.g6_employment(employmentForm, completedQuestionGroups))
        case _ => Redirect(controllers.s1_carers_allowance.routes.G1Benefits.present())
      }
  }

  def submit = claiming {
    implicit claim => implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s2_about_you.g6_employment(formWithErrors, completedQuestionGroups)),
        employment => claim.update(employment) -> Redirect(routes.G7PropertyAndRent.present()))
  }
}