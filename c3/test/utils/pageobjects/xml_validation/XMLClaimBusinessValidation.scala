package utils.pageobjects.xml_validation

import utils.pageobjects.{TestDatumValue, PageObjectException, TestData}
import scala.xml.{Node, Elem}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import scala.language.implicitConversions
import XMLValidationNode.prepareElement
import scala.annotation.tailrec
import scala.language.postfixOps

/**
 * Validates that an XML contains all the relevant data that was provided in a Claim.
 * The mapping claim's attributes to XML xPath is defined in a configuration file.
 * @author Jorge Migueis
 *         Date: 23/07/2013
 */
class XMLClaimBusinessValidation extends XMLBusinessValidation {
  val mappingFilename = "/ClaimScenarioXmlMapping.csv"

  def createXMLValidationNode = (xml: Elem, nodes: Array[String]) => new ClaimXmlNode(xml, nodes)

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
class ClaimXmlNode(xml: Elem, path: Array[String]) extends XMLValidationNode(xml, path) {

  def matches(claimValue: TestDatumValue): Boolean = {


    try {
      val nodeStart = theNodes(0).mkString

      val isARepeatableNode = !nodeStart.contains(EvidenceListNode) && !nodeStart.contains(DeclarationNode) && !nodeStart.contains("<Employed>") && !nodeStart.contains("<BreaksSinceClaim>")

      val isRepeatedAttribute = claimValue.attribute.contains( """_""")

      val iteration = if (isRepeatedAttribute) claimValue.attribute.split("_")(1).toInt - 1 else 0

      if (!isARepeatableNode && iteration > 0 && !nodeStart.contains(EvidenceListNode) || iteration > 0 && isUniqueValueInRepetitions(claimValue)){
        true
      }
      else {

        val matches = anyMatches(theNodes.iterator,claimValue,res = false)

        if (!matches){
          error = " value expected: [" + (if (theNodes.mkString.startsWith(EvidenceListNode)) claimValue.question + "=" + claimValue.value else claimValue.value) + "] within value read: [" + theNodes.text + "]"
        }

        matches
      }
    }
    catch {
      case e: IndexOutOfBoundsException => throw new PageObjectException("XML Validation failed " + this.toString() + " - " + claimValue.attribute + " Cause:"+e.getCause+" theNode:"+theNodes)//Trace:"+e.getStackTraceString)
    }
  }


  private def isUniqueValueInRepetitions(claimValue:TestDatumValue):Boolean = {
    //We will test if this value is collected in repeated values like breaks, employment, time abroad, but it's saved in a non-repetition xml node
    //If the iteration is 1, these values are going to be tested, but they don't need to be testes further this point.
    claimValue.attribute.startsWith("AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare") || claimValue.attribute.startsWith("AboutYouHaveYouBeenEmployedAtAnyTime")
  }
  @tailrec
  private def anyMatches(iterator:Iterator[Node],claimValue:TestDatumValue,res:Boolean):Boolean = {
    if (iterator.hasNext) {
      val node = iterator.next()
      anyMatches(iterator,claimValue, res || valuesMatching(claimValue,node))
    }
    else res
  }

  def valuesMatching(claimValue:TestDatumValue,node:Node): Boolean = {
    val nodeName = node.mkString
    val value = prepareElement(node.text)

    if (nodeName.endsWith("DateTime>") || nodeName.endsWith("OtherNames>") || nodeName.endsWith("PayerName>") || node.mkString.contains("<PensionScheme>")) value.contains(claimValue.value)
    else if (nodeName.endsWith("Line>")) claimValue.value.contains(value)
    else if (nodeName.startsWith("<ClaimantActing")) nodeName.toLowerCase.contains(claimValue.value + ">" + value)
    else if (nodeName.startsWith(DeclarationNode)) claimValue.value == answerText(node, "DeclarationQuestion",claimValue.question)
    else if (nodeName.startsWith(DisclaimerNode))  claimValue.value == answerText(node, "DisclaimerQuestion", claimValue.question)
    else if (nodeName.startsWith(ConsentNode))  claimValue.value == answerText(node, "Consent", claimValue.question)//TODO: If consent is no, check why
    else value == claimValue.value
  }

  def answerText(node:Node,questionTag:String,questionLabel:String) = {
    XMLValidationNode.prepareElement(((node \\ questionTag).filter { n => XMLValidationNode.prepareElement(n \\ "QuestionLabel" text) == questionLabel } \\ "Answer").text)
  }

}

class ClaimValue(attribute: String, value: String, question: String) extends TestDatumValue(attribute, value, question) {}

object ClaimValue {

  private def prepareQuestion(question: String) = question.replace("\\n", "").replace("\n", "").replace(" ", "").trim.toLowerCase

  private def prepareClaimValue(claimValue: String, attribute: String) = {
    val cleanValue = claimValue.replace("\\n", "").replace(" ", "").trim.toLowerCase

    if (cleanValue.contains("/") && !attribute.startsWith("EmploymentLeavingDateP45") && !attribute.startsWith("AboutYouWhenDidYouArriveInYheUK")) {
      val date = DateTime.parse(cleanValue, DateTimeFormat.forPattern("dd/MM/yyyy"))
      date.toString(DateTimeFormat.forPattern("dd-MM-yyy"))
    } else cleanValue
  }

  def apply(attribute: String, value: String, question: String) = new ClaimValue(attribute, prepareClaimValue(value, attribute), prepareQuestion(question))
}