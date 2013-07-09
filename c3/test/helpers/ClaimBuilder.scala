package helpers

import models.domain._
import models.{Whereabouts, DayMonthYear, MultiLineAddress}
import scala.Some
import models.NationalInsuranceNumber

object ClaimBuilder {
  val yourDetails = YourDetails(title = "mr", firstName = "Phil", middleName = None, surname = "Smith",
    otherSurnames = Some("O'Dwyer"), None, nationality = "French",
    dateOfBirth = DayMonthYear(1, 1, 1963), maritalStatus = "m", alwaysLivedUK = "yes")

  val contactDetails = ContactDetails(address = MultiLineAddress(Some("Line1"), None, None),
    postcode = Some("PR2 8AE"),
    phoneNumber = Some("01772 700806"), None)

  val timeOutsideUK = TimeOutsideUK(currentlyLivingInUK = "no", arrivedInUK = Some(DayMonthYear()),
    originCountry = None, planToGoBack = Some("yes"), whenPlanToGoBack = None,
    visaReference = None)

  val claimDate = ClaimDate(dateOfClaim = DayMonthYear(1, 1, 2013))

  val aboutYou = AboutYou(yourDetails, contactDetails, Some(timeOutsideUK), claimDate)

  val theirPersonalDetails = TheirPersonalDetails(title = "ms",firstName = "Minnie", middleName = None, surname = "Mouse",
    None, dateOfBirth = DayMonthYear(1, 1, 1963),liveAtSameAddress = "no")

  val theirContactDetails = TheirContactDetails(address = MultiLineAddress(Some("Line1"),None,None),postcode = Some("PR2 8AE"))

  val moreAboutThePerson = MoreAboutThePerson(relationship = "mother",None,claimedAllowanceBefore = "no")

  val representatives = RepresentativesForPerson(actForPerson = "yes",actAs = Some("Lawyer"),someoneElseActForPerson = "yes",
                                                someoneElseActAs = Some("Lawyer"),someoneElseFullName = Some("Mr. Lawyer"))

  val previousCarerContactDetails = PreviousCarerContactDetails(address = Some(MultiLineAddress(Some("Line1"),None,None)),postcode = Some("PR2 8AE"))

  val previousCarerPersonalDetails = PreviousCarerPersonalDetails(firstName = Some("Some"), middleName = None, surname = Some("One"),
    None, dateOfBirth = Some(DayMonthYear(1, 1, 1963)))

  val moreAboutTheCare = MoreAboutTheCare(spent35HoursCaring = "yes",spent35HoursCaringBeforeClaim = "no",careStartDate = Some(DayMonthYear(1, 1, 2013)),hasSomeonePaidYou = "yes")

  val oneWhoPays = OneWhoPaysPersonalDetails(organisation = Some("SomeOrg Inc."),amount = Some("300"),startDatePayment = Some(DayMonthYear(1, 1, 2012)))

  val contactDetailsPayingPerson = ContactDetailsOfPayingPerson(address = Some(MultiLineAddress(Some("Line1"),None,None)),postcode = Some("PR2 8AE"))

  val breaksInCare = BreaksInCare(
                        List(
                          Break(id = "1",start = DayMonthYear(1, 1, 2001),end = Some(DayMonthYear(1, 5, 2001)),whereYou = Whereabouts("Holiday",None),wherePerson = Whereabouts("Hospital",None),medicalDuringBreak = "yes"),
                          Break(id = "1",start = DayMonthYear(1, 1, 2002),end = Some(DayMonthYear(1, 5, 2002)),whereYou = Whereabouts("Holiday",None),wherePerson = Whereabouts("Hospital",None),medicalDuringBreak = "yes")
                        )
                        )


  val careYouProvide = CareYouProvide(theirPersonalDetails,theirContactDetails,
    moreAboutThePerson,representatives,
    Some(previousCarerContactDetails), Some(previousCarerPersonalDetails),
    moreAboutTheCare,Some(oneWhoPays),Some(contactDetailsPayingPerson),breaksInCare)
    
  val yourPartnerPersonalDetails = YourPartnerPersonalDetails(title = "mrs", firstName = "Minnie", middleName = None, surname = "Mouse", otherNames = Some("Oswald"), nationalInsuranceNumber= Some(NationalInsuranceNumber(Some("AB"),Some("00"),Some("00"),Some("00"),Some("B"))), dateOfBirth = DayMonthYear(3, 3, 1956), nationality = Some("British"), liveAtSameAddress = "yes")
  val yourPartnerContactDetails = YourPartnerContactDetails(address = Some(MultiLineAddress(Some("10"),Some("Anyplace Street"),None)),postcode = Some("PR2 8AE"))
  val moreAboutYourPartner = MoreAboutYourPartner(dateStartedLivingTogether = Some(DayMonthYear(1,1,1960)), separatedFromPartner = "no", separationDate = None)
  val personYouCareFor = PersonYouCareFor(isPartnerPersonYouCareFor = "yes")
  val yourPartner = YourPartner(yourPartnerPersonalDetails, yourPartnerContactDetails,
                          moreAboutYourPartner, Some(personYouCareFor))
}
