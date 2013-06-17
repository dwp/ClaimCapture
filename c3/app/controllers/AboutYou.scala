package controllers

import models.claim._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._

object AboutYou extends Controller with CachedClaim with FormMappings {
  val route = Map(YourDetails.id -> routes.AboutYou.yourDetails,
                  ContactDetails.id -> routes.AboutYou.contactDetails,
                  TimeOutsideUK.id -> routes.AboutYou.timeOutsideUK(),
                  ClaimDate.id -> routes.AboutYou.claimDate,
                  MoreAboutYou.id -> routes.AboutYou.moreAboutYou,
                  Employment.id -> routes.AboutYou.employment,
                  PropertyAndRent.id -> routes.AboutYou.propertyAndRent)

  val yourDetailsForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "firstName" -> nonEmptyText,
      "middleName" -> optional(text),
      "surname" -> nonEmptyText,
      "otherNames" -> optional(text),
      "nationalInsuranceNumber" -> optional(text verifying(pattern( """^([a-zA-Z]){2}( )?([0-9]){2}( )?([0-9]){2}( )?([0-9]){2}( )?([a-zA-Z]){1}?$""".r,
                                                          "constraint.nationalInsuranceNumber", "error.nationalInsuranceNumber"), maxLength(10))),
      "nationality" -> nonEmptyText,
      "dateOfBirth" -> date.verifying(validDate),
      "maritalStatus" -> nonEmptyText,
      "alwaysLivedUK" -> nonEmptyText
    )(YourDetails.apply)(YourDetails.unapply)
  )

  val contactDetailsForm = Form(
    mapping(
      "address" -> address.verifying(requiredAddress),
      "postcode" -> optional(text verifying(pattern( """^(GIR 0AA)|((([A-Z][0-9][0-9]?)|(([A-Z][A-HJ-Y][0-9][0-9]?)|(([A-Z][0-9][A-Z])|([A-Z][A-HJ-Y][0-9]?[A-Z])))) [0-9][A-Z]{2})$""".r,
        "constraint.postcode", "error.postcode"), maxLength(10))),
      "phoneNumber" -> optional(text),
      "mobileNumber" -> optional(text)
    )(ContactDetails.apply)(ContactDetails.unapply)
  )

  val timeOutsideUKForm = Form(
    mapping(
      "currentlyLivingInUK" -> nonEmptyText(),
      "arrivedInUK" -> optional(date.verifying(validDate)),
      "originCountry" -> optional(text),
      "planToGoBack" -> text,
      "whenPlanToGoBack"-> optional(date.verifying(validDate)),
      "visaReference" -> optional(text)
    )(TimeOutsideUK.apply)(TimeOutsideUK.unapply)
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

  val employmentForm = Form(
    mapping(
      "beenSelfEmployedSince1WeekBeforeClaim" -> nonEmptyText,
      "beenEmployedSince6MonthsBeforeClaim" -> nonEmptyText
    )(Employment.apply)(Employment.unapply)
  )

  val propertyAndRentForm = Form(
    mapping(
      "ownProperty" -> nonEmptyText,
      "hasSublet" -> nonEmptyText
    )(PropertyAndRent.apply)(PropertyAndRent.unapply)
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
        inputForm => claim.update(inputForm) -> Redirect(routes.AboutYou.contactDetails()))
  }

  def contactDetails = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

      val contactDetailsFormParam: Form[ContactDetails] = claim.form(ContactDetails.id) match {
        case Some(n: ContactDetails) => contactDetailsForm.fill(n)
        case _ => contactDetailsForm
      }

      Ok(views.html.s2_aboutyou.g2_contactDetails(contactDetailsFormParam, completedForms.takeWhile(_.id != ContactDetails.id)))
  }

  def contactDetailsSubmit = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

      contactDetailsForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s2_aboutyou.g2_contactDetails(formWithErrors, completedForms.takeWhile(_.id != ContactDetails.id))),
        inputForm => claim.update(inputForm) -> Redirect(routes.AboutYou.timeOutsideUK()))
  }

  def timeOutsideUK = claiming {
    implicit claim => implicit request =>
      claim.form(YourDetails.id) match {
        case Some(YourDetails(_, _, _, _, _, _, _, _, _, "yes")) => Redirect(routes.AboutYou.claimDate())
        case _ =>
          val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

          val timeOutsideUKFormParam: Form[TimeOutsideUK] = claim.form(TimeOutsideUK.id) match {
            case Some(n: TimeOutsideUK) => timeOutsideUKForm.fill(n)
            case _ => timeOutsideUKForm
          }

          Ok(views.html.s2_aboutyou.g3_timeOutsideUK(timeOutsideUKFormParam, completedForms.takeWhile(_.id != TimeOutsideUK.id)))
      }
  }

  def timeOutsideUKSubmit = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

      timeOutsideUKForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s2_aboutyou.g3_timeOutsideUK(formWithErrors, completedForms.takeWhile(_.id != TimeOutsideUK.id))),
        inputForm => claim.update(inputForm) -> Redirect(routes.AboutYou.claimDate()))
  }

  def claimDate = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)
      val claimDateFormParam: Form[ClaimDate] = claim.form(ClaimDate.id) match {
        case Some(n: ClaimDate) => claimDateForm.fill(n)
        case _ => claimDateForm
      }

      Ok(views.html.s2_aboutyou.g4_claimDate(claimDateFormParam, completedForms.takeWhile(_.id != ClaimDate.id)))
  }

  def claimDateSubmit = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

      claimDateForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s2_aboutyou.g4_claimDate(formWithErrors, completedForms.takeWhile(_.id != ClaimDate.id))),
        inputForm => claim.update(inputForm) -> Redirect(routes.AboutYou.moreAboutYou()))
  }

  def moreAboutYou = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

      val moreAboutYouFormParam: Form[MoreAboutYou] = claim.form(MoreAboutYou.id) match {
        case Some(n: MoreAboutYou) => moreAboutYouForm.fill(n)
        case _ => moreAboutYouForm
      }

      claim.form(models.claim.ClaimDate.id) match {
        case Some(n) => Ok(views.html.s2_aboutyou.g5_moreAboutYou(moreAboutYouFormParam, completedForms.takeWhile(_.id != MoreAboutYou.id)))
        case _ => Redirect(routes.CarersAllowance.benefits())
      }
  }

  def moreAboutYouSubmit = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

      moreAboutYouForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s2_aboutyou.g5_moreAboutYou(formWithErrors, completedForms.takeWhile(_.id != MoreAboutYou.id))),
        inputForm => claim.update(inputForm) -> Redirect(routes.AboutYou.employment()))
  }

  def employment = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

      val employmentFormParam: Form[Employment] = claim.form(Employment.id) match {
        case Some(n: Employment) => employmentForm.fill(n)
        case _ => employmentForm
      }

      claim.form(models.claim.ClaimDate.id) match {
        case Some(n) => Ok(views.html.s2_aboutyou.g6_employment(employmentFormParam, completedForms.takeWhile(_.id != Employment.id)))
        case _ => Redirect(routes.CarersAllowance.benefits())
      }
  }

  def employmentSubmit = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

      employmentForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s2_aboutyou.g6_employment(formWithErrors, completedForms.takeWhile(_.id != Employment.id))),
        inputForm => claim.update(inputForm) -> Redirect(routes.AboutYou.propertyAndRent()))
  }

  def propertyAndRent = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

      val propertyAndRentFormParam: Form[PropertyAndRent] = claim.form(PropertyAndRent.id) match {
        case Some(n: PropertyAndRent) => propertyAndRentForm.fill(n)
        case _ => propertyAndRentForm
      }

      claim.form(models.claim.ClaimDate.id) match {
        case Some(n) => Ok(views.html.s2_aboutyou.g7_propertyAndRent(propertyAndRentFormParam, completedForms.takeWhile(_.id != PropertyAndRent.id)))
        case _ => Redirect(routes.CarersAllowance.benefits())
      }
  }

  def propertyAndRentSubmit = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

      propertyAndRentForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s2_aboutyou.g7_propertyAndRent(formWithErrors, completedForms.takeWhile(_.id != PropertyAndRent.id))),
        inputForm => claim.update(inputForm) -> Redirect(routes.AboutYou.completed()))
  }

  def completed = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

      Ok(views.html.s2_aboutyou.g8_completed(completedForms))
  }

  def completedSubmit = claiming {
    implicit claim => implicit request =>
      val completedForms = claim.completedFormsForSection(models.claim.AboutYou.id)

      if (completedForms.distinct.size == 6) Redirect(routes.YourPartner.yourPartner())
      else Redirect(routes.AboutYou.yourDetails())
  }
}