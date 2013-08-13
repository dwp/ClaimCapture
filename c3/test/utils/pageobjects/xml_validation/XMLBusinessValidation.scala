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

    // Internal function used to recursively go through the xPath provided to find value
    def childNode(xml: NodeSeq, children: Array[String]): NodeSeq =
      if (children.size == 0) xml else childNode(xml \ children(0), children.drop(1))

    // Load the XML mapping
    val mapping = XMLBusinessValidation.buildXmlMappingFromFile(xmlMappingFile)
    val listErrors = mutable.MutableList.empty[String]
    // Go through the attributes of the claim and check that there is a corresponding entry in the XML
    claim.map.foreach {
      case (attribute, value) =>
        val xPathNodes = mapping.get(attribute)
        if (xPathNodes != None) {
          val path = xPathNodes.get._1
          val nodes = path.split(">")
          val elementValue = XmlNode(childNode(xml.\\(nodes(0)), nodes.drop(1)))
          if (elementValue.isDefined) {
            if (elementValue doesNotMatch ClaimValue(attribute, value)) listErrors += attribute + " " + path + elementValue.error
          }
          else listErrors += attribute + " " + path + " XML element not found"
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
    val map = mutable.Map.empty[String,Tuple2[String,String]]
    def converter(attribute: String)(path: String)(question:String): Unit = map += (attribute -> Tuple2(path,question))
    FactoryFromFile.buildFromFileLast3Columns(fileName, converter)
    map
  }
}

/**
 * Represents an Xml Node once "cleaned", i.e. trimmed and line returns removed.
 */
class XmlNode(val nodes: NodeSeq) {

  var error = ""

  def matches(claimValue: ClaimValue): Boolean = {

    var index = 0

    if (claimValue.attribute.contains( """_""")) index = claimValue.attribute.split("_")(1).toInt - 1

    val value = XmlNode.prepareElement(nodes(index).text)
    val nodeName = nodes(index).mkString

    def valuesMatching = {
      if (value.matches( """\d{4}-\d{2}-\d{2}[tT]\d{2}:\d{2}:\d{2}""") || nodeName.endsWith("OtherNames>") || nodeName.endsWith("PayerName>")) value.contains(claimValue.value)
      else if (nodeName.endsWith("Line>")) claimValue.value.contains(value)
      else if (nodeName.startsWith("<ClaimantActing")) nodeName.toLowerCase.contains(claimValue.value + ">" + value)
      else value == claimValue.value
    }


    val matching = valuesMatching
    if (!matching)
     error = " value expected: [" + claimValue.value + "] within value read: [" + value + "]"
    matching

  }

  def doesNotMatch(claimValue: ClaimValue): Boolean = !matches(claimValue)

  def size = nodes.size

  def isDefined = nodes.nonEmpty


  override def toString() = nodes.mkString(",")

}


object XmlNode {
  private def prepareElement(elementValue: String) = elementValue.replace("\\n", "").replace("\n", "").replace(" ", "").trim.toLowerCase

  def apply(nodes: NodeSeq) = new XmlNode(nodes)

  //  implicit def fromString(source: String): XmlNode = new XmlNode(prepareElement(source))
}


/**
 * Represents a claimvalue once "cleaned", i.e trimmed and date reformated to yyyy-MM-dd as in the XML.
 * @param value value cleaned from the claim.
 */
class ClaimValue(val attribute: String, val value: String) {

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

  def apply(attribute: String, value: String) = new ClaimValue(attribute, prepareClaimValue(value))

  //  implicit def fromString(source: String): ClaimValue = new ClaimValue(prepareClaimValue(source))
}
