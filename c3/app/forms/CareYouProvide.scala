package forms

import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain._

object CareYouProvide {

  val theirPersonalDetailsForm = Form(
    mapping(
      "title" -> nonEmptyText,
      "firstName" -> nonEmptyText(maxLength = sixty),
      "middleName" -> optional(text(maxLength = sixty)),
      "surname" -> nonEmptyText(maxLength = sixty),
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
      "firstName" -> optional(text(maxLength = sixty)),
      "middleName" -> optional(text(maxLength = sixty)),
      "surname" -> optional(text(maxLength = sixty)),
      "nationalInsuranceNumber" -> optional(nino.verifying(validNino)),
      "dateOfBirth" -> optional(dayMonthYear.verifying(validDateOnly))
    )(PreviousCarerPersonalDetails.apply)(PreviousCarerPersonalDetails.unapply))

  val previousCarerContactDetailsForm = Form(
    mapping(
      "address" -> optional(address.verifying(requiredAddress)),
      "postcode" -> optional(text verifying validPostcode),
      "phoneNumber" -> optional(text verifying validPhoneNumber),
      "mobileNumber" -> optional(text verifying validPhoneNumber)
    )(PreviousCarerContactDetails.apply)(PreviousCarerContactDetails.unapply))

  val representativesForPersonForm = Form(
    mapping(
      "actForPerson" -> nonEmptyText,
      "actAs" -> optional(text),
      "someoneElseActForPerson" -> nonEmptyText,
      "someoneElseActAs" -> optional(text),
      "someoneElseFullName" -> optional(text)
    )(RepresentativesForPerson.apply)(RepresentativesForPerson.unapply))


  val moreAboutTheCareForm = Form(
    mapping(
      "spent35HoursCaring" -> nonEmptyText,
      "spent35HoursCaringBeforeClaim" -> nonEmptyText,
      "careStartDate" -> optional(dayMonthYear verifying validDateOnly),
      "hasSomeonePaidYou" -> nonEmptyText
    )(MoreAboutTheCare.apply)(MoreAboutTheCare.unapply))
}