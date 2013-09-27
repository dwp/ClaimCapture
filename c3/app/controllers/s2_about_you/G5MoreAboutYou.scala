package controllers.s2_about_you

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._
import controllers.Mappings.validYesNo
import controllers.Mappings._
import models.domain._

object G5MoreAboutYou extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "hadPartnerSinceClaimDate" -> nonEmptyText.verifying(validYesNo),
    "beenInEducationSinceClaimDate" -> nonEmptyText.verifying(validYesNo),
    "receiveStatePension" -> nonEmptyText.verifying(validYesNo)
  )(MoreAboutYou.apply)(MoreAboutYou.unapply))

  def present = claiming { implicit claim => implicit request =>
    claim.questionGroup(ClaimDate) match {
      case Some(n) => track(MoreAboutYou) { implicit claim => Ok(views.html.s2_about_you.g5_moreAboutYou(form.fill(MoreAboutYou))) }
      case _ => Redirect("/")
    }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g5_moreAboutYou(formWithErrors)),
      moreAboutYou => {
        val updatedClaim = claim.showHideSection(moreAboutYou.hadPartnerSinceClaimDate == yes, YourPartner)
                                .showHideSection(moreAboutYou.beenInEducationSinceClaimDate == yes, Education)
                                .showHideSection(moreAboutYou.receiveStatePension == no, PayDetails)

        updatedClaim.update(moreAboutYou) -> Redirect(routes.G6Employment.present())
      })
  }
}