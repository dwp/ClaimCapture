package controllers.s2_about_you

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain._
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._
import controllers.CarersForms._
import play.api.Logger

object G1YourDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "title" -> nonEmptyText(maxLength = 4),
    "firstName" -> carersNonEmptyText(maxLength = 17),
    "middleName" -> optional(carersText(maxLength = 17)),
    "surname" -> carersNonEmptyText(maxLength = Name.maxLength),
    "otherNames" -> optional(carersText(maxLength = Name.maxLength)),
    "nationalInsuranceNumber" -> nino.verifying(filledInNino,validNino),
    "dateOfBirth" -> dayMonthYear.verifying(validDate),
    "receiveStatePension" -> nonEmptyText.verifying(validYesNo)
  )(YourDetails.apply)(YourDetails.unapply))

  def present = claiming { implicit claim => implicit request => implicit lang =>
    Logger.info(s"Start your details ${claim.uuid}")
    track(YourDetails) { implicit claim => Ok(views.html.s2_about_you.g1_yourDetails(form.fill(YourDetails))) }
  }

  def submit = claiming { implicit claim => implicit request => implicit lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g1_yourDetails(formWithErrors)),
      yourDetails => {
          val updatedClaim = claim.showHideSection(yourDetails.receiveStatePension == no, PayDetails)
          updatedClaim.update(yourDetails) -> Redirect(routes.G2ContactDetails.present())
      })
  }
}