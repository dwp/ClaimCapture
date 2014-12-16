package models.yesNo

import controllers.mappings.Mappings._

case class YesNoWithDropDown(answer: String, dropDownValue: Option[String])

object YesNoWithDropDown {

  def validate(input: YesNoWithDropDown): Boolean = input.answer match {
    case `yes` => input.dropDownValue.isDefined
    case `no` => true
  }
}