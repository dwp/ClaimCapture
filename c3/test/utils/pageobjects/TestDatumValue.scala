package utils.pageobjects

/**
 * TODO write description
 * @author Jorge Migueis
 *         Date: 09/09/2013
 */
class TestDatumValue (val attribute: String, val value: String, val question: String) {

  override def toString = question + " - " + attribute + ": " + value
}
