package helpers

import models.domain._
import models._
import models.yesNo._
import controllers.Mappings.{yes, no}


import models.PaymentFrequency
import scala.Some
import models.MultiLineAddress
import models.SortCode
import models.NationalInsuranceNumber
import models.Whereabouts
import models.domain.Trip
import models.domain.Break

case class AboutYouSection(yourDetails: YourDetails,
                           contactDetails: ContactDetails,
                           timeOutsideUK: Option[TimeOutsideUK],
                           claimDate: ClaimDate,
                           employment: Employment)

case class EducationSection(yourCourseDetails: YourCourseDetails, addressOfSchool: AddressOfSchoolCollegeOrUniversity)

case class OtherMoneySection(aboutOtherMoney: AboutOtherMoney,
                             moneyPaidToSomeoneElse: MoneyPaidToSomeoneElseForYou,
                             personWhoGetsThisMoney: PersonWhoGetsThisMoney,
                             personContactDetails: PersonContactDetails,
                             statutorySickPay: StatutorySickPay,
                             otherStatutoryPay: OtherStatutoryPay)

case class TimeSpentAbroadSection(normalResidence: NormalResidenceAndCurrentLocation, trips: Trips)

case class SelfEmploymentSection(aboutSelfEmployment: AboutSelfEmployment,
                                 selfEmploymentYourAccounts: SelfEmploymentYourAccounts,
                                 accountantContactDetails: SelfEmploymentAccountantContactDetails,
                                 pensionsAndExpenses: SelfEmploymentPensionsAndExpenses,
                                 childcareExpenses: ChildcareExpensesWhileAtWork,
                                 expensesWhileAtWork: ExpensesWhileAtWork)

object ClaimBuilder {
  val yourDetails = YourDetails(title = "mr", firstName = "Phil", middleName = None, surname = "Smith",
    otherSurnames = Some("O'Dwyer"), None, nationality = "French",
    dateOfBirth = DayMonthYear(1, 1, 1963), maritalStatus = "m", alwaysLivedUK = "yes")

  val contactDetails = ContactDetails(address = MultiLineAddress(Some("Line1"), None, None),
    postcode = Some("PR2 8AE"),
    phoneNumber = Some("01772 700806"), None)

  val timeOutsideUK = TimeOutsideUK(livingInUK = LivingInUK("yes", Some(DayMonthYear()), Some(""), Some(YesNoWithDate("yes", Some(DayMonthYear())))), visaReference = None)

  val claimDate = ClaimDate(dateOfClaim = DayMonthYear(1, 1, 2013))
  val employment = Employment(beenSelfEmployedSince1WeekBeforeClaim = yes, beenEmployedSince6MonthsBeforeClaim = yes)

  val aboutYou = AboutYouSection(yourDetails, contactDetails, Some(timeOutsideUK), claimDate, employment)

