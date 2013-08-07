package controllers.s2_about_you

import language.reflectiveCalls
import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._
import controllers.Mappings.validYesNo
import controllers.Mappings._

object G5MoreAboutYou extends Controller with AboutYouRouting with CachedClaim {
  val form = Form(
    mapping(
      "hadPartnerSinceClaimDate" -> nonEmptyText.verifying(validYesNo),
      "beenInEducationSinceClaimDate" -> nonEmptyText.verifying(validYesNo),
      "receiveStatePension" -> nonEmptyText.verifying(validYesNo)
    )(MoreAboutYou.apply)(MoreAboutYou.unapply))

  def present = claiming { implicit claim => implicit request =>
    claim.questionGroup(ClaimDate) match {
      case Some(n) => Ok(views.html.s2_about_you.g5_moreAboutYou(form.fill(MoreAboutYou), completedQuestionGroups(MoreAboutYou)))
      case _ => Redirect(controllers.s1_carers_allowance.routes.G1Benefits.present())
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g5_moreAboutYou(formWithErrors, completedQuestionGroups(MoreAboutYou))),
      moreAboutYou => {
        val updatedClaim = claim.showHideSection(moreAboutYou.hadPartnerSinceClaimDate == yes, YourPartner)
                                .showHideSection(moreAboutYou.beenInEducationSinceClaimDate == yes, Education)
                                .showHideSection(moreAboutYou.receiveStatePension == no, PayDetails)

        updatedClaim.update(moreAboutYou) -> Redirect(routes.G6Employment.present())
      })
  }
}