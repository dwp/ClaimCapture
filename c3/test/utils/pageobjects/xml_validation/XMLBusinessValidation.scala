package utils.pageobjects.xml_validation

import scala.collection.mutable
import utils.pageobjects.{ClaimScenario, FactoryFromFile}
import scala.xml.{Elem, XML}

/**
* Validates that an XML contains all the relevant data that was provided in a Claim.
* The mapping claim's attributes to XML xPath is defined in a configuration file.
* @author Jorge Migueis
*         Date: 23/07/2013
*/
class XMLBusinessValidation(xmlMappingFile:String = "/ClaimScenarioXmlMapping.csv") {

  def validateXMLClaim(claim:ClaimScenario, xmlString: String):Unit  = validateXMLClaim(claim, XML.loadString(xmlString))

  def validateXMLClaim(claim:ClaimScenario, xml: Elem):Unit = {
    val mapping = XMLBusinessValidation.buildXmlMappingFromFile(xmlMappingFile)
    claim.map.foreach { case (attribute,value) => println(attribute) }
  }

}

object XMLBusinessValidation {

  def buildXmlMappingFromFile(fileName: String) = {
    val map = mutable.Map.empty[String,Array[String]]
    def converter(name: String)(value: String): Unit = map += (name -> value.split(">"))
    FactoryFromFile.buildFromFile(fileName, converter)
    map
  }



}
