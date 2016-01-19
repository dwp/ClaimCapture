package controllers

import controllers.mappings.Mappings
import models.yesNo._
import models.{PaymentFrequency, MultiLineAddress, DayMonthYear, NationalInsuranceNumber}
import models.domain._
import play.api.Play.current
import play.api.i18n.{MMessages, MessagesApi, I18nSupport}
import play.api.routing.Router
import scala.language.postfixOps
import play.api.mvc.{Action, Controller}


object Replica extends Controller with I18nSupport {
  override val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
  val router = current.injector.instanceOf[Router]
  def list = Action { request =>


        val pathList = router.documentation.map(_._2)

        //This list will help us remove the paths of the urls in iterated nodes (they appear duplicated with and without the $id<[^/]+> part)
        val iteratedPaths = pathList.filter(_.matches(".*/\\$id.*")).map { p =>
          val m = "(.*)/\\$id.*".r.pattern.matcher(p)
          if(m.matches()) m.group(1) else "fail"
        }.flatMap(e => Seq(e,e+"/$id<[^/]+>"))

        val updatedList = Seq(pathList.filter(_.matches("/allowance.*")) ,
                          pathList.filter(_.matches("/your-claim-date.*")),
                          pathList.filter(_.matches("/your-claim-date")) ,
                          pathList.filter(_.matches("/about-you.*")) ,
                          pathList.filter(_.matches("/your-partner.*")) ,
                          pathList.filter(_.matches("/care-you-provide.*")) ,
                          pathList.filter(_.matches("/breaks.*")) ,
                          pathList.filter(_.matches("/education.*")),
                          pathList.filter(_.matches("/employment.*")) ,
                          pathList.filter(_.matches("/self-employment.*")) ,
                          pathList.filter(_.matches("/other-money.*")) ,
                          pathList.filter(_.matches("/pay-details.*")) ,
                          pathList.filter(_.matches("/information.*")) ,
                          pathList.filter(_.matches("/preview")) ,
                          pathList.filter(_.matches("/consent-and-declaration.*")) ,
                          pathList.filter(_.matches("/thankyou/apply-carers"))

        ).map(_.filterNot(iteratedPaths.contains(_)))
         .map(_.filterNot(_.matches(".*delete.*")))
         .map(_.filterNot(_.matches(".*error.*")))
         .filterNot(_.isEmpty)

        val finalList = updatedList.map(_.distinct)
        Ok(views.html.replica.routesList(finalList))

  }
}

