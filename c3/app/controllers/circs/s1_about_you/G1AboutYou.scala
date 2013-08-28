package controllers.circs.s1_about_you

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import play.api.mvc.Controller
import models.domain._
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._



object G1AboutYou extends Controller with CachedClaim with Navigable {

  val title = "title"
  val firstName = "firstName"
  val middleName = "middleName"
  val lastName = "lastName"
  val nationalInsuranceNumber = "nationalInsuranceNumber"
  val dateOfBirth = "dateOfBirth"


  val form = Form(mapping(
    title -> nonEmptyText(maxLength = 4),
    firstName -> nonEmptyText(maxLength = Name.maxLength),
    middleName -> optional(text(maxLength = Name.maxLength)),
    lastName -> nonEmptyText(maxLength = Name.maxLength),
    nationalInsuranceNumber -> nino.verifying(filledInNino, validNino),
    dateOfBirth -> dayMonthYear.verifying(validDate)
  )(CircumstancesAboutYou.apply)(CircumstancesAboutYou.unapply))

  def present = claiming { implicit claim => implicit request =>
    track(CircumstancesAboutYou) { implicit claim => Ok(views.html.circumstances.s1_about_you.g1_aboutYou(form.fill(CircumstancesAboutYou))) }
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.circumstances.s1_about_you.g1_aboutYou(formWithErrors)),
      circumstancesAboutYou => claim.update(circumstancesAboutYou) -> Redirect(routes.G1AboutYou.present()))
  }

}
