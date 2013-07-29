package utils.pageobjects.xml_validation

import scala.collection.mutable
import utils.pageobjects.{ClaimScenario, FactoryFromFile}
import scala.xml.{NodeSeq, Elem, XML}

/**
* Validates that an XML contains all the relevant data that was provided in a Claim.
* The mapping claim's attributes to XML xPath is defined in a configuration file.
* @author Jorge Migueis
*         Date: 23/07/2013
*/
class XMLBusinessValidation(xmlMappingFile:String = "/ClaimScenarioXmlMapping.csv") {

  def validateXMLClaim(claim:ClaimScenario, xmlString: String):List[String] = validateXMLClaim(claim, XML.loadString(xmlString))

  def validateXMLClaim(claim:ClaimScenario, xml: Elem) = {

    // Used to recursively go through the xPath provided to find value
    def childNode(xml:NodeSeq, children:Array[String]):NodeSeq =
      if (children.size == 0)  xml else childNode(xml \children(0) , children.drop(1))

    val mapping = XMLBusinessValidation.buildXmlMappingFromFile(xmlMappingFile)
    val listErrors = mutable.MutableList.empty[String]
    claim.map.foreach { case (attribute,value) =>
      val xPathNodes = mapping.get(attribute)
      if (xPathNodes != None) {
        val nodes = xPathNodes.get
        val elementValue = childNode(xml.\\(nodes(0)),nodes.drop(1)).text
        if ( value.toLowerCase  != elementValue.toLowerCase) listErrors += attribute + " " + nodes.mkString(">") + " value expected: [" + value + "] value read: [" + elementValue + "]"
      }
    }
    listErrors.toList
  }

}

/**
 * Contains method to read XML mapping file.
 */
object XMLBusinessValidation {

  def buildXmlMappingFromFile(fileName: String) = {
    val map = mutable.Map.empty[String,Array[String]]
    def converter(name: String)(value: String): Unit = map += (name -> value.split(">"))
    FactoryFromFile.buildFromFile(fileName, converter)
    map
  }



}
