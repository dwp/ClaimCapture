package models.yesNo

import controllers.Mappings._
import models.MultiLineAddress

case class YesNoWithAddressAnd2TextOrTextWithYesNo(answer: String = "", address: Option[MultiLineAddress] = None, postCode: Option[String] = None, text1a: Option[String] = None, text1b: Option[String] = None, text2: Option[String] = None, answer2: Option[String] = None)

object YesNoWithAddressAnd2TextOrTextWithYesNo {
  def validateAddressLine1OnSpecifiedAnswer(input: YesNoWithAddressAnd2TextOrTextWithYesNo, requiredAnswer: String): Boolean = input.answer match {
    case s if (s == requiredAnswer) => input.address.isDefined && input.address.get.lineOne.isDefined
    case _ => true
  }

  def validateAddressLine2OnSpecifiedAnswer(input: YesNoWithAddressAnd2TextOrTextWithYesNo, requiredAnswer: String): Boolean = input.answer match {
    case s if (s == requiredAnswer) => input.address.isDefined && input.address.get.lineTwo.isDefined
    case _ => true
  }

  def validatePostcodeOnSpecifiedAnswer(input: YesNoWithAddressAnd2TextOrTextWithYesNo, requiredAnswer: String): Boolean = input.answer match {
    case s if (s == requiredAnswer) => input.postCode.isDefined
    case _ => true
  }

  def validateText2OnSpecifiedAnswer(input: YesNoWithAddressAnd2TextOrTextWithYesNo, requiredAnswer: String): Boolean = input.answer match {
    case s if (s == requiredAnswer) => input.text2.isDefined
    case _ => true
  }

  def validateAnswer2OnSpecifiedAnswer(input: YesNoWithAddressAnd2TextOrTextWithYesNo, requiredAnswer: String): Boolean = input.answer match {
    case s if (s == requiredAnswer) => input.answer2.isDefined
    case _ => true
  }

  def validateAnswerNotEmpty(input: YesNoWithAddress): Boolean = !input.answer.isEmpty
}