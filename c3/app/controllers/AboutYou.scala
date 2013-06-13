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


  val moreAboutYouForm = Form(
    mapping(
      "hadPartnerSinceClaimDate" -> nonEmptyText,
      "eitherClaimedBenefitSinceClaimDate" -> nonEmptyText,
      "beenInEducationSinceClaimDate" -> nonEmptyText,
      "receiveStatePension" -> nonEmptyText
    )(MoreAboutYou.apply)(MoreAboutYou.unapply)
  )

  def goto(formID: String) = Action {
    formID match {
      case YourDetails.id => Redirect(routes.AboutYou.yourDetails())
      case ContactDetails.id => Redirect(routes.AboutYou.contactDetails())
      case ClaimDate.id => Redirect(routes.AboutYou.claimDate())
      case MoreAboutYou.id => Redirect(routes.AboutYou.moreAboutYou())
    }
  }

  val employmentForm = Form(
    mapping(
      "beenSelfEmployedSince1WeekBeforeClaim" -> nonEmptyText,
      "beenEmployedSince6MonthsBeforeClaim" -> nonEmptyText
    )(Employment.apply)(Employment.unapply)
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
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

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

  def moreAboutYou = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

      val moreAboutYouFormParam: Form[MoreAboutYou] = claim.form(MoreAboutYou.id) match {
        case Some(n: MoreAboutYou) =>  moreAboutYouForm.fill(n)
        case _ => moreAboutYouForm
      }

      Ok(views.html.s2_aboutyou.g5_moreAboutYou(moreAboutYouFormParam,completedForms.takeWhile(_.id != MoreAboutYou.id)))
  }


  def moreAboutYouSubmit = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

      moreAboutYouForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s2_aboutyou.g5_moreAboutYou(formWithErrors,completedForms.takeWhile(_.id != MoreAboutYou.id))),
        inputForm => claim.update(inputForm) -> Redirect(routes.AboutYou.employment())
      )
  }

  def employment = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

      val employmentFormParam: Form[Employment] = claim.form(Employment.id) match {
        case Some(n: Employment) =>  employmentForm.fill(n)
        case _ => employmentForm
      }

      Ok(views.html.s2_aboutyou.g6_employment(employmentFormParam,completedForms.takeWhile(_.id != Employment.id)))
  }

  def employmentSubmit = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

      employmentForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s2_aboutyou.g6_employment(formWithErrors,completedForms.takeWhile(_.id != Employment.id))),
        inputForm => claim.update(inputForm) -> Redirect(routes.AboutYou.propertyAndRent())
      )
  }

  def propertyAndRent = TODO 
  def propertyAndRentSubmit = TODO 
  def completed = TODO 
  def completedSubmit = TODO 
}