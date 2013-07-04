package controllers.s1_carers_allowance

import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import controllers.Routing
import utils.helpers.CarersForm._
import models.domain.{Claim, Hours}
import models.domain.Breadcrumbs

object G2Hours extends Controller with Routing with CachedClaim {
  override val route = Hours.id -> routes.G2Hours.present

  val form = Form(
    mapping(
      "answer" -> boolean
    )(Hours.apply)(Hours.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(Hours)

  def present = claiming { implicit claim => implicit request =>
    val breadcrumbs = Breadcrumbs(models.domain.CarersAllowance.id)
    if (CarersAllowance.claiming(Hours, claim)) Ok(views.html.s1_carers_allowance.g2_hours(confirmed = true, completedQuestionGroups, completedSections = breadcrumbs.completedSections, activeSection = breadcrumbs.activeSection, futureSections = breadcrumbs.futureSections))
    else Ok(views.html.s1_carers_allowance.g2_hours(confirmed = false, completedQuestionGroups, completedSections = breadcrumbs.completedSections, activeSection = breadcrumbs.activeSection, futureSections = breadcrumbs.futureSections))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => Redirect(routes.G2Hours.present()),
      hours => claim.update(hours) -> Redirect(routes.G3Over16.present()))
  }
}