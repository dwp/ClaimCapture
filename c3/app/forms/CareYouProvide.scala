package forms

import play.api.mvc.Controller
import play.api.data.Form
import play.api.data.Forms._
import models.view.CachedClaim
import controllers.Mappings._
import models.domain._
import models.domain.{ HasBreaks, BreaksInCare }
import scala.collection.immutable.ListMap
import scala.Some
import play.api.mvc.Call
import models.domain.BreakInCare
import models.domain.Break
import models.DayMonthYear

object CareYouProvide {

  val theirPersonalDetailsForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "firstName" -> nonEmptyText(maxLength = maxNrOfChars),
      "middleName" -> optional(text(maxLength = maxNrOfChars)),
      "surname" -> nonEmptyText(maxLength = maxNrOfChars),
      "nationalInsuranceNumber" -> optional(nino.verifying(validNino)),
      "dateOfBirth" -> dayMonthYear.verifying(validDate),
      "liveAtSameAddress" -> nonEmptyText
    )(TheirPersonalDetails.apply)(TheirPersonalDetails.unapply))

  val theirContactDetailsForm = Form(
    mapping(
      "address" -> address.verifying(requiredAddress),
      "postcode" -> optional(text verifying validPostcode),
      "phoneNumber" -> optional(text verifying validPhoneNumber)
    )(TheirContactDetails.apply)(TheirContactDetails.unapply))

  val moreAboutThePersonForm = Form(
    mapping(
      "relationship" -> nonEmptyText,
      "armedForcesPayment" -> optional(text),
      "claimedAllowanceBefore" -> nonEmptyText
    )(MoreAboutThePerson.apply)(MoreAboutThePerson.unapply))

  val previousCarerPersonalDetailsForm = Form(
    mapping(
      "firstName" -> optional(text(maxLength = maxNrOfChars)),
      "middleName" -> optional(text(maxLength = maxNrOfChars)),
      "surname" -> optional(text(maxLength = maxNrOfChars)),
      "nationalInsuranceNumber" -> optional(nino.verifying(validNino)),
      "dateOfBirth" -> optional(dayMonthYear.verifying(validDateOnly))
    )(PreviousCarerPersonalDetails.apply)(PreviousCarerPersonalDetails.unapply))

  val representativesForPersonForm = Form(
    mapping(
      "actForPerson" -> nonEmptyText,
      "actAs" -> optional(text),
      "someoneElseActForPerson" -> nonEmptyText,
      "someoneElseActAs" -> optional(text),
      "someoneElseFullName" -> optional(text)
    )(RepresentativesForPerson.apply)(RepresentativesForPerson.unapply))

  val oneWhoPaysPersonalDetailsFrom = Form(
    mapping(
      "organisation" -> optional(text(maxLength = hundred)),
      "title" -> optional(text),
      "firstName" -> optional(text(maxLength = maxNrOfChars)),
      "middleName" -> optional(text(maxLength = maxNrOfChars)),
      "surname" -> optional(text(maxLength = maxNrOfChars)),
      "amount" -> optional(text),
      "startDatePayment" -> optional(dayMonthYear.verifying(validDate))
    )(OneWhoPaysPersonalDetails.apply)(OneWhoPaysPersonalDetails.unapply))

  val moreAboutTheCareForm = Form(
    mapping(
      "spent35HoursCaring" -> nonEmptyText,
      "spent35HoursCaringBeforeClaim" -> nonEmptyText,
      "careStartDate" -> optional(dayMonthYear verifying validDateOnly),
      "hasSomeonePaidYou" -> nonEmptyText
    )(MoreAboutTheCare.apply)(MoreAboutTheCare.unapply)
  )
  
  val hasBreaksForm = Form(
    mapping(
      "answer" -> nonEmptyText
    )(HasBreaks.apply)(HasBreaks.unapply))

  val breakInCareForm = Form(
    mapping(
      "moreBreaks" -> nonEmptyText,
      "break" -> optional(mapping(
        "start" -> (dayMonthYear verifying validDate),
        "end" -> optional(dayMonthYear verifying validDateOnly),
        "whereYou" -> whereabouts.verifying(requiredWhereabouts),
        "wherePerson" -> whereabouts.verifying(requiredWhereabouts),
        "medicalDuringBreak" -> optional(text)
      )((start, end, whereYou, wherePerson, medicalDuringBreak) => Break(java.util.UUID.randomUUID.toString, start, end, whereYou, wherePerson, medicalDuringBreak))
        ((b: Break) => Some(b.start, b.end, b.whereYou, b.wherePerson, b.medicalDuringBreak)))
    )(BreakInCare.apply)(BreakInCare.unapply))

  val breakForm = Form(
    mapping(
      "breakID" -> nonEmptyText,
      "start" -> (dayMonthYear verifying validDate),
      "end" -> optional(dayMonthYear verifying validDateOnly),
      "whereYou" -> whereabouts.verifying(requiredWhereabouts),
      "wherePerson" -> whereabouts.verifying(requiredWhereabouts),
      "medicalDuringBreak" -> optional(text)
    )(Break.apply)(Break.unapply))

}