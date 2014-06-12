package utils.pageobjects.xml_validation

import scala.xml.{Node, Elem}
import utils.pageobjects.{TestDatumValue, PageObjectException, TestData}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import scala.language.postfixOps
import play.api.i18n.{MMessages => Messages}

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

  override def objValue(attribute: String, value: String, question: String) = {
    CircValue(attribute,value,question)
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

        val node = theNodes(index)
        val value = XMLValidationNode.prepareElement(node.text)
        val nodeName = node.mkString

        def valuesMatching: Boolean = {
          if (value.matches( """\d{4}-\d{2}-\d{2}[tT]\d{2}:\d{2}:\d{2}""") || nodeName.endsWith("OtherNames>")) value.contains(claimValue.value)
          else if (nodeName.startsWith(EvidenceListNode) && ignoreQuestions(claimValue)) true
          else if (nodeName.startsWith(EvidenceListNode) && (claimValue.attribute.contains("CircumstancesEmploymentChangeUsuallyPaidSameAmount"))) {
            if (iteration == 0 ) value.matches(".*doyouusuallygetthesameamounteach[^=]*=" + claimValue.value +".*") else true
          }
          else if (nodeName.startsWith(EvidenceListNode)) {
            value.contains(claimValue.question + "=" + (
              claimValue.value match {
                case "fourWeekly" => Messages("reportChanges.fourWeekly")
                case "everyWeek" => Messages("reportChanges.everyWeek")
                case "yourName" => Messages("reportChanges.yourName")
                case "partner" => Messages("reportChanges.partner")
                case "bothNames" => Messages("reportChanges.bothNames")
                case "onBehalfOfYou" => Messages("reportChanges.onBehalfOfYou")
                case "allNames" => Messages("reportChanges.allNames")
                case _ => claimValue.value
            }))
          }
          else if (nodeName.endsWith("Line>")) claimValue.value.contains(value)
          else if (nodeName.startsWith(DeclarationNode)) valuesMatchingForNodes(claimValue, node)
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

  def valuesMatchingForNodes(claimValue:TestDatumValue, node:Node):Boolean  = {
    answerText(node, "DeclarationQuestion",claimValue.question).contains(claimValue.value)
  }

  def answerText(node:Node,questionTag:String,questionLabel:String) = {
    XMLValidationNode.prepareElement(((node \\ questionTag).filter { n => XMLValidationNode.prepareElement(n \\ "QuestionLabel" text) == questionLabel } \\ "Answer").text)
  }
  private def ignoreQuestions(claimValue: TestDatumValue) = {
    val questions = Seq (
      "BreaksInCareSummaryAdditionalBreaks", "BreaksInCareWhereWasThePersonYouCareFor",
      "BreaksInCareWhereWereYou"
    )
    val answers = Seq ("yes", "somewhereelse")

    questions.contains(claimValue.attribute) && answers.contains(claimValue.value.toLowerCase)
  }

}

class CircValue(attribute: String, value: String, question: String) extends TestDatumValue(attribute, value, question) {}

object CircValue {

  private def prepareQuestion(question: String) = question.replace("\\n", "").replace("\n", "").replace(" ", "").trim.toLowerCase

  private def prepareCircValue(claimValue: String, attribute:String) = {

    val cleanValue = claimValue.replace("\\n", "").replace(" ", "").replace("&", "").trim.toLowerCase

    if (cleanValue.contains("/")) {
      val date = DateTime.parse(cleanValue, DateTimeFormat.forPattern("dd/MM/yyyy"))
      date.toString(DateTimeFormat.forPattern("dd-MM-yyyy"))
    } else
      cleanValue
  }

  def apply(attribute: String, value: String, question: String) = new CircValue(attribute, prepareCircValue(value,attribute), prepareQuestion(question))
}