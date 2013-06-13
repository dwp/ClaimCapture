package controllers

import models.claim.{ContactDetails, YourDetails, CachedClaim}
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.DayMonthYear

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
      "dateOfBirth" -> mapping(
        "day" -> number,
        "month" -> number,
        "year" -> number
      )(DayMonthYear.apply)(DayMonthYear.unapply),
      "maritalStatus" -> nonEmptyText,
      "alwaysLivedUK" -> nonEmptyText,
      "action" -> optional(text)
    )(YourDetails.apply)(YourDetails.unapply)
  )

  var contactDetailsForm = Form(
    mapping(
      "address" -> nonEmptyText,
      "postcode" -> nonEmptyText,
      "phoneNumber" -> optional(text),
      "mobileNumber" -> optional(text),
      "action" -> optional(text)
    )(ContactDetails.apply)(ContactDetails.unapply)
  )

  def yourDetails = claiming {
    implicit claim => implicit request =>
      val formContentOption = claim.form(YourDetails.id)
      var filledForm: Form[YourDetails] = yourDetailsForm
      formContentOption match {
        case Some(n) =>  filledForm = yourDetailsForm.fill(n.asInstanceOf[YourDetails])
        case _ =>
      }

      Ok(views.html.s2_aboutyou.g1_yourDetails(filledForm))

  }

  def yourDetailsSubmit = claiming {
    implicit claim => implicit request =>
      yourDetailsForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s2_aboutyou.g1_yourDetails(formWithErrors)),
        inputForm => claim.update(inputForm) -> Redirect(inputForm.findNext)
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
        inputForm => claim.update(inputForm) -> Redirect(inputForm.findNext)
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