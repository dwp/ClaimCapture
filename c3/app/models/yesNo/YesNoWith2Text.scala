package models.yesNo

import controllers.Mappings._

case class YesNoWith2Text(answer: String, text1: Option[String], text2: Option[String])

object YesNoWith2Text {

  def validateOnYes(input: YesNoWith2Text, text1Enabled: Boolean, text2Enabled: Boolean): Boolean = input.answer match {
    case `yes` => (if(text1Enabled) input.text1.isDefined else true) && (if(text2Enabled) input.text2.isDefined else true)
    case `no` => true
  }

  def validateOnNo(input: YesNoWith2Text): Boolean = input.answer match {
    case `yes` => true
    case `no` => input.text1.isDefined && input.text2.isDefined
  }
}