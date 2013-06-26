package controllers

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import models.view.CachedClaim
import models.domain._
import scala.Some
import Mappings._
import scala.collection.immutable.ListMap
import utils.helpers.CarersForm._

object AboutYou extends Controller with CachedClaim {
  val route: ListMap[String, Call] = ListMap(YourDetails.id -> routes.AboutYou.yourDetails,
                                            ContactDetails.id -> routes.AboutYou.contactDetails,
                                            TimeOutsideUK.id -> routes.AboutYou.timeOutsideUK(),
                                            ClaimDate.id -> routes.AboutYou.claimDate,
                                            MoreAboutYou.id -> routes.AboutYou.moreAboutYou,
                                            Employment.id -> routes.AboutYou.employment,
                                            PropertyAndRent.id -> routes.AboutYou.propertyAndRent).asInstanceOf[ListMap[String,Call]]

  val yourDetailsForm = Form(
    mapping(
      "title" -> nonEmptyText(maxLength = 4),
      "firstName" -> nonEmptyText(maxLength = sixty),
      "middleName" -> optional(text(maxLength = sixty)),
      "surname" -> nonEmptyText(maxLength = sixty),
      "otherNames" -> optional(text(maxLength = sixty)),
      "nationalInsuranceNumber" -> optional(nino.verifying(validNino)),
      "nationality" -> nonEmptyText(maxLength = sixty),
      "dateOfBirth" -> dayMonthYear.verifying(validDate),
      "maritalStatus" -> nonEmptyText(maxLength = 1),
      "alwaysLivedUK" -> nonEmptyText
    )(YourDetails.apply)(YourDetails.unapply))

  val contactDetailsForm = Form(
    mapping(
      "address" -> address.verifying(requiredAddress),
      "postcode" -> optional(text verifying(validPostcode)),
      "phoneNumber" -> optional(text verifying(pattern( """[0-9 \-]{1,20}""".r,
        "constraint.invalid", "error.invalid"))),
      "mobileNumber" -> optional(text)
    )(ContactDetails.apply)(ContactDetails.unapply))

  val timeOutsideUKForm = Form(
    mapping(
      "currentlyLivingInUK" -> nonEmptyText(),
      "arrivedInUK" -> optional(dayMonthYear.verifying(validDate)),
      "originCountry" -> optional(text(maxLength = sixty)),
      "planToGoBack" -> optional(text),
      "whenPlanToGoBack"-> optional(dayMonthYear.verifying(validDate)),
      "visaReference" -> optional(text(maxLength = sixty))
    )(TimeOutsideUK.apply)(TimeOutsideUK.unapply))

  val claimDateForm = Form(
    mapping(
      "dateOfClaim" -> dayMonthYear.verifying(validDate)
    )(ClaimDate.apply)(ClaimDate.unapply))

  val moreAboutYouForm = Form(
    mapping(
      "hadPartnerSinceClaimDate" -> nonEmptyText,
      "eitherClaimedBenefitSinceClaimDate" -> nonEmptyText,
      "beenInEducationSinceClaimDate" -> nonEmptyText,
      "receiveStatePension" -> nonEmptyText
    )(MoreAboutYou.apply)(MoreAboutYou.unapply))

  val employmentForm = Form(
    mapping(
      "beenSelfEmployedSince1WeekBeforeClaim" -> nonEmptyText,
      "beenEmployedSince6MonthsBeforeClaim" -> nonEmptyText
    )(Employment.apply)(Employment.unapply))

  val propertyAndRentForm = Form(
    mapping(
      "ownProperty" -> nonEmptyText,
      "hasSublet" -> nonEmptyText
    )(PropertyAndRent.apply)(PropertyAndRent.unapply))

  def yourDetails = claiming { implicit claim => implicit request =>
    val yourDetailsFormParam: Form[YourDetails] = claim.questionGroup(YourDetails.id) match {
      case Some(n: YourDetails) => yourDetailsForm.fill(n)
      case _ => yourDetailsForm
    }

    Ok(views.html.s2_aboutyou.g1_yourDetails(yourDetailsFormParam))
  }

