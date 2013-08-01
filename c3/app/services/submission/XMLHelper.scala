package services.submission

import models.{NationalInsuranceNumber, MultiLineAddress, DayMonthYear}
import models.domain.{Section, QuestionGroup}

object XMLHelper {

  def stringify(value: Option[_]):String = {
    value match {
      case Some(s:String) => s
      case Some(dmy:DayMonthYear) => dmy.`yyyy-MM-dd`
      case Some(nr:NationalInsuranceNumber) => nr.toXmlString
      case _ => ""
    }
  }

  def postalAddressStructure(addressOption:Option[MultiLineAddress], postcodeOption:Option[String]) = {

    def xml(address:MultiLineAddress) = {
      <gds:Line>{address.lineOne.orNull}</gds:Line>
      <gds:Line>{address.lineTwo.orNull}</gds:Line>
      <gds:Line>{address.lineThree.orNull}</gds:Line>
      <gds:PostCode>{postcodeOption.getOrElse("")}</gds:PostCode>
    }

    addressOption match {
      case Some(address:MultiLineAddress) => xml(address)
      case _ =>  xml(MultiLineAddress(None, None, None))
    }
  }

  def questionGroup[T](section: Section, qi: QuestionGroup.Identifier) = {
    section.questionGroup(qi).asInstanceOf[Option[T]]
  }

}
