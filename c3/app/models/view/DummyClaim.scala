package models.view

import models.domain._
import models._
import models.yesNo.{YesNoWithDropDownAndText, YesNoWithDropDown, YesNoWithDate}
import models.SortCode
import models.NationalInsuranceNumber
import scala.Some
import models.Whereabouts
import models.MultiLineAddress
import models.domain.Break

/**
 * Delete me before we go live
 * Use dto help submission testing
 */
object DummyClaim {

  def updateClaim(claim: Claim) = {
    claim.update(yourPartner.yourPartnerPersonalDetails)
      .update(yourPartner.yourPartnerContactDetails)
      .update(yourPartner.moreAboutYourPartner)
      .update(yourPartner.personYouCareFor.get)

      .update(careYouProvide.theirPersonalDetails)
      .update(careYouProvide.theirContactDetails)
      .update(careYouProvide.moreAboutThePerson)
      .update(careYouProvide.representatives)
      .update(careYouProvide.previousCarerContactDetails.get)
      .update(careYouProvide.previousCarerPersonalDetails.get)
      .update(careYouProvide.moreAboutTheCare)
      .update(careYouProvide.oneWhoPays.get)
      .update(careYouProvide.contactDetailsPayingPerson.get)
      .update(careYouProvide.breaksInCare)

      .update(education.yourCourseDetails)
      .update(education.addressOfSchool)

      .update(payDetails.howWePayYou)
      .update(payDetails.bankBuildingSocietyDetails)

      .update(consentAndDeclaration.additionalInfo)
      .update(consentAndDeclaration.consent)
      .update(consentAndDeclaration.disclaimer)
      .update(consentAndDeclaration.declaration)
  }

  val yourDetails = YourDetails(NoRouting, title = "mr", firstName = "TEST", middleName = None, surname = "TEST",
    otherSurnames = Some("TEST"), None, nationality = "French",
    dateOfBirth = DayMonthYear(1, 1, 1963), maritalStatus = "m", alwaysLivedUK = "yes")

  val contactDetails = ContactDetails(NoRouting, address = MultiLineAddress(Some("TEST HOUSE"), None, None),
    postcode = Some("PR2 8AE"),
    phoneNumber = Some("01772 700806"), None)

  val timeOutsideUK = TimeOutsideUK(NoRouting, livingInUK = LivingInUK("yes", Some(DayMonthYear()), Some(""), Some(YesNoWithDate("yes", Some(DayMonthYear())))), visaReference = None)

  val claimDate = ClaimDate(NoRouting, dateOfClaim = DayMonthYear(1, 1, 2013))

  val aboutYou = AboutYou(yourDetails, contactDetails, Some(timeOutsideUK), claimDate)

  val theirPersonalDetails = TheirPersonalDetails(NoRouting, title = "ms", firstName = "TEST", middleName = None, surname = "TEST",
    None, dateOfBirth = DayMonthYear(1, 1, 1963), liveAtSameAddress = "no")

  val theirContactDetails = TheirContactDetails(NoRouting, address = MultiLineAddress(Some("Line1"), None, None), postcode = Some("PR2 8AE"))

  val moreAboutThePerson = MoreAboutThePerson(NoRouting, relationship = "mother", None, claimedAllowanceBefore = "no")

  val representatives = RepresentativesForPerson(NoRouting, youAct = YesNoWithDropDown("yes", Some("Lawyer")), someoneElseAct = YesNoWithDropDownAndText("yes",
    Some("Lawyer"), Some("Mr. Lawyer")))

  val previousCarerContactDetails = PreviousCarerContactDetails(NoRouting, address = Some(MultiLineAddress(Some("Line1"), None, None)), postcode = Some("PR2 8AE"))

  val previousCarerPersonalDetails = PreviousCarerPersonalDetails(NoRouting, firstName = Some("Some"), middleName = None, surname = Some("One"),
    None, dateOfBirth = Some(DayMonthYear(1, 1, 1963)))

  val moreAboutTheCare = MoreAboutTheCare(NoRouting, spent35HoursCaring = "yes", spent35HoursCaringBeforeClaim = YesNoWithDate("no", Some(DayMonthYear(1, 1, 2013))), hasSomeonePaidYou = "yes")

