package models.yesNo

import controllers.Mappings._
import models.MultiLineAddress

case class YesNoWithAddress(answer: String = "", address: Option[MultiLineAddress] = None, postCode: Option[String] = None)

object YesNoWithAddress {

  def validateOnYes(input: YesNoWithAddress): Boolean = input.answer match {
    case `yes` =>
      input.address.isDefined
      input.postCode.isDefined
    case `no` => true
  }

  def validateOnNo(input: YesNoWithAddress): Boolean = input.answer match {
    case `yes` => true
    case `no` =>
      input.address.isDefined
      input.postCode.isDefined
  }

  def validateAnswerNotEmpty(input: YesNoWithAddress): Boolean = !input.answer.isEmpty
}