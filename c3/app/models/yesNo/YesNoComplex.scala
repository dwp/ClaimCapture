package models.yesNo

/**
 * Created by neddakaltcheva on 2/14/14.
 */
import controllers.mappings.Mappings._

case class YesNoComplex(answer: String = "", address: YesNoWithAddress = YesNoWithAddress(None, None, None))

object YesNoComplex {

  def validateOnYes(input: YesNoComplex): Boolean = input.answer match {
    case `yes` => input.address.answer.isDefined
    case `no` => true
  }

  def validateOnNo(input: YesNoComplex): Boolean = input.answer match {
    case `yes` => true
    case `no` => input.address.answer.isDefined
  }

  def validateAnswerNotEmpty(input: YesNoComplex): Boolean = !input.answer.isEmpty
}