package controllers

import java.io.FileReader

import controllers.mappings.Mappings
import gov.dwp.carers.xml.signing.XmlSignatureValidator
import gov.dwp.carers.xml.validation.XmlValidatorFactory
import gov.dwp.exceptions.DwpRuntimeException
import models.yesNo._
import models.{PaymentFrequency, MultiLineAddress, DayMonthYear, NationalInsuranceNumber}
import models.domain._
import play.api.Play.current
import play.api.i18n.{MMessages, MessagesApi, I18nSupport}
import play.api.routing.Router
import scala.language.postfixOps
import play.api.mvc.{Action, Controller}
import submission._
import scala.util.{Success, Failure, Try}
import scala.xml.{NodeSeq, XML}
import app.ConfigProperties._
import play.api.Logger
import scala.collection.JavaConversions._

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
                          pathList.filter(_.matches("/disclaimer.*")) ,
                          pathList.filter(_.matches("/third-party.*")) ,
                          pathList.filter(_.matches("/your-claim-date.*")),
                          pathList.filter(_.matches("/your-claim-date")) ,
                          pathList.filter(_.matches("/about-you.*")) ,
                          pathList.filter(_.matches("/your-partner.*")) ,
                          pathList.filter(_.matches("/care-you-provide.*")) ,
                          pathList.filter(_.matches("/breaks.*")) ,
                          pathList.filter(_.matches("/education.*")),
                          pathList.filter(_.matches("/employment.*")) ,
                          pathList.filter(_.matches("/self-employment.*")),
                          pathList.filter(_.matches("/your-income.*")) ,
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
  def newInstance(cacheKey: String, newuuid: String) = {
    val claim = new Claim(cacheKey, uuid = newuuid)
    Try(loadAndValidateXMLFile) match {
      case Success(xml) => claimGeneratorFromXml(xml, claim)
      case Failure(e) =>
        Logger.error(s"Failed to load replica file, loading default class data: $e", e)
        claim + Benefits(Benefits.aa) +
          Eligibility(hours = Mappings.yes, over16 = Mappings.yes, origin = "GB") +
          ThirdPartyDetails(thirdParty = ThirdPartyDetails.noCarer, nameAndOrganisation  = Some("Jenny Bloggs Preston carers")) +
          ClaimDate(DayMonthYear.today,spent35HoursCaringBeforeClaim = YesNoWithDate(Mappings.yes,date = Some(DayMonthYear.today - 3 months))) +
          YourDetails(title = "Mr",firstName = "Joe",surname = "Bloggs",nationalInsuranceNumber = NationalInsuranceNumber(Some("AB123456D")),dateOfBirth = DayMonthYear(10,10,2014)) +
          MaritalStatus(maritalStatus = app.MaritalStatus.Single) +
          ContactDetails(address = new MultiLineAddress(Some("123"),Some("Street")),postcode = Some("C4T 0AD"),howWeContactYou = Some("0000000000"),wantsContactEmail = Mappings.yes, email = Some("CAU.CASA@dwp.gsi.gov.uk"), emailConfirmation = Some("CAU.CASA@dwp.gsi.gov.uk")) +
          NationalityAndResidency(nationality = NationalityAndResidency.british,resideInUK = YesNoWithText(Mappings.yes)) +
          AbroadForMoreThan52Weeks(anyTrips = Mappings.no) +
          OtherEEAStateOrSwitzerland(guardQuestion = YesNoWith2MandatoryFieldsOnYes(answer = Mappings.yes,field1 = Some(YesNoWith1MandatoryFieldOnYes(answer = Mappings.no)), field2 = Some(YesNoWith1MandatoryFieldOnYes(answer = Mappings.no)))) +
          YourPartnerPersonalDetails(hadPartnerSinceClaimDate = Mappings.yes,title = Some("Miss"),firstName = Some("Joan"),surname = Some("Bloggs"),dateOfBirth = Some(DayMonthYear.today - 10 years),nationality = Some("British"),separatedFromPartner = Some(Mappings.no),isPartnerPersonYouCareFor = Some(Mappings.no)) +
          TheirPersonalDetails(relationship = "Grandma",title = "Mrs",firstName = "Jane",surname = "Bloggs",dateOfBirth = DayMonthYear.today - 15 years,theirAddress = YesNoMandWithAddress(answer = Mappings.no, address = Option(MultiLineAddress(lineOne = Some("470 Street"),lineTwo = Some("Newtown"))), postCode = Some("PR1 1HB"))) +
          MoreAboutTheCare(spent35HoursCaring = Mappings.yes) +
          BreaksInCareSummary(Mappings.no) +
          BreaksInCare(List(
            Break("1",DayMonthYear.today - 4 months,hasBreakEnded = YesNoWithDate(answer = Mappings.no,None),whereYou = RadioWithText(app.CircsBreaksWhereabouts.Holiday),wherePerson = RadioWithText(app.CircsBreaksWhereabouts.Home),medicalDuringBreak = Mappings.no),
            Break("2",DayMonthYear.today - 3 months,hasBreakEnded = YesNoWithDate(answer = Mappings.no,None),whereYou = RadioWithText(app.CircsBreaksWhereabouts.Hospital),wherePerson = RadioWithText(app.CircsBreaksWhereabouts.Home),medicalDuringBreak = Mappings.yes)
          )) +
          YourCourseDetails(beenInEducationSinceClaimDate = Mappings.yes,title = Some("Biology"),nameOfSchoolCollegeOrUniversity = Some("A College"),
          nameOfMainTeacherOrTutor = Some("A Tutor"),startDate = Some(DayMonthYear.today - 1 month),expectedEndDate = Some(DayMonthYear.today + 2 years)) +
          YourIncomes(beenSelfEmployedSince1WeekBeforeClaim = Mappings.yes, beenEmployedSince6MonthsBeforeClaim = Mappings.yes, yourIncome_sickpay = Mappings.someTrue, yourIncome_patmatadoppay = Mappings.someTrue, yourIncome_fostering = Mappings.someTrue, yourIncome_directpay = Mappings.someTrue, yourIncome_anyother = Mappings.someTrue, yourIncome_none = None) +
          Employment(beenSelfEmployedSince1WeekBeforeClaim = Mappings.yes, beenEmployedSince6MonthsBeforeClaim = Mappings.yes) +
          SelfEmploymentDates(stillSelfEmployed = Mappings.no, finishThisWork=Some(DayMonthYear.today - 2 months), moreThanYearAgo = Mappings.no, startThisWork = Some(DayMonthYear.today - 6 months), paidMoney = Some(Mappings.yes), paidMoneyDate = Some(DayMonthYear.today - 5 months)) +
          SelfEmploymentPensionsAndExpenses(payPensionScheme = YesNoWithText(Mappings.no),haveExpensesForJob = YesNoWithText(Mappings.no)) +
          Jobs(List(
            models.domain.Iteration("1", List(
              JobDetails("1",employerName = "Hiding Consultants", phoneNumber = "01111111111", address = MultiLineAddress(lineOne = Some("456 Street"),lineTwo = Some("Newtown")),
                startJobBeforeClaimDate = Mappings.yes,finishedThisJob = Mappings.no),
                LastWage("1",oftenGetPaid = PaymentFrequency(app.PensionPaymentFrequency.Weekly),whenGetPaid = "Fridays",lastPaidDate = DayMonthYear.today - 1 week,grossPay = "3000",payInclusions = Some("Some catnip on the side"),sameAmountEachTime = Mappings.yes),
                PensionAndExpenses("1",payPensionScheme = YesNoWithText(Mappings.no),payForThings = YesNoWithText(Mappings.no),haveExpensesForJob = YesNoWithText(Mappings.no))
              ),completed = true)
          )) +
          BeenEmployed(Mappings.no) +
          EmploymentAdditionalInfo(YesNoWithText(Mappings.no)) +
          StatutorySickPay(stillBeingPaidThisPay = Mappings.no, whenDidYouLastGetPaid = Some(DayMonthYear.today - 3 months), whoPaidYouThisPay = "Hiding Consultants", amountOfThisPay = "123.56", howOftenPaidThisPay = app.StatutoryPaymentFrequency.Weekly, howOftenPaidThisPayOther = None) +
          StatutoryMaternityPaternityAdoptionPay(paymentTypesForThisPay = app.PaymentTypes.MaternityPaternity, stillBeingPaidThisPay = Mappings.no, whenDidYouLastGetPaid = Some(DayMonthYear.today - 3 months), whoPaidYouThisPay = "Hiding Consultants", amountOfThisPay = "223.56", howOftenPaidThisPay = app.StatutoryPaymentFrequency.FourWeekly, howOftenPaidThisPayOther = None) +
          FosteringAllowance(paymentTypesForThisPay = app.PaymentTypes.FosteringAllowance, stillBeingPaidThisPay = Mappings.yes, whenDidYouLastGetPaid = None, whoPaidYouThisPay = "Hiding Consultants", amountOfThisPay = "323.56", howOftenPaidThisPay = app.StatutoryPaymentFrequency.Monthly, howOftenPaidThisPayOther = None) +
          DirectPayment(stillBeingPaidThisPay = Mappings.no, whenDidYouLastGetPaid = Some(DayMonthYear.today - 3 months), whoPaidYouThisPay = "Hiding Consultants", amountOfThisPay = "423.56", howOftenPaidThisPay = app.StatutoryPaymentFrequency.ItVaries, howOftenPaidThisPayOther = Some("Twice a day")) +
          OtherPayments(otherPaymentsInfo = "Testing other payments") +
          HowWePayYou(likeToBePaid = Mappings.no,paymentFrequency = app.PaymentFrequency.EveryWeek) +
          AdditionalInfo(anythingElse = YesNoWithText(Mappings.no),welshCommunication = Mappings.no)
    }
  }

  private def loadAndValidateXMLFile() = {
    //try file system if that fails try resources
    val fileName = getProperty("replica.replicaFileData", default = "/replica/DefaultClaim.xml")
    val xml = Try(XML.load(new FileReader(fileName))) match {
      case Success(xml) => xml
      case Failure(e) => XML.load(getClass getResourceAsStream(fileName))
    }
    if (getProperty("replica.validate.xml.data", default = true))validateLoadedXML(xml, fileName)
    if (getProperty("replica.validate.xml.signature", default = true))validateXMLSignature(xml, fileName)
    xml
  }

  private def validateLoadedXML(xml: NodeSeq, fileName: String) = {
    val xmlValidator = XmlValidatorFactory.buildCaFutureValidator()
    val xmlErrors = xmlValidator.validate(xml.mkString)
    if (xmlErrors.hasFoundErrorOrWarning) {
      xmlErrors.getWarningAndErrors.toList.foreach(errorMsg => Logger.error(errorMsg))
      throw new DwpRuntimeException(s"Validation of xml file $fileName failed")
    }
  }

  private def validateXMLSignature(xml: NodeSeq, fileName: String) = {
    XmlSignatureValidator.validate(xml.mkString) match {
      case true => true
      case false =>
        Logger.error(s"Failed to validate signature of msg for filename $fileName")
        throw new DwpRuntimeException(s"Failed to validate signature of msg for filename $fileName")
    }
  }
}
