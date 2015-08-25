package models.domain

import models._
import models.Whereabouts
import models.MultiLineAddress
import models.NationalInsuranceNumber
import models.yesNo.{RadioWithText, YesNoWithDate}
import controllers.Iteration.{Identifier => IterationID}

case object CareYouProvide extends Section.Identifier {
  val id = "s5"
}

case class TheirPersonalDetails(relationship: String = "",
                                title: String = "",
                                titleOther: Option[String] = None,
                                firstName: String = "",
                                middleName: Option[String] = None,
                                surname: String = "",
                                nationalInsuranceNumber: Option[NationalInsuranceNumber] = None,
                                dateOfBirth: DayMonthYear = DayMonthYear(None, None, None),
                                liveAtSameAddressCareYouProvide: String = "") extends QuestionGroup(TheirPersonalDetails)

case object TheirPersonalDetails extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g1"

  def verifyTitleOther(form:TheirPersonalDetails):Boolean = {
    form.title match {
      case "Other" => form.titleOther.isDefined
      case _ => true
    }
  }

}

case class TheirContactDetails(address: MultiLineAddress = MultiLineAddress(), postcode: Option[String] = None) extends QuestionGroup(TheirContactDetails)

case object TheirContactDetails extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g2"
}

case class MoreAboutTheCare(spent35HoursCaring: String = "") extends QuestionGroup(MoreAboutTheCare)

case object MoreAboutTheCare extends QuestionGroup.Identifier {
  val id = s"${CareYouProvide.id}.g4"
}

