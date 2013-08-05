package models.yesNo

import controllers.Mappings._

case class YesNoWithDropDownAndText(answer: String, dropDownValue: Option[String], text: Option[String])

object YesNoWithDropDownAndText {

  def validate(input: YesNoWithDropDownAndText): Boolean = input.answer match {
    case `yes` => input.dropDownValue.isDefined
    case `no` => true
  }
}