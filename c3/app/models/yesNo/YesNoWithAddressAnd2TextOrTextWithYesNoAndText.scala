package models.yesNo

import models.MultiLineAddress

case class YesNoWithAddressAnd2TextOrTextWithYesNoAndText(answer: String = "", employerName:Option[String] = None, address: Option[MultiLineAddress] = None, postCode: Option[String] = None, text1a: Option[String] = None, text2a: Option[String] = None, answer2: Option[String] = None, text2b: Option[String] = None)

object YesNoWithAddressAnd2TextOrTextWithYesNoAndText {
  def validateNameOnSpecifiedAnswer(input: YesNoWithAddressAnd2TextOrTextWithYesNoAndText, requiredAnswer: String): Boolean = input.answer match {
    case s if s == requiredAnswer => input.employerName.isDefined
    case _ => true
  }

  def validateAddressOnSpecifiedAnswer(input: YesNoWithAddressAnd2TextOrTextWithYesNoAndText, requiredAnswer: String): Boolean = input.answer match {
    case s if s == requiredAnswer => input.address.isDefined
    case _ => true
  }

  def validateAddressLine1OnSpecifiedAnswer(input: YesNoWithAddressAnd2TextOrTextWithYesNoAndText, requiredAnswer: String): Boolean = input.answer match {
    case s if s == requiredAnswer => input.address.isDefined && input.address.get.lineOne.isDefined
    case _ => true
  }

  def validateAddressLine2OnSpecifiedAnswer(input: YesNoWithAddressAnd2TextOrTextWithYesNoAndText, requiredAnswer: String): Boolean = input.answer match {
    case s if s == requiredAnswer => input.address.isDefined && input.address.get.lineTwo.isDefined
    case _ => true
  }

  def validatePostcodeOnSpecifiedAnswer(input: YesNoWithAddressAnd2TextOrTextWithYesNoAndText, requiredAnswer: String): Boolean = input.answer match {
    case s if s == requiredAnswer => input.postCode.isDefined
    case _ => true
  }

  def validateText2OnSpecifiedAnswer(input: YesNoWithAddressAnd2TextOrTextWithYesNoAndText, requiredAnswer: String): Boolean = input.answer match {
    case s if s == requiredAnswer => input.text2a.isDefined
    case _ => true
  }

  def validateAnswer2OnSpecifiedAnswer(input: YesNoWithAddressAnd2TextOrTextWithYesNoAndText, requiredAnswer: String): Boolean = input.answer match {
    case s if s == requiredAnswer => input.answer2.isDefined
    case _ => true
  }

  def validateAnswerNotEmpty(input: YesNoWithAddress): Boolean = input.answer.nonEmpty
}
