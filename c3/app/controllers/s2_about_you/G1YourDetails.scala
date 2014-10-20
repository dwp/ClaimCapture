package controllers.s2_about_you

import language.reflectiveCalls
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Controller
import controllers.Mappings._
import models.domain._
import models.DayMonthYearComparator.compare
import models.view.{Navigable, CachedClaim}
import utils.helpers.CarersForm._
import controllers.CarersForms._
import play.api.Logger
import scala.language.postfixOps

object G1YourDetails extends Controller with CachedClaim with Navigable {
  val form = Form(mapping(
    "title" -> carersNonEmptyText(maxLength = 4),
    "firstName" -> carersNonEmptyText(maxLength = 17),
    "middleName" -> optional(carersText(maxLength = 17)),
    "surname" -> carersNonEmptyText(maxLength = Name.maxLength),
    "otherNames" -> optional(carersText(maxLength = Name.maxLength)),
    "nationalInsuranceNumber" -> nino.verifying(filledInNino,validNino),
    "dateOfBirth" -> dayMonthYear.verifying(validDate)
  )(YourDetails.apply)(YourDetails.unapply))

  def present = claiming {implicit claim =>  implicit request =>  lang =>
    Logger.info(s"Start your details ${claim.uuid}")
    track(YourDetails) { implicit claim => Ok(views.html.s2_about_you.g1_yourDetails(form.fill(YourDetails))(lang)) }
  }

  def submit = claiming {implicit claim =>  implicit request =>  lang =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g1_yourDetails(formWithErrors)(lang)),
      yourDetails => { // Show pay details if the person is below 62 years of age on the day of the claim (claim date)
          val payDetailsVisible = showPayDetails(claim, yourDetails)
          val updatedClaim = claim.showHideSection(payDetailsVisible, PayDetails)
          updatedClaim.update(yourDetails) -> Redirect(routes.G2ContactDetails.present())
      })
  }

  def showPayDetails(claim:Claim, yourDetails:YourDetails):Boolean = {
    claim.dateOfClaim match {
      case Some(dmy) => compare(Some(dmy.withTime(0,0) - 62 years),Some(yourDetails.dateOfBirth.withTime(0,0))) < 0
      case _ => false
    }
  }
}

