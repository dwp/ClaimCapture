package controllers.s3_your_partner

import models.domain.Claim
import controllers.{Routing}
import controllers.Mappings._
import models.domain.MoreAboutYourPartner
import models.view.CachedClaim
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.nonEmptyText
import play.api.mvc.Controller

object G3MoreAboutYourPartner extends Controller with Routing with CachedClaim {

  override val route = MoreAboutYourPartner.id -> routes.G3MoreAboutYourPartner.present

  val form = Form(
    mapping(
      "dateStartedLivingTogether" -> dayMonthYear.verifying(validDate),
      "separatedFromPartner" -> nonEmptyText
    )(MoreAboutYourPartner.apply)(MoreAboutYourPartner.unapply))


  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(models.domain.YourPartner.id).filter(q => q.id < MoreAboutYourPartner.id)

  def present = claiming {
    implicit claim => implicit request =>

      Ok(views.html.s3_your_partner.g3_moreAboutYourPartner(form, completedQuestionGroups))
  }

  def submit = claiming {
    implicit claim => implicit request =>

      Ok("submit")
  }

}