  def yourDetailsSubmit = claiming { implicit claim => implicit request =>
    yourDetailsForm.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_aboutyou.g1_yourDetails(formWithErrors)),
      yourDetails => claim.update(yourDetails) -> Redirect(routes.AboutYou.contactDetails()))
  }

  def contactDetails = claiming { implicit claim => implicit request =>
    val completedForms = claim.completedQuestionGroups(models.domain.AboutYou.id)

    val contactDetailsQGForm: Form[ContactDetails] = claim.questionGroup(ContactDetails.id) match {
      case Some(c: ContactDetails) => contactDetailsForm.fill(c)
      case _ => contactDetailsForm
    }

    Ok(views.html.s2_aboutyou.g2_contactDetails(contactDetailsQGForm, completedForms.takeWhile(_.id != ContactDetails.id)))
  }

  def contactDetailsSubmit = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.AboutYou.id)

    contactDetailsForm.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_aboutyou.g2_contactDetails(formWithErrors, completedQuestionGroups.takeWhile(_.id != ContactDetails.id))),
      contactDetails => claim.update(contactDetails) -> Redirect(routes.AboutYou.timeOutsideUK()))
  }

  def timeOutsideUK = claiming { implicit claim => implicit request =>
    claim.questionGroup(YourDetails.id) match {
      case Some(YourDetails(_, _, _, _, _, _, _, _, _, "yes")) => Redirect(routes.AboutYou.claimDate())
      case _ =>
        val completedQuestionGroups = claim.completedQuestionGroups(models.domain.AboutYou.id)

        val timeOutsideUKQGForm: Form[TimeOutsideUK] = claim.questionGroup(TimeOutsideUK.id) match {
          case Some(t: TimeOutsideUK) => timeOutsideUKForm.fill(t)
          case _ => timeOutsideUKForm
        }

        Ok(views.html.s2_aboutyou.g3_timeOutsideUK(timeOutsideUKQGForm, completedQuestionGroups.takeWhile(_.id != TimeOutsideUK.id)))
    }
  }

  def timeOutsideUKSubmit = claiming { implicit claim => implicit request =>
    def livingInUK(timeOutsideUKForm: Form[TimeOutsideUK])(implicit timeOutsideUK: TimeOutsideUK): Form[TimeOutsideUK] = {
      if (timeOutsideUK.currentlyLivingInUK == "no" && timeOutsideUK.arrivedInUK == None) timeOutsideUKForm.fill(timeOutsideUK).withError("arrivedInUK", "error.required")
      else timeOutsideUKForm
    }

    def planToGoBack(timeOutsideUKForm: Form[TimeOutsideUK])(implicit timeOutsideUK: TimeOutsideUK): Form[TimeOutsideUK] = {
      if (timeOutsideUK.planToGoBack.getOrElse("no") == "yes" && timeOutsideUK.whenPlanToGoBack == None) timeOutsideUKForm.fill(timeOutsideUK).withError("whenPlanToGoBack", "error.required")
      else timeOutsideUKForm
    }

    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.AboutYou.id)

    timeOutsideUKForm.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_aboutyou.g3_timeOutsideUK(formWithErrors, completedQuestionGroups.takeWhile(_.id != TimeOutsideUK.id))),
      implicit timeOutsideUK => {
        val formValidations = livingInUK _ andThen planToGoBack _
        val timeOutsideUKFormValidated = formValidations(timeOutsideUKForm)

        if (timeOutsideUKFormValidated.hasErrors) BadRequest(views.html.s2_aboutyou.g3_timeOutsideUK(timeOutsideUKFormValidated, completedQuestionGroups.takeWhile(_.id != TimeOutsideUK.id)))
        else claim.update(timeOutsideUK) -> Redirect(routes.AboutYou.claimDate())
      })
  }

  def claimDate = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.AboutYou.id)

    val claimDateQGForm: Form[ClaimDate] = claim.questionGroup(ClaimDate.id) match {
      case Some(c: ClaimDate) => claimDateForm.fill(c)
      case _ => claimDateForm
    }

    Ok(views.html.s2_aboutyou.g4_claimDate(claimDateQGForm, completedQuestionGroups.takeWhile(_.id != ClaimDate.id)))
  }

  def claimDateSubmit = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.AboutYou.id)

    claimDateForm.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_aboutyou.g4_claimDate(formWithErrors, completedQuestionGroups.takeWhile(_.id != ClaimDate.id))),
      claimDate => claim.update(claimDate) -> Redirect(routes.AboutYou.moreAboutYou()))
  }

  def moreAboutYou = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.AboutYou.id)

    val moreAboutYouQGForm: Form[MoreAboutYou] = claim.questionGroup(MoreAboutYou.id) match {
      case Some(m: MoreAboutYou) => moreAboutYouForm.fill(m)
      case _ => moreAboutYouForm
    }

    claim.questionGroup(models.domain.ClaimDate.id) match {
      case Some(n) => Ok(views.html.s2_aboutyou.g5_moreAboutYou(moreAboutYouQGForm, completedQuestionGroups.takeWhile(_.id != MoreAboutYou.id)))
      case _ => Redirect(routes.CarersAllowance.benefits())
    }
  }

  def moreAboutYouSubmit = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.AboutYou.id)

    moreAboutYouForm.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_aboutyou.g5_moreAboutYou(formWithErrors, completedQuestionGroups.takeWhile(_.id != MoreAboutYou.id))),
      moreAboutYou => claim.update(moreAboutYou) -> Redirect(routes.AboutYou.employment()))
  }

  def employment = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.AboutYou.id)

    val employmentQGForm: Form[Employment] = claim.questionGroup(Employment.id) match {
      case Some(e: Employment) => employmentForm.fill(e)
      case _ => employmentForm
    }

    claim.questionGroup(models.domain.ClaimDate.id) match {
      case Some(n) => Ok(views.html.s2_aboutyou.g6_employment(employmentQGForm, completedQuestionGroups.takeWhile(_.id != Employment.id)))
      case _ => Redirect(routes.CarersAllowance.benefits())
    }
  }

  def employmentSubmit = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.AboutYou.id)

    employmentForm.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_aboutyou.g6_employment(formWithErrors, completedQuestionGroups.takeWhile(_.id != Employment.id))),
      employment => claim.update(employment) -> Redirect(routes.AboutYou.propertyAndRent()))
  }

  def propertyAndRent = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.AboutYou.id)

    val propertyAndRentQGForm: Form[PropertyAndRent] = claim.questionGroup(PropertyAndRent.id) match {
      case Some(p: PropertyAndRent) => propertyAndRentForm.fill(p)
      case _ => propertyAndRentForm
    }

    claim.questionGroup(models.domain.ClaimDate.id) match {
      case Some(n) => Ok(views.html.s2_aboutyou.g7_propertyAndRent(propertyAndRentQGForm, completedQuestionGroups.takeWhile(_.id != PropertyAndRent.id)))
      case _ => Redirect(routes.CarersAllowance.benefits())
    }
  }

  def propertyAndRentSubmit = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.AboutYou.id)

    propertyAndRentForm.bindEncrypted.fold(
      formWithErrors => BadRequest(views.html.s2_aboutyou.g7_propertyAndRent(formWithErrors, completedQuestionGroups.takeWhile(_.id != PropertyAndRent.id))),
      propertyAndRent => claim.update(propertyAndRent) -> Redirect(routes.AboutYou.completed()))
  }

  def completed = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.AboutYou.id)

    Ok(views.html.s2_aboutyou.g8_completed(completedQuestionGroups))
  }

  def completedSubmit = claiming { implicit claim => implicit request =>
    val completedQuestionGroups = claim.completedQuestionGroups(models.domain.AboutYou.id)

    claim.questionGroup(YourDetails.id) match {
      case Some(YourDetails(_, _, _, _, _, _, _, _, _, "no")) if completedQuestionGroups.distinct.size == 7 =>
        Redirect(routes.YourPartner.yourPartner())

      case Some(YourDetails(_, _, _, _, _, _, _, _, _, _)) if completedQuestionGroups.distinct.size == 6 =>
        Redirect(routes.YourPartner.yourPartner())

      case _ => Redirect(routes.AboutYou.yourDetails())
    }
  }
}