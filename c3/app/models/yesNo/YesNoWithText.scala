package models.yesNo

import controllers.Mappings._

case class YesNoWithText(answer: String, text: Option[String])

object YesNoWithText {

  def validate(input: YesNoWithText): Boolean = input.answer match {
    case `yes` => input.text.isDefined
    case `no` => true
  }

  def validateOnNo(input: YesNoWithText): Boolean = input.answer match {
    case `yes` => true
    case `no` => input.text.isDefined
  }
}