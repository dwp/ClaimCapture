package controllers

import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.CachedClaim
import Mappings._
import play.api.data.validation.Constraints._
import models.domain._
import models.domain.BreakInCare
import scala.Some
import models.domain.{HasBreaks, BreakInCare, Break, BreaksInCare}
import models.Whereabouts._

object CareYouProvide extends Controller with CachedClaim {
  val route = Map(HasBreaks.id -> routes.CareYouProvide.hasBreaks)

  val theirPersonalDetailsForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "firstName" -> nonEmptyText(maxLength = maxNrOfChars),
      "middleName" -> optional(text(maxLength = maxNrOfChars)),
      "surname" -> nonEmptyText(maxLength = maxNrOfChars),
      "nationalInsuranceNumber" -> optional(nationalInsuranceNumber verifying validNationalInsuranceNumber),
      "dateOfBirth" -> Mappings.dayMonthYear.verifying(Mappings.validDate),
      "liveAtSameAddress" -> nonEmptyText
    )(TheirPersonalDetails.apply)(TheirPersonalDetails.unapply))


  val theirContactDetailsForm = Form(
    mapping(
      "address" -> address.verifying(requiredAddress),
      "postcode" -> optional(text verifying(pattern( """^(GIR 0AA)|((([A-Z][0-9][0-9]?)|(([A-Z][A-HJ-Y][0-9][0-9]?)|(([A-Z][0-9][A-Z])|([A-Z][A-HJ-Y][0-9]?[A-Z])))) [0-9][A-Z]{2})$""".r,
        "constraint.postcode", "error.postcode"), maxLength(10))),
      "phoneNumber" -> optional(text)
    )(TheirContactDetails.apply)(TheirContactDetails.unapply))


  val hasBreaksForm = Form(
    mapping(
      "answer" -> nonEmptyText
    )(HasBreaks.apply)(HasBreaks.unapply))

  val breakInCareForm = Form(
    mapping(
      "moreBreaks" -> nonEmptyText,
      "break" -> optional(mapping(
        "start" -> (dayMonthYear verifying validDate),
        "end"   -> optional(dayMonthYear verifying validDateOnly),
        "whereYou"    -> whereabouts.verifying(requiredWhereabouts),
        "wherePerson" -> whereabouts.verifying(requiredWhereabouts),
        "medicalDuringBreak" -> optional(text)
      )(Break.apply)(Break.unapply))
    )(BreakInCare.apply)(BreakInCare.unapply))

  def theirPersonalDetails = claiming {
    implicit claim => implicit request =>

      val theirPersonalDetailsQGForm: Form[TheirPersonalDetails] = claim.questionGroup(TheirPersonalDetails.id) match {
        case Some(t: TheirPersonalDetails) => theirPersonalDetailsForm.fill(t)
        case _ => theirPersonalDetailsForm
      }

      Ok(views.html.s4_careYouProvide.g1_theirPersonalDetails(theirPersonalDetailsQGForm))
  }

  def theirPersonalDetailsSubmit = claiming {
    implicit claim => implicit request =>
      theirPersonalDetailsForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s4_careYouProvide.g1_theirPersonalDetails(formWithErrors)),
        theirPersonalDetails => claim.update(theirPersonalDetails) -> Redirect(routes.CareYouProvide.theirContactDetails()))
  }

  def theirContactDetails = claiming {
    implicit claim => implicit request =>

      val liveAtSameAddress = claim.questionGroup(TheirPersonalDetails.id) match {
        case Some(t: TheirPersonalDetails) => if (t.liveAtSameAddress == "yes") true else false
        case _ => false

      }

      val theirContactDetailsPrePopulatedForm: Form[TheirContactDetails] = if (liveAtSameAddress) {
        claim.questionGroup(ContactDetails.id) match {
          case Some(cd: ContactDetails) => theirContactDetailsForm.fill(TheirContactDetails(address = cd.address, postcode = cd.postcode))
          case _ => theirContactDetailsForm
        }
      } else {
        claim.questionGroup(TheirContactDetails.id) match {
          case Some(t: TheirContactDetails) => theirContactDetailsForm.fill(t)
          case _ => theirContactDetailsForm
        }
      }

      Ok(views.html.s4_careYouProvide.g2_theirContactDetails(theirContactDetailsPrePopulatedForm))
  }

  def theirContactDetailsSubmit = claiming {
    implicit claim => implicit request =>

      theirContactDetailsForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s4_careYouProvide.g2_theirContactDetails(formWithErrors)),
        theirContactDetails => claim.update(theirContactDetails) -> Redirect(routes.CareYouProvide.moreAboutThePerson)
      )
  }

  def moreAboutThePerson = claiming {
    implicit claim => implicit request =>
      Ok(views.html.s4_careYouProvide.g3_moreAboutThePerson(theirContactDetailsForm))
  }

  def hasBreaks = claiming {
    implicit claim => implicit request =>
      val hasBreaksQGForm = claim.questionGroup(HasBreaks.id) match {
        case Some(h: HasBreaks) => hasBreaksForm.fill(h)
        case _ => hasBreaksForm
      }

      Ok(views.html.s4_careYouProvide.g10_hasBreaks(hasBreaksQGForm))
  }

  def hasBreaksSubmit = claiming {
    implicit claim => implicit request =>
      hasBreaksForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s4_careYouProvide.g10_hasBreaks(formWithErrors)),
        hasBreaks =>
          if (hasBreaks.answer == "yes") claim.update(hasBreaks) -> Redirect(routes.CareYouProvide.breaksInCare())
          else claim.update(hasBreaks) -> Redirect(routes.CareYouProvide.completed()))
  }

  def breaksInCare = claiming {
    implicit claim => implicit request =>
      val breaksInCare = claim.questionGroup(BreaksInCare.id) match {
        case Some(b: BreaksInCare) => b
        case _ => BreaksInCare()
      }

      Ok(views.html.s4_careYouProvide.g11_breaksInCare(breakInCareForm, breaksInCare))
  }

  def breaksInCareSubmit = claiming {
    implicit claim => implicit request =>
      val breaksInCare = claim.questionGroup(BreaksInCare.id) match {
        case Some(b: BreaksInCare) => b
        case _ => BreaksInCare()
      }

      breakInCareForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s4_careYouProvide.g11_breaksInCare(formWithErrors, breaksInCare)),
        breakInCare => {
          val updatedBreaksInCare = breakInCare.break.fold(breaksInCare)(break => if (breaksInCare.breaks.size == 10) breaksInCare else breaksInCare.update(break))

          breakInCare.moreBreaks match {
            case "no" => claim.update(updatedBreaksInCare) -> Redirect(routes.CareYouProvide.completed())
            case "yes" if updatedBreaksInCare.breaks.size == 10 => claim.update(updatedBreaksInCare) -> Redirect(routes.CareYouProvide.completed(/* TODO WARNING FEEDBACK MESSAGE*/))
            case _ => claim.update(updatedBreaksInCare) -> Redirect(routes.CareYouProvide.breaksInCare())
          }
        })
  }

  def completed = claiming {
    implicit claim => implicit request =>
      val outcome =
        <html>
          <head>
            <title>Completed - Care You Provide</title>
          </head>

          <body>
            <h1>End of Sprint 2</h1>

            <ul>
              {claim.completedQuestionGroups(models.domain.CareYouProvide.id).map(f => <li>
              {f}
            </li>)}
            </ul>
          </body>
        </html>

      Ok(outcome)
  }
}