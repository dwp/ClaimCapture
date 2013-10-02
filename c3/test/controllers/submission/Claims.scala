package controllers.submission

import controllers.Mappings._
import models.yesNo.{OptYesNoWithText, YesNoWithText, YesNoWithDate, YesNo}
import models.domain._
import models._
import models.Street
import models.MultiLineAddress
import models.domain.Claim
import models.Whereabouts
import models.domain.Trip
import models.domain.Break

object Claims {
  val completedClaim = Claim() +
    Benefits(yes) +
    Hours(yes) +
    Over16(yes) +
    LivesInGB(yes) +
    YourDetails(title = "mr", firstName = "Scooby", surname = "Doo", nationality = "Scottish", dateOfBirth = DayMonthYear(1, 1, 1980), alwaysLivedUK = yes, maritalStatus = "m") +
    ContactDetails(address = MultiLineAddress(Street(Some("Scooby Street")))) +
    ClaimDate(dateOfClaim = DayMonthYear(1, 1, 2013)) +
    MoreAboutYou(hadPartnerSinceClaimDate = yes, beenInEducationSinceClaimDate = yes, receiveStatePension = yes) +
    Employment(beenSelfEmployedSince1WeekBeforeClaim = yes, beenEmployedSince6MonthsBeforeClaim = yes) +
    YourPartnerPersonalDetails(title = "Mrs", firstName = "Daphne", surname = "Schoo", dateOfBirth = DayMonthYear(1, 1, 1980), separatedFromPartner = no) +
    PersonYouCareFor(isPartnerPersonYouCareFor = yes) +
    TheirPersonalDetails(title = "mrs", firstName = "Daphne", surname = "Schoo", dateOfBirth = DayMonthYear(1, 1, 1980), liveAtSameAddressCareYouProvide = yes) +
    TheirContactDetails(address = MultiLineAddress(Street(Some("Scooby Street")))) +
    MoreAboutThePerson(relationship = "wife") +
    MoreAboutTheCare(spent35HoursCaring = yes, spent35HoursCaringBeforeClaim = YesNoWithDate(answer = yes, date = Some(DayMonthYear(1, 1, 2000)))) +
    BreaksInCare(Break(start = DayMonthYear(1, 1, 2000), whereYou = Whereabouts(location = "At Home"), wherePerson = Whereabouts(location = "Hospital"), medicalDuringBreak = no) :: Nil) +
    NormalResidenceAndCurrentLocation(whereDoYouLive = YesNoWithText(answer = yes), inGBNow = yes) +
    (Trips() + Trip(id = "trip1", start = DayMonthYear(1, 1, 2010), end = DayMonthYear(1, 2, 2010), where = "Scotland", why = "Get a suntan").as[FourWeeksTrip]
      + Trip(id = "trip2", start = DayMonthYear(1, 2, 2010), end = DayMonthYear(1, 2, 2011), where = "Scotland", why = "Get a suntan").as[FiftyTwoWeeksTrip]) +
    YourCourseDetails(title = Some("Hacking")) +
    AddressOfSchoolCollegeOrUniversity(nameOfSchoolCollegeOrUniversity = Some("Hackerversity")) +
    JobDetails(employerName = "Hackers R Us", finishedThisJob = no) +
    EmployerContactDetails() +
    LastWage() +
    AdditionalWageDetails(employerOwesYouMoney = no) +
    PensionSchemes(payOccupationalPensionScheme = no, payPersonalPensionScheme = no) +
    AboutExpenses(payForAnythingNecessary = no, payAnyoneToLookAfterChildren = no, payAnyoneToLookAfterPerson = no) +
    AboutSelfEmployment(areYouSelfEmployedNow = no) +
    SelfEmploymentYourAccounts() +
    SelfEmploymentPensionsAndExpenses(doYouPayToPensionScheme = no, doYouPayToLookAfterYourChildren = no, didYouPayToLookAfterThePersonYouCaredFor = no) +
    AboutOtherMoney(yourBenefits = YesNo(answer = no), anyPaymentsSinceClaimDate = YesNo(answer = no)) +
    StatutorySickPay(haveYouHadAnyStatutorySickPay = no) +
    OtherStatutoryPay(otherPay = no) +
    OtherEEAStateOrSwitzerland(benefitsFromOtherEEAStateOrSwitzerland = no, workingForOtherEEAStateOrSwitzerland = no) +
    AdditionalInfo(welshCommunication = no) +
    Consent(informationFromEmployer = OptYesNoWithText(answer = Some(yes)), informationFromPerson = YesNoWithText(answer = yes)) +
    Disclaimer(read = yes) +
    Declaration(read = yes)
}