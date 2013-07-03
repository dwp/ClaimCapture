package controllers.s3_your_partner

import models.domain.Claim
import controllers.Routing
import controllers.Mappings._
import models.domain.MoreAboutYourPartner
import models.view.CachedClaim
import play.api.data.Forms.mapping
import play.api.data.Forms.nonEmptyText
import play.api.data.Form
import play.api.mvc.Controller
import utils.helpers.CarersForm.formBinding

object G3MoreAboutYourPartner extends Controller with Routing with CachedClaim {

  override val route = MoreAboutYourPartner.id -> routes.G3MoreAboutYourPartner.present

  val form = Form(
    mapping(
      "dateStartedLivingTogether" -> dayMonthYear.verifying(validDate),
      "separatedFromPartner" -> nonEmptyText
    )(MoreAboutYourPartner.apply)(MoreAboutYourPartner.unapply))


  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(MoreAboutYourPartner)

  def present = claiming { implicit claim => implicit request =>
    val currentForm: Form[MoreAboutYourPartner] = claim.questionGroup(MoreAboutYourPartner.id) match {
      case Some(t: MoreAboutYourPartner) => form.fill(t)
      case _ => form
    }

    Ok(views.html.s3_your_partner.g3_moreAboutYourPartner(currentForm, completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s3_your_partner.g3_moreAboutYourPartner(formWithErrors, completedQuestionGroups)),
      f => claim.update(f) -> Redirect(routes.G4DateOfSeparation.present))
  }
}