package models.yesNo

import controllers.mappings.Mappings._

case class OptYesNoWithText(answer: Option[String] = None, text: Option[String] = None)

object OptYesNoWithText {

  def validateOnYes(input: OptYesNoWithText): Boolean = input.answer.getOrElse("no") match {
    case `yes` => input.text.isDefined
    case `no` => true
  }

  def validateOnNo(input: OptYesNoWithText): Boolean = input.answer.getOrElse("yes") match {
    case `yes` => true
    case `no` => input.text.isDefined
  }

  /**
   * we are using this function in scenarios where the answer is yes and the field to be validated
   * is not visible based on some other condition
   */
  def doNotValidateOnYes(input: OptYesNoWithText): Boolean = input.answer match {
    case _ => true
  }
}
