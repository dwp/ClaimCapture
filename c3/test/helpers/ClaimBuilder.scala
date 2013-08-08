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
                           moreAboutYou:MoreAboutYou,
                           employment: Employment,
                           propertyAndRent: PropertyAndRent)

case class YourPartnerSection(yourPartnerPersonalDetails: YourPartnerPersonalDetails, yourPartnerContactDetails: YourPartnerContactDetails,
                       moreAboutYourPartner: MoreAboutYourPartner, personYouCareFor: Option[PersonYouCareFor])


case class EducationSection(yourCourseDetails: YourCourseDetails, addressOfSchool: AddressOfSchoolCollegeOrUniversity)

case class CareYouProvideSection(theirPersonalDetails: TheirPersonalDetails, theirContactDetails: TheirContactDetails,
                          moreAboutThePerson: MoreAboutThePerson, representatives: RepresentativesForPerson,
                          previousCarerContactDetails: Option[PreviousCarerContactDetails], previousCarerPersonalDetails: Option[PreviousCarerPersonalDetails],
                          moreAboutTheCare: MoreAboutTheCare, oneWhoPays: Option[OneWhoPaysPersonalDetails],
                          contactDetailsPayingPerson: Option[ContactDetailsOfPayingPerson], breaksInCare: BreaksInCare)

case class OtherMoneySection(aboutOtherMoney: AboutOtherMoney,
                             moneyPaidToSomeoneElse: MoneyPaidToSomeoneElseForYou,
                             personWhoGetsThisMoney: PersonWhoGetsThisMoney,
                             personContactDetails: PersonContactDetails,
                             statutorySickPay: StatutorySickPay,
                             otherStatutoryPay: OtherStatutoryPay)

case class TimeSpentAbroadSection(normalResidence: NormalResidenceAndCurrentLocation, abroadForMoreThan4Weeks:AbroadForMoreThan4Weeks, trips: Trips)

case class SelfEmploymentSection(aboutSelfEmployment: AboutSelfEmployment,
                                 selfEmploymentYourAccounts: SelfEmploymentYourAccounts,
                                 accountantContactDetails: SelfEmploymentAccountantContactDetails,
                                 pensionsAndExpenses: SelfEmploymentPensionsAndExpenses,
                                 childcareExpenses: ChildcareExpensesWhileAtWork,
                                 expensesWhileAtWork: ExpensesWhileAtWork)

case class ConsentAndDeclarationSection(additionalInfo: AdditionalInfo, consent: Consent, disclaimer:Disclaimer, declaration:Declaration)

case class PayDetailsSection(howWePayYou: Option[HowWePayYou],bankBuildingSocietyDetails: Option[BankBuildingSocietyDetails])

object ClaimBuilder {
  val yourDetails = YourDetails(title = "mr", firstName = "Phil", middleName = None, surname = "Smith",
    otherSurnames = Some("O'Dwyer"), None, nationality = "French",
    dateOfBirth = DayMonthYear(1, 1, 1963), maritalStatus = "m", alwaysLivedUK = "no")

  val contactDetails = ContactDetails(address = MultiLineAddress(Some("Line1"), None, None),
    postcode = Some("PR2 8AE"),
    phoneNumber = Some("01772 700806"), None)

  val timeOutsideUK = TimeOutsideUK(livingInUK = LivingInUK("yes", Some(DayMonthYear(Some(1), Some(2), Some(2013))), Some("netherlands"), Some(YesNoWithDate("yes", Some(DayMonthYear(Some(3), Some(4), Some(2012)))))), visaReference = None)

  val claimDate = ClaimDate(dateOfClaim = DayMonthYear(1, 1, 2013))
  val moreAboutYou = MoreAboutYou(yes, yes, yes)
  val employment = Employment(beenSelfEmployedSince1WeekBeforeClaim = yes, beenEmployedSince6MonthsBeforeClaim = yes)
  val propertyAndRent = PropertyAndRent(yes, yes)
  val aboutYou = AboutYouSection(yourDetails, contactDetails, Some(timeOutsideUK), claimDate, moreAboutYou, employment, propertyAndRent)

