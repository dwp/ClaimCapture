package utils.pageobjects

import scala.language.dynamics

/**
 * Represents a full claim. Fill only the responses you need for the your specific test scenario.
 * The class is dynamic. It builds of attributes dynamically. To create a new attribute and provide a value, just
 * write:<br>
 *  <i>val claim = new ClaimScenario</i><br>
 *  <i>claim.[A New Name Of Attribute] =  [value]</i><br>
 * @author Jorge Migueis
 *         Date: 10/07/2013
 */
class ClaimScenario extends Dynamic {

  var map = Map.empty[String,String]

  def selectDynamic(name: String) =
    map get name getOrElse null

  def updateDynamic(name: String)(value: String) {
    map += name -> value
  }

}
