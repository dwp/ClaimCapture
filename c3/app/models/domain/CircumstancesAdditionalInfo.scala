package models.domain

import app.XMLValues._
import models.yesNo._
import models.{MultiLineAddress, SortCode, DayMonthYear}
import play.api.data.Forms._
import models.SortCode
import models.SortCode
import models.MultiLineAddress
import controllers.Mappings._
import models.SortCode
import models.MultiLineAddress
import models._
import controllers.Mappings.yes
import controllers.Mappings.no

case object CircumstancesReportChanges extends Section.Identifier {
  val id = "c2"
}

case class ReportChanges(reportChanges: String = NotAsked) extends QuestionGroup(ReportChanges)

object ReportChanges extends QuestionGroup.Identifier {
  val id = s"${CircumstancesReportChanges.id}.g1"
}

case class CircumstancesSelfEmployment(stillCaring: YesNoWithDate = YesNoWithDate("", None),
                                       whenThisSelfEmploymentStarted: DayMonthYear = DayMonthYear(),
                                       typeOfBusiness: String = "",
                                       totalOverWeeklyIncomeThreshold: String = "",
                                       moreAboutChanges: Option[String] = None)
  extends QuestionGroup(CircumstancesSelfEmployment)

object CircumstancesSelfEmployment extends QuestionGroup.Identifier {
  val id = s"${CircumstancesReportChanges.id}.g2"
}

case class CircumstancesStoppedCaring(stoppedCaringDate: DayMonthYear = DayMonthYear(None, None, None),
                                      moreAboutChanges: Option[String] = None) extends QuestionGroup(CircumstancesStoppedCaring)

object CircumstancesStoppedCaring extends QuestionGroup.Identifier {
  val id = s"${CircumstancesReportChanges.id}.g3"
}

case class CircumstancesOtherInfo(change: String = "") extends QuestionGroup(CircumstancesOtherInfo)

object CircumstancesOtherInfo extends QuestionGroup.Identifier {
  val id = s"${CircumstancesReportChanges.id}.g4"
}

case class CircumstancesPaymentChange(
  currentlyPaidIntoBank: YesNoWith2Text = YesNoWith2Text("", None, None),
  accountHolderName: String = "",
  whoseNameIsTheAccountIn: String = "",
  bankFullName: String = "",
  sortCode: SortCode = SortCode("","",""),
  accountNumber: String = "",
  rollOrReferenceNumber: String = "",
  paymentFrequency: String = "",
  moreAboutChanges: Option[String] = None
) extends QuestionGroup(CircumstancesPaymentChange)

object CircumstancesPaymentChange extends QuestionGroup.Identifier {
  val id = s"${CircumstancesReportChanges.id}.g5"
}

case class CircumstancesAddressChange(previousAddress: MultiLineAddress = new MultiLineAddress(),
                                      previousPostcode: Option[String] = None,
                                      stillCaring: YesNoWithDateAndQs = YesNoWithDateAndQs("", None, None),
                                      newAddress: MultiLineAddress = new MultiLineAddress(),
                                      newPostcode: Option[String] = None,
                                      caredForChangedAddress: OptYesNoWithText = OptYesNoWithText(None, None),
                                      sameAddress: YesNoWithAddress = YesNoWithAddress(None, None),
                                      moreAboutChanges: Option[String] = None) extends QuestionGroup(CircumstancesAddressChange)

object CircumstancesAddressChange extends QuestionGroup.Identifier {
  val id = s"${CircumstancesReportChanges.id}.g6"
}

case class CircumstancesBreaksInCare(breaksInCareStartDate: DayMonthYear = DayMonthYear(None, None, None),
                                     breaksInCareStartTime: Option[String] = None ,
                                     wherePersonBreaksInCare: RadioWithText = RadioWithText("", None),
                                     whereYouBreaksInCare: RadioWithText = RadioWithText("", None),
                                     breakEnded: YesNoWithDateTimeAndText = YesNoWithDateTimeAndText("", None, None),
                                     expectStartCaring: YesNoDontKnowWithDates = YesNoDontKnowWithDates(None, None, None),
                                     medicalCareDuringBreak: String = "",
                                     moreAboutChanges: Option[String] = None) extends QuestionGroup(CircumstancesBreaksInCare)

object CircumstancesBreaksInCare extends QuestionGroup.Identifier {
  val id = s"${CircumstancesReportChanges.id}.g7"
}

case class CircumstancesBreaksInCareSummary(additionalBreaks: YesNoWithText = YesNoWithText("", None)) extends QuestionGroup(CircumstancesBreaksInCareSummary)

object CircumstancesBreaksInCareSummary extends QuestionGroup.Identifier {
  val id = s"${CircumstancesReportChanges.id}.g8"
}

case class CircumstancesEmploymentChange(stillCaring: YesNoWithDate = YesNoWithDate("", None),
                                         hasWorkStartedYet: YesNoWithDateOrDateAndOptYesNoWithDate = YesNoWithDateOrDateAndOptYesNoWithDate("", None, None, OptYesNoWithDate(None, None)),
                                         typeOfWork: YesNoWithAddressAnd2TextOrTextWithYesNoAndText = YesNoWithAddressAnd2TextOrTextWithYesNoAndText("", None, None, None))
  extends QuestionGroup(CircumstancesEmploymentChange)

object CircumstancesEmploymentChange extends QuestionGroup.Identifier {
  val id = s"${CircumstancesReportChanges.id}.g9"
}

case class CircumstancesStartedEmploymentAndOngoing(beenPaid: String,
                                                    howMuchPaid: String,
                                                    date: DayMonthYear,
                                                    howOften: PaymentFrequency,
                                                    monthlyPayDay: Option[String],
                                                    usuallyPaidSameAmount: String,
                                                    payIntoPension: YesNoWithText = YesNoWithText("", None),
                                                    careCostsForThisWork: YesNoWithText = YesNoWithText("", None))
  extends QuestionGroup(CircumstancesStartedEmploymentAndOngoing)

object CircumstancesStartedEmploymentAndOngoing extends QuestionGroup.Identifier {
  val id = s"${CircumstancesReportChanges.id}.g10"
}
