package models

import controllers.Mappings._
import models.yesNo.{OptYesNoWithText, YesNoWithText, YesNoWithDate, YesNo}
import java.io.{FileOutputStream, OutputStreamWriter, BufferedWriter, File}
import io.ResourceUtil._
import scala.xml.{PrettyPrinter, Node}

package object domain {
  val claim = Claim() +
    Benefits(yes) +
    Hours(yes) +
    Over16(yes) +
    LivesInGB(yes) +
    YourDetails(title = "Mr", firstName = "Scooby", surname = "Doo", nationality = "Scottish", dateOfBirth = DayMonthYear(1, 1, 1980), alwaysLivedUK = yes, maritalStatus = "Married or civil partner", receiveStatePension = yes) +
    ContactDetails(address = MultiLineAddress(Street(Some("Scooby Street")))) +
    ClaimDate(dateOfClaim = DayMonthYear(1, 1, 2013)) +
    MoreAboutYou(hadPartnerSinceClaimDate = yes, beenInEducationSinceClaimDate = yes) +
    Employment(beenSelfEmployedSince1WeekBeforeClaim = yes, beenEmployedSince6MonthsBeforeClaim = yes) +
    YourPartnerPersonalDetails(title = "Mrs", firstName = "Daphne", surname = "Schoo", dateOfBirth = DayMonthYear(1, 1, 1980), separatedFromPartner = no) +
    PersonYouCareFor(isPartnerPersonYouCareFor = yes) +
    TheirPersonalDetails(title = "Mrs", firstName = "Daphne", surname = "Schoo", dateOfBirth = DayMonthYear(1, 1, 1980), liveAtSameAddressCareYouProvide = yes) +
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
    OtherEEAStateOrSwitzerland(benefitsFromEEA = no, workingForEEA = no) +
    AdditionalInfo(welshCommunication = no) +
    Consent(informationFromEmployer = OptYesNoWithText(answer = Some(yes)), informationFromPerson = YesNoWithText(answer = yes)) +
    Disclaimer(read = yes) +
    Declaration(read = yes)

  def writeXML(xml: String)(implicit file: File = new File("acceptance/models/domain/claim.xml")): String = {
    using(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))) { out =>
      out.write(xml)
    }

    xml
  }

  def prettyXML(node: Node)(implicit file: File = new File("acceptance/models/domain/claim.xml"), prettyPrinter: PrettyPrinter = new PrettyPrinter(180, 2)): String = {
    val encoding = "UTF-8"
    val prettyXML = prettyPrinter.format(node)

    using(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding))) { out =>
      out.write(s"""<?xml version="1.0" encoding="$encoding"?>\n$prettyXML""")
    }

    prettyXML
  }
}