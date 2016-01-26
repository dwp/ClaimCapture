package models.yesNo

import controllers.mappings.Mappings._

case class OptYesNoWith2Text(answer: Option[String] = None, text1: Option[String] = None, text2: Option[String] = None){
  def text : String={
    answer match{
      case Some("yes") => text1.getOrElse("")
      case Some("no") => text2.getOrElse("")
      case _ => ""
    }
  }
}

object OptYesNoWith2Text {

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

