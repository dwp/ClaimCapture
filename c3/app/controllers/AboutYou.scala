package controllers

import models.claim.{YourDetailsForm, CachedClaim}
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

object AboutYou extends Controller with CachedClaim {

  var yourDetailsForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "firstName" -> nonEmptyText,
      "middleName" -> optional(text),
      "surname" -> nonEmptyText,
      "otherNames" -> optional(text),
      "nationalInsuranceNumber" -> optional(text),
      "nationality" -> nonEmptyText,
      "dateOfBirth" -> nonEmptyText,
      "maritalStatus" -> nonEmptyText,
      "alwaysLivedUK" -> boolean
    )(YourDetailsForm.apply)(YourDetailsForm.unapply)
  )

  def yourDetails = claiming {
    implicit claim => implicit request =>
      Ok(views.html.s2_aboutyou.g1_yourDetails(yourDetailsForm))
  }

  def yourDetailsSubmit = claiming {
    implicit claim => implicit request =>
      yourDetailsForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s2_aboutyou.g1_yourDetails(formWithErrors)),
        inputForm => claim.update(inputForm) -> Redirect(routes.AboutYou.contactDetails())
      )
  }

  def contactDetails = claiming {
    implicit claim => implicit request =>
      Ok(views.html.s2_aboutyou.g2_contactDetails())
  }
}