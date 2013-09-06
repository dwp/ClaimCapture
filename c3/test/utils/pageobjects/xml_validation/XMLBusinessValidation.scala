package utils.pageobjects.xml_validation

import scala.collection.mutable
import utils.pageobjects.{PageObjectException, ClaimScenario, FactoryFromFile}
import scala.xml.{NodeSeq, Elem, XML}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import scala.language.implicitConversions
import app.{StatutoryPaymentFrequency, PensionPaymentFrequency}

/**
 * Validates that an XML contains all the relevant data that was provided in a Claim.
 * The mapping claim's attributes to XML xPath is defined in a configuration file.
 * @author Jorge Migueis
 *         Date: 23/07/2013
 */
class XMLBusinessValidation(xmlMappingFile: String = "/ClaimScenarioXmlMapping.csv") {

  val errors = mutable.MutableList.empty[String]
  val warnings = mutable.MutableList.empty[String]

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

    // Go through the attributes of the claim and check that there is a corresponding entry in the XML
    claim.map.foreach {
      case (attribute, value) =>
        try {
          val xPathNodesAndQuestion = mapping.get(attribute.split("_")(0))

          if (xPathNodesAndQuestion != None) {
            val path = xPathNodesAndQuestion.get._1
            val options = path.split("[|]")

            def validateNodeValue(options: Array[String]) {
              val nodes = options(0).split(">")
              val elementValue = XmlNode(childNode(xml.\\(nodes(0)), nodes.drop(1)))
              if (!elementValue.isDefined) {
                if (options.size > 1) validateNodeValue(options.drop(1))
                else errors += attribute + " " + path + " XML element not found"
              } else {
                if (elementValue doesNotMatch ClaimValue(attribute, value, xPathNodesAndQuestion.get._2)) {
                  if (options.size > 1) validateNodeValue(options.drop(1))
                  else errors += attribute + " " + path + elementValue.error
                }
              }
            }

            validateNodeValue(options)
          } else  warnings += attribute + " does not have an XML path mapping defined."
        }
        catch {
          case e:Exception => errors += attribute + " " + value + " Error: " + e.getMessage
        }
    }

    if (errors.nonEmpty && throwException) throw new PageObjectException("XML validation failed", errors.toList)
    errors.toList
  }
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

/**
 * Represents an Xml Node once "cleaned", i.e. trimmed and line returns removed.
 */
class XmlNode(val theNodes: NodeSeq) {

  var error = ""