  val theirPersonalDetails = TheirPersonalDetails(NoRouting, title = "ms", firstName = "Minnie", middleName = None, surname = "Mouse",
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

  val employmentJobs = new Jobs(List(
     new Job("1",List(
        JobDetails("1","KFC",Some(DayMonthYear()),"yes",Some(DayMonthYear(1,1,2001)),Some("70"),Some("Chicken feeder"),None),
        EmployerContactDetails("1",None,None,None),
        LastWage("1",None,None,None,None,None),
        AdditionalWageDetails("1",Some(PaymentFrequency("every 4 years",None)),Some("29th of February"),None,"no",None,"yes"),
        MoneyOwedbyEmployer("1",None,None,None,None,None),
        PensionSchemes("1","no",None,None,"no",None,None),
        AboutExpenses("1","yes","yes","yes"),
        NecessaryExpenses("1","dunno","100000","cause i do"),
        ChildcareExpenses("1",None,"dunno",None,None,None),
        ChildcareProvider("1",None,None),
        PersonYouCareForExpenses("1",None,"dunno",None),
        CareProvider("1",None,None)
     ))
    ,
    new Job("2",List(
      JobDetails("2","Valtech",Some(DayMonthYear()),"yes",Some(DayMonthYear(1,2,2001)),Some("10"),Some("Janitor"),None),
      EmployerContactDetails("2",None,None,None),
      LastWage("2",None,None,None,None,None),
      AdditionalWageDetails("2",Some(PaymentFrequency("every 4 years",None)),Some("29th of February"),None,"no",None,"yes"),
      MoneyOwedbyEmployer("2",None,None,None,None,None),
      PensionSchemes("2","no",None,None,"no",None,None),
      AboutExpenses("2","yes","yes","yes"),
      NecessaryExpenses("2","dunno","100000","cause i do"),
      ChildcareExpenses("2",None,"dunno",None,None,None),
      ChildcareProvider("2",None,None),
      PersonYouCareForExpenses("2",None,"dunno",None),
      CareProvider("2",None,None)
    ))
  ))

  val normalResidence = NormalResidenceAndCurrentLocation(whereDoYouLive = YesNoWithText(answer = yes, text = Some("UK")), inGBNow = yes)
  val fourWeekTrip = Trip(id = "four-one", DayMonthYear(Some(1), Some(2), Some(2011)), DayMonthYear(Some(1), Some(3), Some(2011)), "Netherlands", Some("Holiday"))
  val fiftyTwoWeeksTrip = Trip(id = "fiftyTwo-one", DayMonthYear(Some(1), Some(2), Some(2012)), DayMonthYear(Some(1), Some(2), Some(2013)), "Spain", Some("Family"))
  val trips = Trips(fourWeeksTrips = List(fourWeekTrip), fiftyTwoWeeksTrips = List(fiftyTwoWeeksTrip))

  val timeSpentAbroad = TimeSpentAbroadSection(normalResidence, trips)

  val education = EducationSection(YourCourseDetails(NoRouting, Some("courseType"), Some("courseTitle"), Some(DayMonthYear(Some(1), Some(1), Some(2001))), Some(DayMonthYear(Some(2), Some(2), Some(2002))), Some(DayMonthYear(Some(3), Some(3), Some(2003))), Some("ST11")),
    AddressOfSchoolCollegeOrUniversity(NoRouting, Some("schoolName"), Some("tutorName"), Some(MultiLineAddress(Some("line1"), Some("line2"), Some("line3"))), Some("SE1 6EH"), Some("020192827273"), Some("0302928273"))
  )

  val otherMoney = OtherMoneySection(AboutOtherMoney(YesNoWithText(yes, Some("text"))), MoneyPaidToSomeoneElseForYou("yes"),
    PersonWhoGetsThisMoney("fullName", Some(NationalInsuranceNumber(Some("VO"), Some("12"), Some("34"), Some("56"), Some("D"))), "benefitName"),
    PersonContactDetails(Some(MultiLineAddress(Some("line1"), Some("line2"), Some("line3"))), Some("SE1 6EH")),
    StatutorySickPay(haveYouHadAnyStatutorySickPay = yes, employersName = Some("employersName"), employersAddress = Some(MultiLineAddress(Some("line1"), Some("line2"), Some("line3"))), employersPostcode = Some("SE1 6EH")),
    OtherStatutoryPay(otherPay = yes, employersName = Some("employersName"), employersAddress = Some(MultiLineAddress(Some("line1"), Some("line2"), Some("line3"))), employersPostcode = Some("SE1 6EH"))
  )

  val selfEmployment = SelfEmploymentSection(AboutSelfEmployment(areYouSelfEmployedNow = yes, whenDidYouStartThisJob = Some(DayMonthYear(Some(1), Some(2), Some(2013))), whenDidTheJobFinish = Some(DayMonthYear(Some(1), Some(9), Some(2013))), natureOfYourBusiness = Some("IT"), haveYouCeasedTrading = Some(no)),
    SelfEmploymentYourAccounts(doYouHaveAnAccountant = Some(yes)),
    SelfEmploymentAccountantContactDetails(accountantsName = "KPMG", address = MultiLineAddress(Some("line1"), Some("line2"), Some("line3"))),
    SelfEmploymentPensionsAndExpenses(pensionSchemeMapping = YesNoWithText(yes, Some("150.5")), lookAfterChildrenMapping = YesNoWithText(yes, Some("150")), lookAfterCaredForMapping = YesNoWithText(yes, Some("150.5"))),
    ChildcareExpensesWhileAtWork(howMuchYouPay = Some("150.5"), nameOfPerson = "Andy", whatRelationIsToYou = Some("grandSon"), whatRelationIsTothePersonYouCareFor = Some("relation")),
    ExpensesWhileAtWork(howMuchYouPay = Some("200.5"), nameOfPerson = "NameOfPerson", whatRelationIsToYou = Some("grandSon"), whatRelationIsTothePersonYouCareFor = Some("grandSon"))
  )

  val careYouProvide = CareYouProvide(theirPersonalDetails, theirContactDetails,
    moreAboutThePerson, representatives,
    Some(previousCarerContactDetails), Some(previousCarerPersonalDetails),
    moreAboutTheCare, Some(oneWhoPays), Some(contactDetailsPayingPerson), breaksInCare)

  val yourPartnerPersonalDetails = YourPartnerPersonalDetails(title = "mr", firstName = "Michael", middleName = None, surname = "Mouse", otherNames = Some("Oswald"), nationalInsuranceNumber = Some(NationalInsuranceNumber(Some("AA"), Some("12"), Some("34"), Some("56"), Some("A"))), dateOfBirth = DayMonthYear(1, 1, 1930), nationality = Some("British"), liveAtSameAddress = "yes")
  val yourPartnerContactDetails = YourPartnerContactDetails(address = Some(MultiLineAddress(Some("Line1"), None, None)), postcode = Some("PR2 8AE"))
  val moreAboutYourPartner = MoreAboutYourPartner(startedLivingTogether = Some(YesNoWithDate("yes", Some(DayMonthYear(1, 1, 1940)))), separated = YesNoWithDate("no", None))
  val personYouCareFor = PersonYouCareFor(isPartnerPersonYouCareFor = "yes")
  val yourPartner = YourPartner(yourPartnerPersonalDetails, yourPartnerContactDetails, moreAboutYourPartner, Some(personYouCareFor))

  val howWePayYou = HowWePayYou(NoRouting, "01", "everyWeek")
  val bank = BankBuildingSocietyDetails(NoRouting, "Holder", "Bank name", SortCode("12", "34", "56"), "1234567890", "1234")

  val payDetails = PayDetails(howWePayYou,Some(bank))

  val additionalInfo = AdditionalInfo(NoRouting, Some("Other information"), "yes")
  val consent = Consent(NoRouting, "no", Some("I don't want to"), "no", Some("I said I don't want to"))
  val disclaimer = Disclaimer(NoRouting, "checked")
  val declaration = Declaration(NoRouting, "checked", Some("checked"))

  val consentAndDeclaration = ConsentAndDeclaration(additionalInfo, consent, disclaimer, declaration)
}
