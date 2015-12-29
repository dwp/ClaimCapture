package models.domain

import models._
import models.Whereabouts
import models.MultiLineAddress
import models.NationalInsuranceNumber
import models.yesNo.{YesNoMandWithAddress, RadioWithText, YesNoWithDate}
import controllers.Iteration.{Identifier => IterationID}

case object CareYouProvide extends Section.Identifier {
  val id = "s5"
}

case class TheirPersonalDetails(relationship: String = "",
                                title: String = "",
                                firstName: String = "",
                                middleName: Option[String] = None,
                                surname: String = "",
                                nationalInsuranceNumber: Option[NationalInsuranceNumber] = None,
                                dateOfBirth: DayMonthYear = DayMonthYear(None, None, None),
                                theirAddress: YesNoMandWithAddress = YesNoMandWithAddress()
                                 ) extends QuestionGroup(TheirPersonalDetails)

case object TheirPersonalDetails extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g1"
}


case class MoreAboutTheCare(spent35HoursCaring: String = "") extends QuestionGroup(MoreAboutTheCare)

case object MoreAboutTheCare extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g4"
}

