package utils.helpers

import controllers.mappings.Mappings._

/**
 * Created by tudormalene on 24/09/15.
 */
object YesNoHelpers {

  def validateFieldOnYes(answer: String)(validate: =>Boolean ): Boolean = answer match {
    case `yes` => validate
    case `no` => true
  }

  def validateFieldOnNo(answer: String): Boolean = answer match {
    case `yes` => false
    case `no` => true
  }

}
