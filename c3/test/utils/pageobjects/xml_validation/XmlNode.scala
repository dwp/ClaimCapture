package utils.pageobjects.xml_validation

import scala.xml.{Node, Elem}
import utils.pageobjects.{PageObjectException, TestDatumValue}
import scala.annotation.tailrec
import utils.pageobjects.xml_validation.XMLValidationNode._
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import scala.language.postfixOps

/**
 * Represents an Xml Node once "cleaned", i.e. trimmed and line returns removed.
 */
class XmlNode(xml: Elem, path: Array[String]) extends XMLValidationNode(xml, path) {

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
    claimValue.attribute.startsWith("AboutTheCareYouProvideHaveYouHadAnyMoreBreaksInCare") ||
      claimValue.attribute.startsWith("AboutYouHaveYouBeenEmployedAtAnyTime") || claimValue.attribute.startsWith("AboutYouMoreTripsOutOfGBforMoreThan52WeeksAtATime")
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
//    Logger.debug(nodeName)
    if (attributeForContains(claimValue)) value.contains(claimValue.value)
    else if (nodeName.endsWith("Line>")) claimValue.value.contains(value)
    else if (nodeName.startsWith(DeclarationNode)) answerText(node, "DeclarationQuestion",claimValue.question).contains(claimValue.value)
    else if (nodeName.startsWith(DisclaimerNode))  answerText(node, "DisclaimerQuestion", claimValue.question).contains(claimValue.value)
    else if (nodeName.startsWith(ConsentsNode))  answerText(node, "Consent", claimValue.question).contains(claimValue.value)
    else value == claimValue.value
  }

  private def attributeForContains (claimValue:TestDatumValue):Boolean = {
     val attributes = Seq("StartDate","StartTime","EndDate","EndTime","FirstName","MiddleName", "MiddleNamePersonCareFor", "FirstNamePersonCareFor")
     val claimAttribute = if(claimValue.attribute.contains("_")) claimValue.attribute.split("_")(0) else claimValue.attribute
     attributes.foreach(a => if(claimAttribute.endsWith(a)) return true) // Could have done with contains
     return false
  }

  def answerText(node:Node,questionTag:String,questionLabel:String) = {
    XMLValidationNode.prepareElement(((node \\ questionTag).filter { n => XMLValidationNode.prepareElement(n \\ "QuestionLabel" text) == questionLabel } \\ "Answer").text)
  }
}

class ClaimValue(attribute: String, value: String, question: String) extends TestDatumValue(attribute, value, question) {}

object ClaimValue {

  private def prepareQuestion(question: String) = question.replace("\\n", "").replace("\n", "").replace(" ", "").trim.toLowerCase

  private def prepareClaimValue(claimValue: String, attribute: String) = {
    val cleanValue = claimValue.replace("\\n", "").replace(" ", "").replace("£", "").trim.toLowerCase

    if (cleanValue.contains("/")) {
      val date = DateTime.parse(cleanValue, DateTimeFormat.forPattern("dd/MM/yyyy"))
      date.toString(DateTimeFormat.forPattern("dd-MM-yyy"))
    } else cleanValue
  }

  def apply(attribute: String, value: String, question: String) = new ClaimValue(attribute, prepareClaimValue(value, attribute), prepareQuestion(question))
}