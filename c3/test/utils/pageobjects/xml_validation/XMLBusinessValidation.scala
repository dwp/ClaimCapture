package utils.pageobjects.xml_validation

import utils.pageobjects.{FactoryFromFile, TestData}
import scala.xml.{Elem, XML}
import scala.collection.mutable

/**
 * Interface of the XML Business Validation classes.
 * @author Jorge Migueis
 *         Date: 06/09/2013
 */
abstract class XMLBusinessValidation {

  def validateXMLClaim(data: TestData, xmlString: String, throwException: Boolean): List[String]

  def validateXMLClaim(data: TestData, xml: Elem, throwException: Boolean): List[String]

}


/**
 * Contains method to read XML mapping file.
 */
object XMLBusinessValidation {

  def buildXmlMappingFromFile(fileName: String) = {
    val map = mutable.Map.empty[String, (String, String)]
    def converter(attribute: String)(path: String)(question: String): Unit = map += (attribute -> Tuple2(path, question))
    FactoryFromFile.buildFromFileLast3Columns(fileName, converter)
    map
  }
}
