package models.yesNo

import controllers.Mappings._
import models.MultiLineAddress

case class YesNoWithAddress(answer: String = "", address: Option[MultiLineAddress] = None, postCode: Option[String] = None)

object YesNoWithAddress {

  def validateOnNo(input: YesNoWithAddress): Boolean = input.answer match {
    case `yes` => true
    case `no` =>
      input.address.isDefined
  }

  def validateAnswerNotEmpty(input: YesNoWithAddress): Boolean = !input.answer.isEmpty
}