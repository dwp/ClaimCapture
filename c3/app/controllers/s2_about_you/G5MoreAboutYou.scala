package controllers.s2_about_you

import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import controllers.Routing
import utils.helpers.CarersForm._

object G5MoreAboutYou extends Controller with Routing with CachedClaim {
  override val route = MoreAboutYou.id -> routes.G5MoreAboutYou.present

  val form = Form(
    mapping(
      "hadPartnerSinceClaimDate" -> nonEmptyText,
      "eitherClaimedBenefitSinceClaimDate" -> nonEmptyText,
      "beenInEducationSinceClaimDate" -> nonEmptyText,
      "receiveStatePension" -> nonEmptyText
    )(MoreAboutYou.apply)(MoreAboutYou.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(MoreAboutYou)

  def present = claiming { implicit claim => implicit request =>
    val moreAboutYouForm: Form[MoreAboutYou] = claim.questionGroup(MoreAboutYou.id) match {
      case Some(m: MoreAboutYou) => form.fill(m)
      case _ => form
    }

    claim.questionGroup(models.domain.ClaimDate.id) match {
      case Some(n) => Ok(views.html.s2_about_you.g5_moreAboutYou(moreAboutYouForm, completedQuestionGroups))
      case _ => Redirect(controllers.s1_carers_allowance.routes.G1Benefits.present())
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g5_moreAboutYou(formWithErrors, completedQuestionGroups)),
      moreAboutYou => claim.update(moreAboutYou) -> Redirect(routes.G6Employment.present()))
  }
}