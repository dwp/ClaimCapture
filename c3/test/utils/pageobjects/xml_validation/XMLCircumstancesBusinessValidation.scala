package utils.pageobjects.xml_validation

import scala.xml.Elem
import utils.pageobjects.{TestDatumValue, PageObjectException, TestData}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/**
 * Validates that an XML contains all the relevant data that was provided in a Claim.
 * The mapping claim's attributes to XML xPath is defined in a configuration file.
 * @author Jorge Migueis
 *         Date: 23/07/2013
 */
class XMLCircumstancesBusinessValidation extends XMLBusinessValidation  {
  val mappingFilename = "/CircumstancesXmlMapping.csv"
  def createXMLValidationNode = (xml: Elem, nodes: Array[String]) => new CircumstancesXmlNode(xml,nodes)

  /**
   * Performs the validation of a claim XML against the data used to populate the claim forms.
   * @param claim Original claim used to go through the screens and now used to validate XML.
   * @param xml  XML that needs to be validated against the provided claim.
   * @param throwException Specify whether the validation should throw an exception if mismatches are found.
   * @return List of errors found. The list is empty if no errors were found.
   */
  def validateXMLClaim(claim: TestData, xml: Elem, throwException: Boolean): List[String] = {
    super.validateXMLClaim(claim, xml, throwException, mappingFilename, createXMLValidationNode)
  }
}

/**
 * Represents an Xml Node once "cleaned", i.e. trimmed and line returns removed.
 */
class CircumstancesXmlNode(xml: Elem, path:Array[String]) extends XMLValidationNode(xml, path) {

  def matches(claimValue: TestDatumValue): Boolean = {
    try {
      val nodeStart = theNodes(0).mkString

      val isARepeatableNode = !nodeStart.contains(EvidenceListNode) && !nodeStart.contains(DeclarationNode)

      val isRepeatedAttribute = claimValue.attribute.contains( """_""")

      val iteration = if (isRepeatedAttribute) claimValue.attribute.split("_")(1).toInt - 1 else 0

      if (!isARepeatableNode && iteration > 0 && !nodeStart.contains(EvidenceListNode)) true
      else {
        val index = if (isRepeatedAttribute && isARepeatableNode) iteration else 0

        val value = XMLValidationNode.prepareElement(theNodes(index).text)
        val nodeName = theNodes(index).mkString
        def valuesMatching: Boolean = {
          if (value.matches( """\d{4}-\d{2}-\d{2}[tT]\d{2}:\d{2}:\d{2}""") || nodeName.endsWith("OtherNames>")) value.contains(claimValue.value) 
          else if (nodeName.startsWith(EvidenceListNode)) {
            value.contains(claimValue.question + "=" + claimValue.value)
          }
          else if (nodeName.endsWith("gds:Line>")) claimValue.value.contains(value)
          else if (nodeName.startsWith(DeclarationNode)) value.contains(claimValue.question + claimValue.value)
          else value == claimValue.value
        }

        val matching = valuesMatching

        if (!matching)
          error = " value expected: [" + (if (nodeName.startsWith(EvidenceListNode)) claimValue.question + "=" + claimValue.value else claimValue.value) + "] within value read: [" + value + "]"
        matching
      }
    }
    catch {
      case e: IndexOutOfBoundsException => throw new PageObjectException("XML Validation failed" + this.toString() + " - " + claimValue.attribute)
    }
  }

}

class CircValue(attribute: String, value: String, question: String) extends TestDatumValue(attribute, value, question) {}

object CircValue {

  private def prepareQuestion(question: String) = question.replace("\\n", "").replace("\n", "").replace(" ", "").trim.toLowerCase

  private def prepareCircValue(claimValue: String, attribute:String) = {
    val cleanValue = claimValue.replace("\\n", "").replace(" ", "").trim.toLowerCase

    if (cleanValue.contains("/")) {
      val date = DateTime.parse(cleanValue, DateTimeFormat.forPattern("dd/MM/yyyy"))
      date.toString(DateTimeFormat.forPattern("yyyy-MM-dd"))
    } else cleanValue
  }

  def apply(attribute: String, value: String, question: String) = new CircValue(attribute, prepareCircValue(value,attribute), prepareQuestion(question))
}