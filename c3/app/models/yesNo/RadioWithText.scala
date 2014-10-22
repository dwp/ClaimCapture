package models.yesNo

import app.CircsBreaksWhereabouts._

/**
 * Created by neddakaltcheva on 3/20/14.
 */

case class RadioWithText(answer: String = "", text: Option[String] = None)

object RadioWithText {

  def validateOnOther(input: RadioWithText): Boolean = {
    input.answer match {
      case SomewhereElse => input.text.isDefined
      case _ => true
    }
  }

}