object ReplicaData {
  def newInstance(cacheKey:String,newuuid:String) = {
    import Mappings._

    val claim = new Claim(cacheKey,uuid = newuuid)
    claim + Benefits(Benefits.aa) +
      Eligibility(hours = yes, over16 = yes,livesInGB = yes) +
      ThirdPartyDetails(thirdParty = ThirdPartyDetails.noCarer, nameAndOrganisation  = Some("Jenny Bloggs Preston carers")) +
      ClaimDate(DayMonthYear.today,spent35HoursCaringBeforeClaim = YesNoWithDate(yes,date = Some(DayMonthYear.today - 3 months))) +
      YourDetails(title = "Mr",firstName = "Joe",surname = "Bloggs",nationalInsuranceNumber = NationalInsuranceNumber(Some("AB123456D")),dateOfBirth = DayMonthYear(10,10,2014)) +
      MaritalStatus(maritalStatus = app.MaritalStatus.Single) +
      ContactDetails(address = new MultiLineAddress(Some("123"),Some("Street")),postcode = Some("C4T 0AD"),howWeContactYou = Some("0000000000"),wantsContactEmail = Mappings.yes, email = Some("CAU.CASA@dwp.gsi.gov.uk"), emailConfirmation = Some("CAU.CASA@dwp.gsi.gov.uk")) +
      NationalityAndResidency(nationality = NationalityAndResidency.british,resideInUK = YesNoWithText(yes)) +
      AbroadForMoreThan52Weeks(anyTrips = no) +
      OtherEEAStateOrSwitzerland(guardQuestion = YesNoWith2MandatoryFieldsOnYes(answer = yes,field1 = Some(YesNoWith1MandatoryFieldOnYes(answer = no)), field2 = Some(YesNoWith1MandatoryFieldOnYes(answer = no)))) +
      YourPartnerPersonalDetails(hadPartnerSinceClaimDate = yes,title = Some("Miss"),firstName = Some("Joan"),surname = Some("Bloggs"),dateOfBirth = Some(DayMonthYear.today - 10 years),nationality = Some("British"),separatedFromPartner = Some(no),isPartnerPersonYouCareFor = Some(no)) +
      TheirPersonalDetails(relationship = "Grandma",title = "Mrs",firstName = "Jane",surname = "Bloggs",dateOfBirth = DayMonthYear.today - 15 years,theirAddress = YesNoMandWithAddress(answer = no, address = Option(MultiLineAddress(lineOne = Some("470 Street"),lineTwo = Some("Newtown"))), postCode = Some("PR1 1HB"))) +
      MoreAboutTheCare(spent35HoursCaring = yes) +
      BreaksInCareSummary(no) +
      BreaksInCare(List(
        Break("1",DayMonthYear.today - 4 months,hasBreakEnded = YesNoWithDate(answer = no,None),whereYou = RadioWithText(app.CircsBreaksWhereabouts.Holiday),wherePerson = RadioWithText(app.CircsBreaksWhereabouts.Home),medicalDuringBreak = no),
        Break("2",DayMonthYear.today - 3 months,hasBreakEnded = YesNoWithDate(answer = no,None),whereYou = RadioWithText(app.CircsBreaksWhereabouts.Hospital),wherePerson = RadioWithText(app.CircsBreaksWhereabouts.Home),medicalDuringBreak = yes)
      )) +
      YourCourseDetails(beenInEducationSinceClaimDate = yes,title = Some("Biology"),nameOfSchoolCollegeOrUniversity = Some("A College"),
                        nameOfMainTeacherOrTutor = Some("A Tutor"),startDate = Some(DayMonthYear.today - 1 month),expectedEndDate = Some(DayMonthYear.today + 2 years)) +
      Employment(beenSelfEmployedSince1WeekBeforeClaim = yes, beenEmployedSince6MonthsBeforeClaim = yes) +
      AboutSelfEmployment(areYouSelfEmployedNow = yes,whenDidYouStartThisJob = DayMonthYear.today - 2 months,natureOfYourBusiness = "Consultancy") +
      SelfEmploymentYourAccounts(doYouKnowYourTradingYear = no) +
      SelfEmploymentPensionsAndExpenses(payPensionScheme = YesNoWithText(no),haveExpensesForJob = YesNoWithText(no)) +
      Jobs(List(
        models.domain.Iteration("1", List(
          JobDetails("1",employerName = "Hiding Consultants", phoneNumber = "01111111111", address = MultiLineAddress(lineOne = Some("456 Street"),lineTwo = Some("Newtown")),
                    startJobBeforeClaimDate = yes,finishedThisJob = no),
          LastWage("1",oftenGetPaid = PaymentFrequency(app.PensionPaymentFrequency.Weekly),whenGetPaid = "Fridays",lastPaidDate = DayMonthYear.today - 1 week,grossPay = "3000",payInclusions = Some("Some catnip on the side"),sameAmountEachTime = yes),
          PensionAndExpenses("1",payPensionScheme = YesNoWithText(no),payForThings = YesNoWithText(no),haveExpensesForJob = YesNoWithText(no))
          ),completed = true)
      )) +
      BeenEmployed(no) +
      EmploymentAdditionalInfo(YesNoWithText(no)) +
      AboutOtherMoney(anyPaymentsSinceClaimDate = YesNo(no),statutorySickPay = YesNoWithEmployerAndMoney(no),otherStatutoryPay = YesNoWithEmployerAndMoney(no)) +
      HowWePayYou(likeToBePaid = no,paymentFrequency = app.PaymentFrequency.EveryWeek) +
      AdditionalInfo(anythingElse = YesNoWithText(no),welshCommunication = no)

  }
}
