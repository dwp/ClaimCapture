package controllers

import play.api.mvc.Controller
import play.api.data.Form
import play.api.mvc.Call
import play.api.data.Forms._
import models.view.CachedClaim
import Mappings._
import play.api.data.validation.Constraints._
import models.domain._
import scala.Some
import models.domain.{HasBreaks, BreakInCare, Break, BreaksInCare}
import scala.collection.immutable.ListMap

object CareYouProvide extends Controller with CachedClaim {
  val route: ListMap[String,Call] = ListMap(TheirPersonalDetails.id -> routes.CareYouProvide.theirPersonalDetails,
                                            TheirContactDetails.id -> routes.CareYouProvide.theirContactDetails,
                                            HasBreaks.id -> routes.CareYouProvide.hasBreaks,
                                            BreaksInCare.id -> routes.CareYouProvide.breaksInCare)

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
        "constraint.invalid", "error.postcode"), maxLength(10))),
      "phoneNumber" -> optional(text verifying pattern("""[0-9 \-]{1,20}""".r, "constraint.invalid", "error.invalid"))
    )(TheirContactDetails.apply)(TheirContactDetails.unapply))

  val hasBreaksForm = Form(
    mapping(
      "answer" -> nonEmptyText
    )(HasBreaks.apply)(HasBreaks.unapply))

  val breakInCareForm = Form(
    mapping(
      "moreBreaks" -> nonEmptyText,
      "break" -> optional(mapping(
        "breakID" -> ignored(java.util.UUID.randomUUID.toString),
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

      val completedForms = claim.completedQuestionGroups(models.domain.CareYouProvide.id)

      val liveAtSameAddress = claim.questionGroup(TheirPersonalDetails.id) match {
        case Some(t: TheirPersonalDetails) => t.liveAtSameAddress == yes
        case _ => false
      }

      val theirContactDetailsPrePopulatedForm = if (liveAtSameAddress) {
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

      Ok(views.html.s4_careYouProvide.g2_theirContactDetails(theirContactDetailsPrePopulatedForm, completedForms))
  }

  def theirContactDetailsSubmit = claiming {
    implicit claim => implicit request =>
      theirContactDetailsForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s4_careYouProvide.g2_theirContactDetails(formWithErrors, claim.completedQuestionGroups(models.domain.CareYouProvide.id))),
        theirContactDetails => claim.update(theirContactDetails) -> Redirect(routes.CareYouProvide.moreAboutThePerson())
      )
  }

  def moreAboutThePerson = claiming {
    implicit claim => implicit request =>
      Ok(views.html.s4_careYouProvide.g3_moreAboutThePerson(theirContactDetailsForm))
  }

  def hasBreaks = claiming {
    implicit claim => implicit request =>
      val completedQuestionGroups = claim.completedQuestionGroups(models.domain.AboutYou.id).takeWhile(q => q.id != HasBreaks.id)

      val hasBreaksQGForm = claim.questionGroup(HasBreaks.id) match {
        case Some(h: HasBreaks) => hasBreaksForm.fill(h)
        case _ => hasBreaksForm
      }

      Ok(views.html.s4_careYouProvide.g10_hasBreaks(hasBreaksQGForm, completedQuestionGroups))
  }

  def hasBreaksSubmit = claiming {
    implicit claim => implicit request =>
      val completedQuestionGroups = claim.completedQuestionGroups(models.domain.AboutYou.id).takeWhile(q => q.id != HasBreaks.id)

      hasBreaksForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.s4_careYouProvide.g10_hasBreaks(formWithErrors, completedQuestionGroups)),
        hasBreaks =>
          if (hasBreaks.answer == yes) claim.update(hasBreaks) -> Redirect(routes.CareYouProvide.breaksInCare())
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

  def deleteBreak(id: String) = claiming {
    implicit claim => implicit request =>
      import play.api.libs.json.Json

      claim.questionGroup(BreaksInCare.id) match {
        case Some(b: BreaksInCare) => claim.update(b.delete(id)) -> Ok(Json.obj("id" -> id))
        case _ => BadRequest(s"""Failed to delete break with ID "$id" as claim currently has no breaks""")
      }
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
              {claim.completedQuestionGroups(models.domain.CareYouProvide.id).map(f => <li>{f}</li>)}
            </ul>
          </body>
        </html>

      Ok(outcome)
  }
}