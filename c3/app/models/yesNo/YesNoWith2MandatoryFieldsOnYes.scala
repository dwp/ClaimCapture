package models.yesNo

import utils.helpers.YesNoHelpers._

/**
 * Created by tudormalene on 23/09/15.
 */
case class YesNoWith2MandatoryFieldsOnYes[A, B](answer: String = "", field1: Option[A] = None, field2: Option[B] = None)

object YesNoWith2MandatoryFieldsOnYes {

  def validateField1OnYes[A](input: YesNoWith2MandatoryFieldsOnYes[A, _]): Boolean = validateFieldOnYes(input.answer)(input.field1.isDefined)

  def validateField1OnNo[A](input: YesNoWith2MandatoryFieldsOnYes[A, _]): Boolean = validateFieldOnNo(input.answer)

  def validateField2OnYes[B](input: YesNoWith2MandatoryFieldsOnYes[_, B]): Boolean = validateFieldOnYes(input.answer)(input.field2.isDefined)

  def validateField2OnNo[B](input: YesNoWith2MandatoryFieldsOnYes[_, B]): Boolean = validateFieldOnNo(input.answer)

}
