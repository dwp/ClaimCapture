package services.submission

import models.{MultiLineAddress, DayMonthYear}

object Helper {

  def stringify(value: Option[_]):String = {
    value match {
      case Some(s:String) => s
      case Some(dmy:DayMonthYear) => dmy.toXmlString
      case _ => ""
    }
  }

  def addressXml(addressOption:Option[MultiLineAddress], postcodeOption:Option[String]) = {

    def xml(address:MultiLineAddress) = {
      <Address>
        <gds:Line>{stringify(address.lineOne) }</gds:Line>
        <gds:Line>{stringify(address.lineTwo)}</gds:Line>
        <gds:Line>{stringify(address.lineThree)}</gds:Line>
        <gds:PostCode>{stringify(postcodeOption)}</gds:PostCode>
      </Address>
    }

    addressOption match {
      case Some(address:MultiLineAddress) => xml(address)
      case _ =>  xml(MultiLineAddress(None, None, None))
    }
  }

}
