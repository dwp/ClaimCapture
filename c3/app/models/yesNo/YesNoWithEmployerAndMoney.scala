package models.yesNo


import controllers.mappings.Mappings._
import models.{PaymentFrequency, MultiLineAddress}


/**
 * Created by neddakaltcheva on 2/25/14.
 */
case class YesNoWithEmployerAndMoney(answer: String, howMuch: Option[String] = None, howOften: Option[PaymentFrequency] = None,
                                     employersName: Option[String] = None, address: Option[MultiLineAddress] = None, postCode: Option[String] = None)

object YesNoWithEmployerAndMoney {

  def validateEmployerNameOnYes(input: YesNoWithEmployerAndMoney): Boolean = input.answer match {
    case `yes` => input.employersName.isDefined
    case _ => true
  }

  def validateHowMuchOnYes(input: YesNoWithEmployerAndMoney): Boolean = input.answer match {
    case `yes` => input.howMuch.isDefined
    case _ => true
  }
 // def validateAnswerNotEmpty(input: YesNoWithEmployerAndMoney): Boolean = !input.answer.isEmpty
}