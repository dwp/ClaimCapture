package models.yesNo

import controllers.Mappings._

case class YesNoWith2Text(answer: String, text1: Option[String], text2: Option[String])

object YesNoWith2Text {


  def validateText(input: YesNoWith2Text, text:Option[String], required:Boolean = true) = {
    input.answer match {
      case `yes` => if(required) text.isDefined else true
      case `no` => true
    }
  }
  


}