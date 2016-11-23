package models.domain

import controllers.mappings.Mappings
import models.yesNo._
import models.DayMonthYear
import models.SortCode
import models.MultiLineAddress
import models._
import utils.helpers.TextLengthHelper

object CircumstancesReportChanges extends Identifier(id = "c2")

case class CircumstancesSelfEmployment(stillCaring: YesNoWithDate = YesNoWithDate("", None),
                                       whenThisSelfEmploymentStarted: DayMonthYear = DayMonthYear(),
                                       typeOfBusiness: String = "",
                                       paidMoneyYet: OptYesNoWithDate = OptYesNoWithDate(None, None),
                                       totalOverWeeklyIncomeThreshold: String = "",
                                       moreAboutChanges: Option[String] = None)
  extends QuestionGroup(CircumstancesSelfEmployment)

object CircumstancesSelfEmployment extends QGIdentifier(s"${CircumstancesReportChanges.id}.g2") {
  def textMaxLength = TextLengthHelper.textMaxLength("DWPCAChangeOfCircumstances//EmploymentChange//SelfEmployment//MoreAboutChanges//Answer")
}

case class CircumstancesStoppedCaring(stoppedCaringDate: DayMonthYear = DayMonthYear(None, None, None),
                                      moreAboutChanges: Option[String] = None) extends QuestionGroup(CircumstancesStoppedCaring)

object CircumstancesStoppedCaring extends QGIdentifier(id = s"${CircumstancesReportChanges.id}.g3") {

  def textMaxLength = TextLengthHelper.textMaxLength("DWPCAChangeOfCircumstances//StoppedCaring//OtherChanges//Answer")
}

case class CircumstancesOtherInfo(change: String = "") extends QuestionGroup(CircumstancesOtherInfo)

object CircumstancesOtherInfo extends QGIdentifier(id = s"${CircumstancesReportChanges.id}.g4") {

  def textMaxLength = TextLengthHelper.textMaxLength("DWPCAChangeOfCircumstances//OtherChanges//Answer")
}

case class CircumstancesPaymentChange(
                                       currentlyPaidIntoBankAnswer: String = "",
                                       currentlyPaidIntoBankText1: Option[String] = None,
                                       currentlyPaidIntoBankText2: Option[String] = None,
                                       accountHolderName: String = "",
                                       bankFullName: String = "",
                                       sortCode: SortCode = SortCode("", "", ""),
                                       accountNumber: String = "",
                                       rollOrReferenceNumber: String = "",
                                       paymentFrequency: String = "",
                                       moreAboutChanges: Option[String] = None
                                       ) extends QuestionGroup(CircumstancesPaymentChange)

object CircumstancesPaymentChange extends QGIdentifier(id = s"${CircumstancesReportChanges.id}.g5") {

  def textMaxLength = TextLengthHelper.textMaxLength("DWPCAChangeOfCircumstances//PaymentChange//OtherChanges//Answer")
}

case class CircumstancesAddressChange(previousAddress: MultiLineAddress = new MultiLineAddress(),
                                      previousPostcode: Option[String] = None,
                                      stillCaring: YesNoWithDateAndQs = YesNoWithDateAndQs("", None, None),
                                      newAddress: MultiLineAddress = new MultiLineAddress(),
                                      newPostcode: Option[String] = None,
                                      caredForChangedAddress: OptYesNoWithText = OptYesNoWithText(None, None),
                                      sameAddress: YesNoWithAddress = YesNoWithAddress(None, None),
                                      moreAboutChanges: Option[String] = None) extends QuestionGroup(CircumstancesAddressChange)

object CircumstancesAddressChange extends QGIdentifier(id = s"${CircumstancesReportChanges.id}.g6") {

  def textMaxLength = TextLengthHelper.textMaxLength("DWPCAChangeOfCircumstances//AddressChange//OtherChanges//Answer")
}

case class CircumstancesBreaksInCare(breaksInCareStartDate: DayMonthYear = DayMonthYear(None, None, None),
                                     breaksInCareStartTime: Option[String] = None,
                                     wherePersonBreaksInCare: RadioWithText = RadioWithText("", None),
                                     whereYouBreaksInCare: RadioWithText = RadioWithText("", None),
                                     breakEnded: YesNoWithDateTimeAndText = YesNoWithDateTimeAndText("", None, None),
                                     expectStartCaring: YesNoDontKnowWithDates = YesNoDontKnowWithDates(None, None, None),
                                     medicalCareDuringBreak: String = "",
                                     moreAboutChanges: Option[String] = None) extends QuestionGroup(CircumstancesBreaksInCare)

object CircumstancesBreaksInCare extends QGIdentifier(id = s"${CircumstancesReportChanges.id}.g7") {

  def textMaxLength = TextLengthHelper.textMaxLength("DWPCAChangeOfCircumstances//BreakFromCaring//MoreChanges//Answer")
}

case class CircumstancesBreaksInCareSummary(additionalBreaks: YesNoWithText = YesNoWithText("", None)) extends QuestionGroup(CircumstancesBreaksInCareSummary)

object CircumstancesBreaksInCareSummary extends QGIdentifier(id = s"${CircumstancesReportChanges.id}.g8") {

  def textMaxLength = TextLengthHelper.textMaxLength("DWPCAChangeOfCircumstances//BreakFromCaring//AdditionalBreaksNotReportedDesc//Answer")
}

