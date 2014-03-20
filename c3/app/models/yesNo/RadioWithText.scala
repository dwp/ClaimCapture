package models.yesNo

import controllers.Mappings._

/**
 * Created by neddakaltcheva on 3/20/14.
 */

case class RadioWithText(answer: String = "", text: Option[String] = None)

object RadioWithText {
  val somewhereElse = "somewhere else"

  def validateOnOther(input: RadioWithText): Boolean = input.answer match {
    case "somewhere else" => input.text.isDefined
    case _ => true
  }

}
