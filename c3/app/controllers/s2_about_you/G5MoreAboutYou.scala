package controllers.s2_about_you

import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import controllers.Routing
import utils.helpers.CarersForm._
import controllers.Mappings.validYesNo
import controllers.Mappings.yes

object G5MoreAboutYou extends Controller with Routing with CachedClaim {
  override val route = MoreAboutYou.id -> routes.G5MoreAboutYou.present

  val form = Form(
    mapping(
      "hadPartnerSinceClaimDate" -> nonEmptyText.verifying(validYesNo),
      "eitherClaimedBenefitSinceClaimDate" -> nonEmptyText.verifying(validYesNo),
      "beenInEducationSinceClaimDate" -> nonEmptyText.verifying(validYesNo),
      "receiveStatePension" -> nonEmptyText.verifying(validYesNo)
    )(MoreAboutYou.apply)(MoreAboutYou.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(MoreAboutYou)

  def present = claiming {
    implicit claim => implicit request =>
      val moreAboutYouForm: Form[MoreAboutYou] = claim.questionGroup(MoreAboutYou) match {
        case Some(m: MoreAboutYou) => form.fill(m)
        case _ => form
      }

      claim.questionGroup(ClaimDate) match {
        case Some(n) => Ok(views.html.s2_about_you.g5_moreAboutYou(moreAboutYouForm, completedQuestionGroups))
        case _ => Redirect(controllers.s1_carers_allowance.routes.G1Benefits.present())
      }
  }

  def submit = claiming {
    implicit claim => implicit request =>
      form.bindEncrypted.fold(
        formWithErrors => BadRequest(views.html.s2_about_you.g5_moreAboutYou(formWithErrors, completedQuestionGroups)),
        moreAboutYou => {
          val updatedClaim = showHideYourPartnerSection(claim, moreAboutYou.hadPartnerSinceClaimDate)
          updatedClaim.update(moreAboutYou) -> Redirect(routes.G6Employment.present())
        })
  }

  def showHideYourPartnerSection(claim: Claim, hadPartner:String): Claim = hadPartner match {
    case `yes` => claim.showHideSection(YourPartner.id, true)
    case _ =>  claim.showHideSection(YourPartner.id, false)
  }
}