  val theirPersonalDetails = TheirPersonalDetails(title = "ms", firstName = "Minnie", middleName = Some("middleName"), surname = "Mouse",
    nationalInsuranceNumber = Some(NationalInsuranceNumber(Some("AA"), Some("12"), Some("34"), Some("56"), Some("A"))), dateOfBirth = DayMonthYear(1, 1, 1963), liveAtSameAddressCareYouProvide = "no")

  val theirContactDetails = TheirContactDetails(address = MultiLineAddress(Some("Line1"), None, None), postcode = Some("PR2 8AE"))

  val moreAboutThePerson = MoreAboutThePerson(relationship = "mother", None, claimedAllowanceBefore = "no")

  val representatives = RepresentativesForPerson(youAct = YesNoWithDropDown("yes", Some("Lawyer")), someoneElseAct = YesNoWithDropDownAndText(Some("yes"), Some("Lawyer"), Some("Mr. Lawyer")))
  
  val previousCarerContactDetails = PreviousCarerContactDetails(address = Some(MultiLineAddress(Some("Line1"), None, None)), postcode = Some("PR2 8AE"))

  val previousCarerPersonalDetails = PreviousCarerPersonalDetails(firstName = Some("Some"), middleName = None, surname = Some("One"), None, dateOfBirth = Some(DayMonthYear(1, 1, 1963)))

  val moreAboutTheCare = MoreAboutTheCare(spent35HoursCaring = "yes", spent35HoursCaringBeforeClaim = YesNoWithDate("no", Some(DayMonthYear(1, 1, 2013))), hasSomeonePaidYou = "yes")

  val oneWhoPays = OneWhoPaysPersonalDetails(organisation = Some("SomeOrg Inc."), amount = "300", startDatePayment = DayMonthYear(1, 1, 2012))

  val contactDetailsPayingPerson = ContactDetailsOfPayingPerson(address = Some(MultiLineAddress(Some("Line1"), None, None)), postcode = Some("PR2 8AE"))

