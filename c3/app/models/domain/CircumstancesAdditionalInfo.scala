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

case class CircumstancesAddressChange(stillCaringMapping: YesNoWithDateAndQs = YesNoWithDateAndQs("", None, None),
                                      newAddress: MultiLineAddress = new MultiLineAddress(),
                                      newPostcode: Option[String] = None,
                                      caredForChangedAddress: YesNoWithText = YesNoWithText("", None),
                                      sameAddress: YesNoWithAddress = YesNoWithAddress("", None),
                                      moreAboutChanges: Option[String] = None) extends QuestionGroup(ContactDetails)

object CircumstancesAddressChange extends QuestionGroup.Identifier {
  val id = s"${CircumstancesReportChanges.id}.g6"
}
