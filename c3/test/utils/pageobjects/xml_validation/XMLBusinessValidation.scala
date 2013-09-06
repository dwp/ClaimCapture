package utils.pageobjects.xml_validation

import utils.pageobjects.TestData
import scala.xml.{Elem, XML}

/**
 * Interface of the XML Business Validation classes.
 * @author Jorge Migueis
 *         Date: 06/09/2013
 */
trait XMLBusinessValidation {

  def validateXMLClaim(data: TestData, xmlString: String, throwException: Boolean): List[String]

  def validateXMLClaim(data: TestData, xml: Elem, throwException: Boolean): List[String]

}
