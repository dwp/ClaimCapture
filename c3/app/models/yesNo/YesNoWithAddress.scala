package models.yesNo

import controllers.Mappings._
import models.MultiLineAddress

case class YesNoWithAddress(answer: Option[String] = None, address: Option[MultiLineAddress] = None, postCode: Option[String] = None)

object YesNoWithAddress {

  def validateOnNo(input: YesNoWithAddress): Boolean = input.answer match {
    case Some(`no`) => input.address.isDefined
    case _ => true
  }

  def validateAnswerNotEmpty(input: YesNoWithAddress): Boolean = !input.answer.isEmpty
}