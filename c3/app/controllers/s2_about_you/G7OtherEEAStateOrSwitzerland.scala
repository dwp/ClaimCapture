package controllers.s2_about_you

import language.reflectiveCalls
import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.CachedClaim
import models.domain.OtherEEAStateOrSwitzerland
import controllers.Mappings._
import utils.helpers.CarersForm._
import models.view.Navigable

object G7OtherEEAStateOrSwitzerland extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "benefitsFromOtherEEAStateOrSwitzerland" -> nonEmptyText.verifying(validYesNo),
    "claimedForBenefitsFromOtherEEAStateOrSwitzerland" -> nonEmptyText.verifying(validYesNo),
    "workingForOtherEEAStateOrSwitzerland" -> nonEmptyText.verifying(validYesNo)
  )(OtherEEAStateOrSwitzerland.apply)(OtherEEAStateOrSwitzerland.unapply))

  def present = claiming { implicit claim => implicit request =>
    track(OtherEEAStateOrSwitzerland) { implicit claim => Ok(views.html.s2_about_you.g7_otherEEAStateOrSwitzerland(form.fill(OtherEEAStateOrSwitzerland))) }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g7_otherEEAStateOrSwitzerland(formWithErrors)),
      benefitsFromOtherEEAStateOrSwitzerland => claim.update(benefitsFromOtherEEAStateOrSwitzerland) -> Redirect(routes.G8MoreAboutYou.present()))
  }
}
