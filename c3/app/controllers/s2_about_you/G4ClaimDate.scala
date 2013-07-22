package controllers.s2_about_you

import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._

object G4ClaimDate extends Controller with CachedClaim {
  val form = Form(
    mapping(
      call(routes.G4ClaimDate.present()),
      "dateOfClaim" -> dayMonthYear.verifying(validDate)
    )(ClaimDate.apply)(ClaimDate.unapply))

  def completedQuestionGroups(implicit claim: Claim) = claim.completedQuestionGroups(ClaimDate)

  def present = claiming { implicit claim => implicit request =>
    val claimDateForm: Form[ClaimDate] = claim.questionGroup(ClaimDate) match {
      case Some(c: ClaimDate) => form.fill(c)
      case _ => form
    }

    Ok(views.html.s2_about_you.g4_claimDate(claimDateForm, completedQuestionGroups))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g4_claimDate(formWithErrors, completedQuestionGroups)),
      claimDate => claim.update(claimDate) -> Redirect(routes.G5MoreAboutYou.present()))
  }
}