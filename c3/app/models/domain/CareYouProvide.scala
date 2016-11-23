package models.domain

import models._
import models.Whereabouts
import models.MultiLineAddress
import models.NationalInsuranceNumber
import models.yesNo.{YesNoMandWithAddress, RadioWithText, YesNoWithDate}
import controllers.Iteration.{Identifier => IterationID}
import utils.helpers.TextLengthHelper

object CareYouProvide extends Identifier(id = "s6")

case class TheirPersonalDetails(title: String = "",
                                firstName: String = "",
                                middleName: Option[String] = None,
                                surname: String = "",
                                nationalInsuranceNumber: Option[NationalInsuranceNumber] = None,
                                dateOfBirth: DayMonthYear = DayMonthYear(None, None, None),
                                relationship: String = "",
                                theirAddress: YesNoMandWithAddress = YesNoMandWithAddress()
                                 ) extends QuestionGroup(TheirPersonalDetails)

object TheirPersonalDetails extends QGIdentifier(id = s"${CareYouProvide.id}.g1")

case class MoreAboutTheCare(spent35HoursCaring: Option[String] = None,
                            otherCarer: Option[String] = None,
                            otherCarerUc: Option[String] = None,
                            otherCarerUcDetails: Option[String] = None
                             ) extends QuestionGroup(MoreAboutTheCare)

object MoreAboutTheCare extends QGIdentifier(id = s"${CareYouProvide.id}.g4") {
  def textMaxLength = TextLengthHelper.textMaxLength("DWPCAClaim//Caree//OtherCarerUcDetails//Answer")
}

