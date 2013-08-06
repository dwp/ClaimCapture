package utils.pageobjects.xml_validation

import scala.collection.mutable
import utils.pageobjects.{PageObjectException, ClaimScenario, FactoryFromFile}
import scala.xml.{NodeSeq, Elem, XML}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import scala.language.implicitConversions
import scala.util.matching.Regex


/**
 * Validates that an XML contains all the relevant data that was provided in a Claim.
 * The mapping claim's attributes to XML xPath is defined in a configuration file.
 * @author Jorge Migueis
 *         Date: 23/07/2013
 */
class XMLBusinessValidation(xmlMappingFile: String = "/ClaimScenarioXmlMapping.csv") {

  def validateXMLClaim(claim: ClaimScenario, xmlString: String, throwException: Boolean): List[String] = validateXMLClaim(claim, XML.loadString(xmlString), throwException)

  /**
   * Performs the validation of a claim XML against the data used to populate the claim forms.
   * @param claim Original claim used to go through the screens and now used to validate XML.
   * @param xml  XML that needs to be validated against the provided claim.
   * @param throwException Specify whether the validation should throw an exception if mismatches are found.
   * @return List of errors found. The list is empty if no errors were found.
   */
  def validateXMLClaim(claim: ClaimScenario, xml: Elem, throwException: Boolean) = {

    // Used to recursively go through the xPath provided to find value
    def childNode(xml: NodeSeq, children: Array[String]): NodeSeq =
      if (children.size == 0) xml else childNode(xml \ children(0).replace("...",""), children.drop(1))

    val mapping = XMLBusinessValidation.buildXmlMappingFromFile(xmlMappingFile)
    val listErrors = mutable.MutableList.empty[String]
    claim.map.foreach {
      case (attribute, value) =>
        val xPathNodes = mapping.get(attribute)
        if (xPathNodes != None) {
          val path = xPathNodes.get
          val nodes = path.split(">")
          val elementValue: XmlNode = if (path.endsWith("...")) childNode(xml.\\(nodes(0)), nodes.drop(1)).toString() else childNode(xml.\\(nodes(0)), nodes.drop(1)).text
          val expectedValue: ClaimValue = value
          if (elementValue doesNotMatch expectedValue)
            listErrors += attribute + " " + nodes.mkString(">") + " value expected: [" + expectedValue + "] value read: [" + elementValue + "]"
        }
    }
    if (listErrors.nonEmpty && throwException) throw new PageObjectException("XML validation failed", listErrors.toList)
    listErrors.toList
  }
}


/**
 * Contains method to read XML mapping file.
 */
object XMLBusinessValidation {

  def buildXmlMappingFromFile(fileName: String) = {
    val map = mutable.Map.empty[String, String]
    def converter(attribute: String)(path: String): Unit = map += (attribute -> path) //.split(">")
    FactoryFromFile.buildFromFile(fileName, converter)
    map
  }
}

/**
 * Represents an Xml Node once "cleaned", i.e. trimmed and line returns removed.
 * @param value value of node.
 */
class XmlNode(val value: String) {

  def matches(claimValue: ClaimValue): Boolean = {
    if (value.matches("""\d{4}-\d{2}-\d{2}[tT]\d{2}:\d{2}:\d{2}""")) value.contains(claimValue.value)
    else if (value.matches(".*>.*"))  {
      println(value)
      value.matches(".*" + "yes</[^>]*" + value + ".*")
    } else value == claimValue.value
  }

  def doesNotMatch(claimValue: ClaimValue): Boolean = !matches(claimValue)

  override def toString() = value

}


object XmlNode {
  private def prepareElement(elementValue: String) = elementValue.replace("\\n", "").replace("\n", "").replace(" ", "").trim.toLowerCase

  implicit def fromString(source: String): XmlNode = new XmlNode(prepareElement(source))
}


/**
 * Represents a claimvalue once "cleaned", i.e trimmed and date reformated to yyyy-MM-dd as in the XML.
 * @param value value cleaned from the claim.
 */
class ClaimValue(val value: String) {

  override def toString() = value
}


object ClaimValue {
  private def prepareClaimValue(claimValue: String) = {
    val cleanValue = claimValue.replace("\\n", "").replace(" ", "").trim.toLowerCase
    if (cleanValue.contains("/")) {
      val date = DateTime.parse(cleanValue, DateTimeFormat.forPattern("dd/MM/yyyy"))
      date.toString(DateTimeFormat.forPattern("yyyy-MM-dd"))
    } else cleanValue
  }

  implicit def fromString(source: String): ClaimValue = new ClaimValue(prepareClaimValue(source))
}