case class CircumstancesEmploymentChange(stillCaring: YesNoWithDate = YesNoWithDate("", None),
                                         hasWorkStartedYet: YesNoWithMutuallyExclusiveDates = YesNoWithMutuallyExclusiveDates("", None, None),
                                         hasWorkFinishedYet: OptYesNoWithDate = OptYesNoWithDate(None, None),
                                         typeOfWork: YesNoWithAddressAnd2TextOrTextWithYesNoAndText = YesNoWithAddressAnd2TextOrTextWithYesNoAndText("", None, None, None, None),
                                         paidMoneyYet: OptYesNoWithDate = OptYesNoWithDate(None, None)
                                          )
  extends QuestionGroup(CircumstancesEmploymentChange)

object CircumstancesEmploymentChange extends QGIdentifier(id = s"${CircumstancesReportChanges.id}.g9")

case class CircumstancesEmploymentPay(
                                       pastpresentfuture: String="present",
                                       paid: Option[String]=None,
                                       howMuch: Option[String]=None,
                                       payDate: Option[DayMonthYear]=None,
                                       whatWasIncluded: Option[String]=None,
                                       howOften: PaymentFrequency=PaymentFrequency("",None),
                                       monthlyPayDay: Option[String]=None,
                                       sameAmount: Option[String]=None,
                                       owedMoney: Option[String]=None,
                                       owedMoneyInfo: Option[String] = None)
  extends QuestionGroup(CircumstancesEmploymentPay)

object CircumstancesEmploymentPay extends QGIdentifier(id = s"${CircumstancesReportChanges.id}.g11") {

  def presentPastOrFuture(employment: CircumstancesEmploymentChange) = {
    (employment.hasWorkStartedYet.answer, employment.hasWorkFinishedYet.answer) match {
      case (Mappings.yes, Some(Mappings.yes)) => "past"
      case (Mappings.yes, _) => "present"
      case (Mappings.no, _) => "future"
      case _ => "error-no-employment-tense"
    }
  }
}

case class CircumstancesStartedEmploymentAndOngoing(beenPaid: String,
                                                    howMuchPaid: String,
                                                    date: DayMonthYear,
                                                    howOften: PaymentFrequency,
                                                    monthlyPayDay: Option[String],
                                                    usuallyPaidSameAmount: Option[String])
  extends QuestionGroup(CircumstancesStartedEmploymentAndOngoing)

object CircumstancesStartedEmploymentAndOngoing extends QGIdentifier(id = s"${CircumstancesReportChanges.id}.g10")

case class CircumstancesStartedAndFinishedEmployment(beenPaid: String,
                                                     howMuchPaid: String,
                                                     dateLastPaid: DayMonthYear,
                                                     whatWasIncluded: Option[String],
                                                     howOften: PaymentFrequency,
                                                     monthlyPayDay: Option[String],
                                                     usuallyPaidSameAmount: Option[String],
                                                     employerOwesYouMoney: String,
                                                     employerOwesYouMoneyInfo: Option[String] = None)
  extends QuestionGroup(CircumstancesStartedAndFinishedEmployment)

object CircumstancesStartedAndFinishedEmployment extends QGIdentifier(id = s"${CircumstancesReportChanges.id}.g99")

case class CircumstancesEmploymentNotStarted(beenPaid: String,
                                             howMuchPaid: Option[String],
                                             whenExpectedToBePaidDate: Option[DayMonthYear],
                                             howOften: PaymentFrequency,
                                             usuallyPaidSameAmount: Option[String])
  extends QuestionGroup(CircumstancesEmploymentNotStarted)

object CircumstancesEmploymentNotStarted extends QGIdentifier(id = s"${CircumstancesReportChanges.id}.g12")

case class CircumstancesEmploymentPensionExpenses(payIntoPension: YesNoWithText = YesNoWithText("", None),
                                                  payForThings: YesNoWithText = YesNoWithText("", None),
                                                  careCosts: YesNoWithText = YesNoWithText("", None),
                                                  moreAboutChanges: Option[String] = None)
  extends QuestionGroup(CircumstancesEmploymentPensionExpenses)

object CircumstancesEmploymentPensionExpenses extends QGIdentifier(id = s"${CircumstancesReportChanges.id}.g13") {

  def payIntoPensionMaxLength = TextLengthHelper.textMaxLength("DWPCAChangeOfCircumstances//EmploymentChange//StartedEmploymentAndOngoing//PayIntoPensionWhatFor//Answer")

  def payForThingsMaxLength = TextLengthHelper.textMaxLength("DWPCAChangeOfCircumstances//EmploymentChange//StartedEmploymentAndOngoing//PaidForThingsWhatFor//Answer")

  def careCostsMaxLength = TextLengthHelper.textMaxLength("DWPCAChangeOfCircumstances//EmploymentChange//StartedEmploymentAndOngoing//CareCostsForThisWorkWhatCosts//Answer")

  def moreAboutChangesMaxLength = TextLengthHelper.textMaxLength("DWPCAChangeOfCircumstances//EmploymentChange//StartedEmploymentAndOngoing//MoreAboutChanges//Answer")

  def presentPastOrFuture(employment: CircumstancesEmploymentChange) = {
    (employment.hasWorkStartedYet.answer, employment.hasWorkFinishedYet.answer) match {
      case (Mappings.yes, Some(Mappings.yes)) => "past"
      case (Mappings.yes, _) => "present"
      case (Mappings.no, _) => "future"
      case _ => "error-no-employment-tense"
    }
  }
}
