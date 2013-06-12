package controllers

import models.claim.{Hours, ContactDetails, YourDetails, CachedClaim}
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
    )(YourDetails.apply)(YourDetails.unapply)
  )

  var contactDetailsForm = Form(
    mapping(
      "address" -> nonEmptyText,
      "postcode" -> nonEmptyText,
      "phoneNumber" -> optional(text),
      "mobileNumber" -> optional(text)
    )(ContactDetails.apply)(ContactDetails.unapply)
  )

  def yourDetails = claiming {
    implicit claim => implicit request =>
      Ok(views.html.s2_aboutyou.g1_yourDetails(yourDetailsForm))
  }

  def yourDetailsSubmit = claiming {
    implicit claim => implicit request =>

//      val action = request.body.asFormUrlEncoded.get("action")(0)
//      println(action)

      yourDetailsForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s2_aboutyou.g1_yourDetails(formWithErrors)),
        inputForm => claim.update(inputForm) -> Redirect(routes.AboutYou.contactDetails())
      )
  }

  def contactDetails = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)
      Ok(views.html.s2_aboutyou.g2_contactDetails(contactDetailsForm,completedForms.takeWhile(_.id != ContactDetails.id)))
  }

  def contactDetailsSubmit = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)
      contactDetailsForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s2_aboutyou.g2_contactDetails(formWithErrors,completedForms.takeWhile(_.id != ContactDetails.id))),
        inputForm => claim.update(inputForm) -> Redirect(routes.AboutYou.claimDate())
      )
  }

  def claimDate = TODO 
  def claimDateSubmit = TODO 
  def moreAboutYou = TODO 
  def moreAboutYouSubmit = TODO 
  def employment = TODO 
  def employmentSubmit = TODO 
  def propertyAndRent = TODO 
  def propertyAndRentSubmit = TODO 
  def completed = TODO 
  def completedSubmit = TODO 
}