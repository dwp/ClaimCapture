package xml

import models.{NationalInsuranceNumber, MultiLineAddress, DayMonthYear}

import scala.xml.{Node, Text, Elem, NodeBuffer}
import scala.reflect.ClassTag
import scala.language.implicitConversions

object XMLHelper {

  def stringify(value: Option[_]): String = value match {
    case Some(s: String) => s
    case Some(dmy: DayMonthYear) => dmy.`yyyy-MM-dd`
    case Some(nr: NationalInsuranceNumber) => nr.toXmlString
    case _ => ""
  }

  def postalAddressStructure(addressOption: Option[MultiLineAddress], postcodeOption: Option[String]): NodeBuffer = addressOption match {
    case Some(address:MultiLineAddress) => postalAddressStructure(address, postcodeOption.orNull)
    case _ =>  postalAddressStructure(MultiLineAddress(None, None, None), postcodeOption.orNull)
  }

  def postalAddressStructure(address: MultiLineAddress, postcode: String): NodeBuffer = {
    <gds:Line>{address.lineOne.orNull}</gds:Line>
    <gds:Line>{address.lineTwo.orNull}</gds:Line>
    <gds:Line>{address.lineThree.orNull}</gds:Line>
    <gds:PostCode>{postcode}</gds:PostCode>
  }

  def moneyStructure(amount: String) = {
    <Currency>GBP</Currency>
    <Amount>{amount}</Amount>
  }

  def optional[T](option: Option[T], elem: Elem )(implicit classTag:ClassTag[T]): Elem = option match {
    case Some(o) => addChild(elem, Some(Text(stringify(option))))
    case _ => elem
  }

  def addChild(n: Node, newChild: Option[Node]) = n match {
    case Elem(prefix, label, attribs, scope, child @ _*) =>
      Elem(prefix, label, attribs, scope, true, (newChild match { case Some(n) => child ++ newChild case _ => child}) : _*)
    case _ => <error>failed adding children</error>
  }

  implicit def attachToNode(elem: Elem) = new {
    def +++[T](option: Option[T])(implicit classTag: ClassTag[T]): Elem = {
      optional(option, elem)
    }
  }
}