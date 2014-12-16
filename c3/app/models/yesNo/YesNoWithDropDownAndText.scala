package models.yesNo

import controllers.mappings.Mappings._

case class YesNoWithDropDownAndText(answer: Option[String], dropDownValue: Option[String], text: Option[String])

object YesNoWithDropDownAndText {

  def validate(input: YesNoWithDropDownAndText): Boolean = input.answer.getOrElse("don't know") match {
    case `yes` => input.dropDownValue.isDefined
    case `no` => true
    case "don't know" => true
    case _ => false
  }
}