  def matches(claimValue: ClaimValue): Boolean = {
    try {
      val nodeStart = theNodes(0).mkString

      val isARepeatableNode = !nodeStart.contains(XmlNode.EvidenceListNode) && !nodeStart.contains(XmlNode.DeclarationNode) && !nodeStart.contains("<Employed>") && !nodeStart.contains("<BreaksSinceClaim>")

      val isRepeatedAttribute = claimValue.attribute.contains( """_""")

      val iteration = if (isRepeatedAttribute) claimValue.attribute.split("_")(1).toInt - 1 else 0

      if (!isARepeatableNode && iteration > 0 && !nodeStart.contains(XmlNode.EvidenceListNode)) true
      else {
        val isPensionScheme = theNodes.mkString.contains("<PensionScheme>") // Because of bug in Schema :(  Do not like it

        val index = if (isRepeatedAttribute && isARepeatableNode && !isPensionScheme) iteration else 0

        val value = XmlNode.prepareElement(if (isPensionScheme) theNodes.text else theNodes(index).text)
        val nodeName = theNodes(index).mkString
        def valuesMatching = {
          if (value.matches( """\d{4}-\d{2}-\d{2}[tT]\d{2}:\d{2}:\d{2}""") || nodeName.endsWith("OtherNames>") || nodeName.endsWith("PayerName>") || isPensionScheme) value.contains(claimValue.value)
          else if (claimValue.attribute.contains("EmploymentAddtionalWageHowOftenAreYouPaid") || claimValue.attribute.contains("EmploymentChildcareExpensesHowOften") || claimValue.attribute.contains("EmploymentCareExpensesHowOftenYouPayfor")
          || claimValue.attribute.contains("SelfEmployedCareExpensesHowOften") || claimValue.attribute.contains("SelfEmployedChildcareExpensesHowOften"))
            value.contains(StatutoryPaymentFrequency.mapToHumanReadableString(claimValue.value,None).toLowerCase)
          else if (nodeName.startsWith(XmlNode.EvidenceListNode)) {
            // Awful code. Need to do something about it! (JMI)
            if (claimValue.attribute.contains("TimeSpentAbroadMoreTripsOutOfGBforMoreThan52WeeksAtATime")) {
              if (iteration == 0 ) value.matches(".*haveyouhadanymoretripsoutofgreatbritainformorethan[^=]*=" + claimValue.value +".*") else true
            }
            else if (claimValue.attribute.contains("TimeSpentAbroadHaveYouBeenOutOfGBWithThePersonYouCareFor")) {
              if (iteration == 0) value.matches(".*haveyoubeenoutofgreatbritainwiththepersonyoucarefor[^=]*=" + claimValue.value +".*") else true
            }
            else if (claimValue.attribute == "SelfEmployedChildcareExpensesHowOften" || claimValue.attribute == "SelfEmployedCareExpensesHowOften")
              value.contains(claimValue.question+"="+ PensionPaymentFrequency.mapToHumanReadableString(claimValue.value).toLowerCase)
            else value.contains(claimValue.question + "=" + claimValue.value)
          }
          else if (nodeName.endsWith("gds:Line>")) claimValue.value.contains(value)
          else if (nodeName.startsWith("<ClaimantActing")) nodeName.toLowerCase.contains(claimValue.value + ">" + value)
          else if (nodeName.startsWith(XmlNode.DeclarationNode)) value.contains(claimValue.question + claimValue.value)
          else value == claimValue.value
        }

        val matching = valuesMatching

        if (!matching)
          error = " value expected: [" + (if (nodeName.startsWith(XmlNode.EvidenceListNode)) claimValue.question + "=" + claimValue.value else claimValue.value) + "] within value read: [" + value + "]"
        matching
      }
    }
    catch {
      case e: IndexOutOfBoundsException => throw new PageObjectException("XML Validation failed" + this.toString() + " - " + claimValue.attribute)
    }
  }

  def doesNotMatch(claimValue: ClaimValue): Boolean = !matches(claimValue)

  def size = theNodes.size

  def isDefined = theNodes.nonEmpty

  override def toString = theNodes.mkString(",")
}

object XmlNode {

  val EvidenceListNode = "<EvidenceList>"
  val DeclarationNode = "<Declaration>"

  private def prepareElement(elementValue: String) = elementValue.replace("\\n", "").replace("\n", "").replace(" ", "").trim.toLowerCase

  def apply(nodes: NodeSeq) = new XmlNode(nodes)
}

/**
 * Represents a claimvalue once "cleaned", i.e trimmed and date reformated to yyyy-MM-dd as in the XML.
 * @param value value cleaned from the claim.
 */
class ClaimValue(val attribute: String, val value: String, val question: String) {

  override def toString = question + " - " + attribute + ": " + value
}

object ClaimValue {

  private def prepareQuestion(question: String) = question.replace("\\n", "").replace("\n", "").replace(" ", "").trim.toLowerCase

  private def prepareClaimValue(claimValue: String, attribute:String) = {
    val cleanValue = claimValue.replace("\\n", "").replace(" ", "").trim.toLowerCase

    if (cleanValue.contains("/") && !attribute.startsWith("EmploymentLeavingDateP45") && !attribute.startsWith("AboutYouWhenDidYouArriveInYheUK")) {
      val date = DateTime.parse(cleanValue, DateTimeFormat.forPattern("dd/MM/yyyy"))
      date.toString(DateTimeFormat.forPattern("yyyy-MM-dd"))
    } else cleanValue
  }

  def apply(attribute: String, value: String, question: String) = new ClaimValue(attribute, prepareClaimValue(value,attribute), prepareQuestion(question))
}