  val breaksInCare = BreaksInCare(List(
    Break(id = "1", start = DayMonthYear(1, 1, 2001), end = Some(DayMonthYear(1, 5, 2001)), whereYou = Whereabouts("Holiday", None), wherePerson = Whereabouts("Hospital", None), medicalDuringBreak = "yes"),
    Break(id = "2", start = DayMonthYear(1, 1, 2002), end = Some(DayMonthYear(1, 5, 2002)), whereYou = Whereabouts("Holiday", None), wherePerson = Whereabouts("Hospital", None), medicalDuringBreak = "yes")))

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
        ChildcareExpenses("1",Some("1234"),"dunno",Some("other"),Some("other"),Some("other")),
        ChildcareProvider("1",None,None),
        PersonYouCareForExpenses("1",Some("10"),"dunno",Some("other"), Some("other")),
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
      ChildcareExpenses("2",Some("2333"),"dunno",Some("other"),Some("other"),Some("other")),
      ChildcareProvider("2",None,None),
      PersonYouCareForExpenses("2",Some("10"),"dunno",Some("other"), Some("other")),
      CareProvider("2",None,None)
    ))
  ))

  val normalResidence = NormalResidenceAndCurrentLocation(whereDoYouLive = YesNoWithText(answer = yes, text = Some("UK")), inGBNow = yes)
  val abroadForMoreThan4Weeks = AbroadForMoreThan4Weeks(anyTrips=no)
  val fourWeekTrip = Trip(id = "four-one", DayMonthYear(Some(1), Some(2), Some(2011)), DayMonthYear(Some(1), Some(3), Some(2011)), "Netherlands", Some("Holiday"))
  val fiftyTwoWeeksTrip = Trip(id = "fiftyTwo-one", DayMonthYear(Some(1), Some(2), Some(2012)), DayMonthYear(Some(1), Some(2), Some(2013)), "Spain", Some("Family"))
  val trips = Trips(fourWeeksTrips = List(fourWeekTrip), fiftyTwoWeeksTrips = List(fiftyTwoWeeksTrip))

  val timeSpentAbroad = TimeSpentAbroadSection(normalResidence, abroadForMoreThan4Weeks, trips)

  val education = EducationSection(YourCourseDetails(Some("courseType"), Some("courseTitle"), Some(DayMonthYear(Some(1), Some(1), Some(2001))), Some(DayMonthYear(Some(2), Some(2), Some(2002))), Some(DayMonthYear(Some(3), Some(3), Some(2003))), Some("ST11")),
    AddressOfSchoolCollegeOrUniversity(Some("schoolName"), Some("tutorName"), Some(MultiLineAddress(Some("line1"), Some("line2"), Some("line3"))), Some("SE1 6EH"), Some("020192827273"), Some("0302928273"))
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
    SelfEmploymentPensionsAndExpenses(pensionSchemeMapping = YesNoWithText(yes, Some("150.5")), doYouPayToLookAfterYourChildren = yes, didYouPayToLookAfterThePersonYouCaredFor = yes),
    ChildcareExpensesWhileAtWork(howMuchYouPay = "150.5", nameOfPerson = "Andy", whatRelationIsToYou = "grandSon", whatRelationIsTothePersonYouCareFor = "relation"),
    ExpensesWhileAtWork(howMuchYouPay = "200.5", nameOfPerson = "NameOfPerson", whatRelationIsToYou = "grandSon", whatRelationIsTothePersonYouCareFor = "grandSon")
  )

  val careYouProvide = CareYouProvideSection(theirPersonalDetails, theirContactDetails,
    moreAboutThePerson, representatives,
    Some(previousCarerContactDetails), Some(previousCarerPersonalDetails),
    moreAboutTheCare, Some(oneWhoPays), Some(contactDetailsPayingPerson), breaksInCare)

  val yourPartnerPersonalDetails = YourPartnerPersonalDetails(title = "mr", firstName = "Michael", middleName = None, surname = "Mouse", otherSurnames = Some("Oswald"), nationalInsuranceNumber = Some(NationalInsuranceNumber(Some("AA"), Some("12"), Some("34"), Some("56"), Some("A"))), dateOfBirth = DayMonthYear(1, 1, 1930), nationality = Some("British"), liveAtSameAddress = "yes")
  val yourPartnerContactDetails = YourPartnerContactDetails(address = Some(MultiLineAddress(Some("Line1"), None, None)), postcode = Some("PR2 8AE"))
  val moreAboutYourPartner = MoreAboutYourPartner(startedLivingTogether = Some(YesNoWithDate("yes", Some(DayMonthYear(1, 1, 1940)))), separated = YesNoWithDate("no", None))
  val personYouCareFor = PersonYouCareFor(isPartnerPersonYouCareFor = "yes")
  val yourPartner = YourPartnerSection(yourPartnerPersonalDetails, yourPartnerContactDetails, moreAboutYourPartner, Some(personYouCareFor))

  val howWePayYou = HowWePayYou(NoRouting, "01", "everyWeek")
  val bank = BankBuildingSocietyDetails(NoRouting, "Holder", "Bank name", SortCode("12", "34", "56"), "1234567890", "1234")

  val payDetails = PayDetailsSection(Some(howWePayYou),Some(bank))

  val additionalInfo = AdditionalInfo(NoRouting, Some("Other information"), "yes")
  val consent = Consent(NoRouting, "no", Some("I don't want to"), "no", Some("I said I don't want to"))
  val disclaimer = Disclaimer(NoRouting, "checked")
  val declaration = Declaration(NoRouting, "checked", Some("checked"))

  val consentAndDeclaration = ConsentAndDeclarationSection(additionalInfo, consent, disclaimer, declaration)
}