  val oneWhoPays = OneWhoPaysPersonalDetails(NoRouting, organisation = Some("SomeOrg Inc."), amount = Some("300"), startDatePayment = Some(DayMonthYear(1, 1, 2012)))

  val contactDetailsPayingPerson = ContactDetailsOfPayingPerson(NoRouting, address = Some(MultiLineAddress(Some("Line1"), None, None)), postcode = Some("PR2 8AE"))

  val breaksInCare = BreaksInCare(NoRouting,
    List(
      Break(id = "1", start = DayMonthYear(1, 1, 2001), end = Some(DayMonthYear(1, 5, 2001)), whereYou = Whereabouts("Holiday", None), wherePerson = Whereabouts("Hospital", None), medicalDuringBreak = "yes"),
      Break(id = "1", start = DayMonthYear(1, 1, 2002), end = Some(DayMonthYear(1, 5, 2002)), whereYou = Whereabouts("Holiday", None), wherePerson = Whereabouts("Hospital", None), medicalDuringBreak = "yes")
    )
  )

  case class EducationSection(yourCourseDetails:YourCourseDetails, addressOfSchool: AddressOfSchoolCollegeOrUniversity)

  val education = EducationSection(YourCourseDetails(NoRouting, Some("courseType"), Some("courseTitle"),  Some(DayMonthYear(Some(1), Some(1), Some(2001))),Some(DayMonthYear(Some(2), Some(2), Some(2002))),  Some(DayMonthYear(Some(3), Some(3), Some(2003))), Some("ST11")),
    AddressOfSchoolCollegeOrUniversity(NoRouting, Some("schoolName"), Some("tutorName"), Some(MultiLineAddress(Some("line1"), Some("line2"), Some("line3"))), Some("SE1 6EH"), Some("020192827273"), Some("0302928273"))
  )

  val careYouProvide = CareYouProvide(theirPersonalDetails, theirContactDetails,
    moreAboutThePerson, representatives,
    Some(previousCarerContactDetails), Some(previousCarerPersonalDetails),
    moreAboutTheCare, Some(oneWhoPays), Some(contactDetailsPayingPerson), breaksInCare)

  val yourPartnerPersonalDetails = YourPartnerPersonalDetails(NoRouting, title = "mr", firstName = "Michael", middleName = None, surname = "Mouse", otherNames = Some("Oswald"), nationalInsuranceNumber = Some(NationalInsuranceNumber(Some("AA"), Some("12"), Some("34"), Some("56"), Some("A"))), dateOfBirth = DayMonthYear(1, 1, 1930), nationality = Some("British"), liveAtSameAddress = "yes")
  val yourPartnerContactDetails = YourPartnerContactDetails(NoRouting, address = Some(MultiLineAddress(Some("Line1"), None, None)), postcode = Some("PR2 8AE"))
  val moreAboutYourPartner = MoreAboutYourPartner(NoRouting, startedLivingTogether = Some(YesNoWithDate("yes", Some(DayMonthYear(1, 1, 1940)))), separated = YesNoWithDate("no",None))
  val personYouCareFor = PersonYouCareFor(NoRouting, isPartnerPersonYouCareFor = "yes")
  val yourPartner = YourPartner(yourPartnerPersonalDetails, yourPartnerContactDetails, moreAboutYourPartner, Some(personYouCareFor))

  val howWePayYou = HowWePayYou(NoRouting, "01","everyWeek")
  val bank = BankBuildingSocietyDetails(NoRouting, "Holder","Bank name",SortCode("12","34","56"),"1234567890","1234")

  val payDetails = PayDetails(howWePayYou,bank)

  val additionalInfo = AdditionalInfo(NoRouting, Some("Other information"),"yes")
  val consent = Consent(NoRouting, "no",Some("I don't want to"),"no",Some("I said I don't want to"))
  val disclaimer = Disclaimer(NoRouting, "checked")
  val declaration = Declaration(NoRouting, "checked",Some("checked"))

  val consentAndDeclaration = ConsentAndDeclaration(additionalInfo,consent,disclaimer,declaration)
}

