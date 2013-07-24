package utils.pageobjects.xml_validation

import scala.collection.mutable
import utils.pageobjects.{ClaimScenario, FactoryFromFile}

/**
* Validates that an XML contains all the relevant data that was provided in a Claim.
* The mapping claim's attributes to XML xPath is defined in a configuration file.
* @author Jorge Migueis
*         Date: 23/07/2013
*/
class XMLBusinessValidation(xmlMappingFile:String = "/ClaimScenarioXmlMapping.csv") {

  def validateXMLClaim(claim:ClaimScenario) {
     val mapping = XMLBusinessValidation.buildXmlMappingFromFile(xmlMappingFile)


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
