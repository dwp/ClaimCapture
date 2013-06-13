package controllers

import models.claim.{ClaimDate, ContactDetails, YourDetails, CachedClaim}
import play.api.mvc._
import play.api.data.{Mapping, Form}
import play.api.data.Forms._
import models.DayMonthYear
import play.api.data.validation._
import models.DayMonthYear
import play.api.data.validation.ValidationError

object AboutYou extends Controller with CachedClaim {


  def nonEmptyDateConstraint: Constraint[DayMonthYear] = Constraint[DayMonthYear]("constraint.required") { o =>
    if (o.year == 0) Invalid(ValidationError("error.required")) else Valid
  }

  val date: Mapping[DayMonthYear] = (mapping(
                                        "day" -> number,
                                        "month" -> number,
                                        "year" -> number)(DayMonthYear.apply)(DayMonthYear.unapply))

  val yourDetailsForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "firstName" -> nonEmptyText,
      "middleName" -> optional(text),
      "surname" -> nonEmptyText,
      "otherNames" -> optional(text),
      "nationalInsuranceNumber" -> optional(text),
      "nationality" -> nonEmptyText,
      "dateOfBirth" -> (date verifying nonEmptyDateConstraint),
      "maritalStatus" -> nonEmptyText,
      "alwaysLivedUK" -> nonEmptyText,
      "action" -> optional(text)
    )(YourDetails.apply)(YourDetails.unapply)
  )

  val contactDetailsForm = Form(
    mapping(
      "address" -> nonEmptyText,
      "postcode" -> nonEmptyText,
      "phoneNumber" -> optional(text),
      "mobileNumber" -> optional(text),
      "action" -> optional(text)
    )(ContactDetails.apply)(ContactDetails.unapply)
  )

  val claimDateForm = Form(
    mapping(
      "dateOfClaim" -> (date verifying nonEmptyDateConstraint),
      "action" -> optional(text)
    )(ClaimDate.apply)(ClaimDate.unapply)
  )

  def yourDetails = claiming {
    implicit claim => implicit request =>

      val yourDetailsFormParam: Form[YourDetails] = claim.form(YourDetails.id) match {
        case Some(n) =>  yourDetailsForm.fill(n.asInstanceOf[YourDetails])
        case _ => yourDetailsForm
      }

      Ok(views.html.s2_aboutyou.g1_yourDetails(yourDetailsFormParam))

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
      val contactDetailsFormParam: Form[ContactDetails]=  claim.form(ContactDetails.id) match {
        case Some(n) =>  contactDetailsForm.fill(n.asInstanceOf[ContactDetails])
        case _ => contactDetailsForm
      }

      Ok(views.html.s2_aboutyou.g2_contactDetails(contactDetailsFormParam,completedForms.takeWhile(_.id != ContactDetails.id)))
  }

  def contactDetailsSubmit = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

      contactDetailsForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s2_aboutyou.g2_contactDetails(formWithErrors,completedForms.takeWhile(_.id != ContactDetails.id))),
        inputForm => claim.update(inputForm) -> Redirect(inputForm.findNext)
      )
  }

  def claimDate = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.ClaimDate.id)
      val claimDateFormParam: Form[ClaimDate] = claim.form(ClaimDate.id) match {
        case Some(n) =>  claimDateForm.fill(n.asInstanceOf[ClaimDate])
        case _ => claimDateForm
      }
      Ok(views.html.s2_aboutyou.g3_claimDate(claimDateFormParam,completedForms.takeWhile(_.id != ContactDetails.id)))
  }


  def claimDateSubmit = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.ClaimDate.id)

      claimDateForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s2_aboutyou.g3_claimDate(formWithErrors,completedForms.takeWhile(_.id != ClaimDate.id))),
        inputForm => claim.update(inputForm) -> Redirect(inputForm.findNext)
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