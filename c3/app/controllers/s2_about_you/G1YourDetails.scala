package controllers.s2_about_you

import models.domain._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import play.api.mvc.Controller
import models.view.CachedClaim
import utils.helpers.CarersForm._

object G1YourDetails extends Controller with CachedClaim {
  val form = Form(
    mapping(
      call(routes.G1YourDetails.present()),
      "title" -> nonEmptyText(maxLength = 4),
      "firstName" -> nonEmptyText(maxLength = sixty),
      "middleName" -> optional(text(maxLength = sixty)),
      "surname" -> nonEmptyText(maxLength = sixty),
      "otherNames" -> optional(text(maxLength = sixty)),
      "nationalInsuranceNumber" -> optional(nino.verifying(validNino)),
      "nationality" -> nonEmptyText(maxLength = sixty),
      "dateOfBirth" -> dayMonthYear.verifying(validDate),
      "maritalStatus" -> nonEmptyText(maxLength = 1),
      "alwaysLivedUK" -> nonEmptyText.verifying(validYesNo)
    )(YourDetails.apply)(YourDetails.unapply)
  )

  def present = claiming { implicit claim => implicit request =>
    val yourDetailsForm: Form[YourDetails] = claim.questionGroup(YourDetails) match {
      case Some(y: YourDetails) => form.fill(y)
      case _ => form
    }

    Ok(views.html.s2_about_you.g1_yourDetails(yourDetailsForm))
  }

  def submit = claiming { implicit claim => implicit request =>
    form.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_about_you.g1_yourDetails(formWithErrors)),
      yourDetails => claim.update(yourDetails) -> Redirect(routes.G2ContactDetails.present()))
  }
}