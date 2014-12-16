package models.yesNo

import controllers.mappings.Mappings._

case class YesNoWithText(answer: String = "", text: Option[String] = None)

object YesNoWithText {

  def validateOnYes(input: YesNoWithText): Boolean = input.answer match {
    case `yes` => input.text.isDefined
    case `no` => true
  }

  def validateOnNo(input: YesNoWithText): Boolean = input.answer match {
    case `yes` => true
    case `no` => input.text.isDefined
  }

  /**
   * we are using this function in scenarios where the answer is yes and the field to be validated
   * is not visible based on some other condition
   * @param input
   * @return
   */
  def doNotValidateOnYes(input: YesNoWithText): Boolean = input.answer match {
    case _ => true
  }
}