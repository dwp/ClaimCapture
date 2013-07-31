package services.submission

import models.{NationalInsuranceNumber, MultiLineAddress, DayMonthYear}
import models.domain.{Section, QuestionGroup}

object XMLHelper {

  def stringify(value: Option[_]):String = {
    value match {
      case Some(s:String) => s
      case Some(dmy:DayMonthYear) => dmy.toXmlString
      case Some(nr:NationalInsuranceNumber) => nr.toXmlString
      case _ => ""
    }
  }

  def stringifyRequired(value:Option[_]):String = {
   val stringValue = stringify(value)
   if(stringValue == "") "empty" else stringValue
  }

  def postalAddressStructure(addressOption:Option[MultiLineAddress], postcodeOption:Option[String]) = {

    def xml(address:MultiLineAddress) = {
      <gds:Line>{stringify(address.lineOne) }</gds:Line>
      <gds:Line>{stringify(address.lineTwo)}</gds:Line>
      <gds:Line>{stringify(address.lineThree)}</gds:Line>
      <gds:PostCode>{stringify(postcodeOption)}</gds:PostCode>
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
