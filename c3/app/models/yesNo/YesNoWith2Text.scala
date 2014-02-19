package models.yesNo

import controllers.Mappings._

case class YesNoWith2Text(answer: String = "", text1: Option[String] = None, text2: Option[String] = None)

object YesNoWith2Text {

  def validateText1OnYes (input: YesNoWith2Text) : Boolean = input.answer match {
    case `yes` => input.text1.isDefined
    case `no` => true
  }

  def validateText1OnNo (input: YesNoWith2Text) : Boolean = input.answer match {
    case `yes` => true
    case `no` => input.text1.isDefined
  }

  def validateText2OnYes (input: YesNoWith2Text) : Boolean = input.answer match {
    case `yes` => input.text2.isDefined
    case `no` => true
  }

  def validateText2OnNo (input: YesNoWith2Text) : Boolean = input.answer match {
    case `yes` => true
    case `no` => input.text2.isDefined
  }
}