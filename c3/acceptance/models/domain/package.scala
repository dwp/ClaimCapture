package models

import controllers.Mappings._
import models.yesNo.{YesNoWith2Text, YesNoWithText, YesNoWithDate}
import java.io.{FileOutputStream, OutputStreamWriter, BufferedWriter, File}
import io.ResourceUtil._
import models.yesNo.YesNo
import scala.xml.{PrettyPrinter, Node}

package object domain {
  val claim = Claim()
    .update(Benefits(yes))
    .update(Hours(yes))
    .update(Over16(yes))
    .update(LivesInGB(yes))
    .update(YourDetails(title = "mr", firstName = "Scooby", surname = "Doo", nationality = "Scottish", dateOfBirth = DayMonthYear(1, 1, 1980), alwaysLivedUK = yes, maritalStatus = "m"))
    .update(ContactDetails(address = MultiLineAddress(lineOne = Some("Scooby Street"))))
    .update(ClaimDate(dateOfClaim = DayMonthYear(1, 1, 2013)))
    .update(MoreAboutYou(hadPartnerSinceClaimDate = yes, beenInEducationSinceClaimDate = yes, receiveStatePension = yes))
    .update(Employment(beenSelfEmployedSince1WeekBeforeClaim = yes, beenEmployedSince6MonthsBeforeClaim = yes))
    .update(YourPartnerPersonalDetails(title = "Mrs", firstName = "Daphne", surname = "Schoo", dateOfBirth = DayMonthYear(1, 1, 1980), separatedFromPartner = no))
    .update(PersonYouCareFor(isPartnerPersonYouCareFor = yes))
    .update(TheirPersonalDetails(title = "mrs", firstName = "Daphne", surname = "Schoo", dateOfBirth = DayMonthYear(1, 1, 1980), liveAtSameAddressCareYouProvide = yes))
    .update(TheirContactDetails(address = MultiLineAddress(lineOne = Some("Scooby Street"))))
    .update(MoreAboutThePerson(relationship = "wife"))
    .update(MoreAboutTheCare(spent35HoursCaring = yes, spent35HoursCaringBeforeClaim = YesNoWithDate(answer = yes, date = Some(DayMonthYear(1, 1, 2000)))))
    .update(BreaksInCare(Break(start = DayMonthYear(1, 1, 2000), whereYou = Whereabouts(location = "Home"), wherePerson = Whereabouts(location = "Hospital"), medicalDuringBreak = no) :: Nil))
    .update(NormalResidenceAndCurrentLocation(whereDoYouLive = YesNoWithText(answer = yes), inGBNow = yes))
    .update(Trips().update(Trip(id = "trip1", start = DayMonthYear(1, 1, 2010), end = DayMonthYear(1, 2, 2010), where = "Scotland", why = "Get a suntan").as[FourWeeksTrip])
    .update(Trip(id = "trip2", start = DayMonthYear(1, 2, 2010), end = DayMonthYear(1, 2, 2011), where = "Scotland", why = "Get a suntan").as[FiftyTwoWeeksTrip]))
    .update(YourCourseDetails(title = Some("Hacking")))
    .update(AddressOfSchoolCollegeOrUniversity(nameOfSchoolCollegeOrUniversity = Some("Hackerversity")))
    .update(JobDetails(employerName = "Hackers R Us", finishedThisJob = no))
    .update(EmployerContactDetails())
    .update(LastWage())
    .update(AdditionalWageDetails(employerOwesYouMoney = no))
    .update(PensionSchemes(payOccupationalPensionScheme = no, payPersonalPensionScheme = no))
    .update(AboutExpenses(payForAnythingNecessary = no, payAnyoneToLookAfterChildren = no, payAnyoneToLookAfterPerson = no))
    .update(AboutSelfEmployment(areYouSelfEmployedNow = no))
    .update(SelfEmploymentYourAccounts())
    .update(SelfEmploymentPensionsAndExpenses(pensionSchemeMapping = YesNoWith2Text(answer = no), doYouPayToLookAfterYourChildren = no, didYouPayToLookAfterThePersonYouCaredFor = no))
    .update(AboutOtherMoney(yourBenefits = YesNo(answer = no), anyPaymentsSinceClaimDate = YesNo(answer = no)))
    .update(StatutorySickPay(haveYouHadAnyStatutorySickPay = no))
    .update(OtherStatutoryPay(otherPay = no))
    .update(OtherEEAStateOrSwitzerland(benefitsFromOtherEEAStateOrSwitzerland = no, workingForOtherEEAStateOrSwitzerland = no))
    .update(AdditionalInfo(welshCommunication = no))
    .update(Consent(informationFromEmployer = YesNoWithText(answer = yes), informationFromPerson = YesNoWithText(answer = yes)))
    .update(Disclaimer(read = yes))
    .update(Declaration(read = yes))

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