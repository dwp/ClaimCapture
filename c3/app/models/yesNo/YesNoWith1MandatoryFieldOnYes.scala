package models.yesNo

import utils.helpers.YesNoHelpers

/**
 * Created by tudormalene on 23/09/15.
 */
case class YesNoWith1MandatoryFieldOnYes[A](answer: String = "", field: Option[A] = None)

object YesNoWith1MandatoryFieldOnYes {

  def validateFieldOnYes[A](input: YesNoWith1MandatoryFieldOnYes[A]): Boolean = YesNoHelpers.validateFieldOnYes(input.answer)(input.field.isDefined)

  def validateFieldOnNo[A](input: YesNoWith1MandatoryFieldOnYes[A]): Boolean = YesNoHelpers.validateFieldOnNo(input.answer)

}