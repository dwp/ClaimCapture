package controllers

import models.claim._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._

object AboutYou extends Controller with CachedClaim with FormMappings {
  val yourDetailsForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "firstName" -> nonEmptyText,
      "middleName" -> optional(text),
      "surname" -> nonEmptyText,
      "otherNames" -> optional(text),
      "nationalInsuranceNumber" -> optional(text),
      "nationality" -> nonEmptyText,
      "dateOfBirth" -> date.verifying(validDate),
      "maritalStatus" -> nonEmptyText,
      "alwaysLivedUK" -> nonEmptyText
    )(YourDetails.apply)(YourDetails.unapply)
  )

  val contactDetailsForm = Form(
    mapping(
      "address" -> nonEmptyText,
      "postcode" -> nonEmptyText,
      "phoneNumber" -> optional(text),
      "mobileNumber" -> optional(text)
    )(ContactDetails.apply)(ContactDetails.unapply)
  )

  val claimDateForm = Form(
    mapping(
      "dateOfClaim" -> date.verifying(validDate)
    )(ClaimDate.apply)(ClaimDate.unapply)
  )

  def yourDetails = claiming {
    implicit claim => implicit request =>

      val yourDetailsFormParam: Form[YourDetails] = claim.form(YourDetails.id) match {
        case Some(n: YourDetails) => yourDetailsForm.fill(n)
        case _ => yourDetailsForm
      }

      Ok(views.html.s2_aboutyou.g1_yourDetails(yourDetailsFormParam))

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
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

      val contactDetailsFormParam: Form[ContactDetails]=  claim.form(ContactDetails.id) match {
        case Some(n: ContactDetails) =>  contactDetailsForm.fill(n)
        case _ => contactDetailsForm
      }

      Ok(views.html.s2_aboutyou.g2_contactDetails(contactDetailsFormParam,completedForms.takeWhile(_.id != ContactDetails.id)))
  }

  def contactDetailsSubmit = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

      contactDetailsForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s2_aboutyou.g2_contactDetails(formWithErrors,completedForms.takeWhile(_.id != ContactDetails.id))),
        inputForm => claim.update(inputForm) -> Redirect(routes.AboutYou.claimDate())
      )
  }

  def claimDate = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.ClaimDate.id)

      val claimDateFormParam: Form[ClaimDate] = claim.form(ClaimDate.id) match {
        case Some(n: ClaimDate) =>  claimDateForm.fill(n)
        case _ => claimDateForm
      }

      Ok(views.html.s2_aboutyou.g4_claimDate(claimDateFormParam,completedForms.takeWhile(_.id != ClaimDate.id)))
  }


  def claimDateSubmit = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

      claimDateForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s2_aboutyou.g4_claimDate(formWithErrors,completedForms.takeWhile(_.id != ClaimDate.id))),
        inputForm => claim.update(inputForm) -> Redirect(routes.AboutYou.moreAboutYou())
      )
  }

  def moreAboutYou = TODO 
  def moreAboutYouSubmit = TODO 
  def employment = TODO 
  def employmentSubmit = TODO 
  def propertyAndRent = TODO 
  def propertyAndRentSubmit = TODO 
  def completed = TODO 
  def completedSubmit = TODO 
}