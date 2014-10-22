package utils.pageobjects.xml_validation

import utils.pageobjects.{TestDatumValue, PageObjectException, FactoryFromFile, TestData}
import scala.xml.{Elem, XML}
import scala.collection.mutable
import scala.language.postfixOps

/**
 * Interface of the XML Business Validation classes.
 * @author Jorge Migueis
 *         Date: 06/09/2013
 */
abstract class XMLBusinessValidation() {

  val errors = mutable.MutableList.empty[String]
  val warnings = mutable.MutableList.empty[String]

  def objValue(attribute: String, value: String, question: String): TestDatumValue

  def validateXMLClaim(claim: TestData, xml: Elem, throwException: Boolean): List[String]

  def validateXMLClaim(claim: TestData, xmlString: String, throwException: Boolean): List[String] = validateXMLClaim(claim, XML.loadString(xmlString), throwException)

  def validateXMLClaim(claim: TestData,
                       xmlString: String,
                       throwException: Boolean,
                       mappingFileName: String,
                       createXMLValidationNode: (Elem, Array[String]) => XMLValidationNode): List[String] = validateXMLClaim(claim, XML.loadString(xmlString), throwException, mappingFileName, createXMLValidationNode)

  /**
   * Performs the validation of a claim XML against the data used to populate the claim forms.
   * @param claim Original claim used to go through the screens and now used to validate XML.
   * @param xml  XML that needs to be validated against the provided claim.
   * @param throwException Specify whether the validation should throw an exception if mismatches are found.
   * @return List of errors found. The list is empty if no errors were found.
   */
  def validateXMLClaim(claim: TestData,
                       xml: Elem,
                       throwException: Boolean,
                       mappingFileName: String,
                       createXMLValidationNode: (Elem, Array[String]) => XMLValidationNode): List[String] = {
    val mapping = XMLBusinessValidation.buildXmlMappingFromFile(mappingFileName) // Load the XML mapping

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
              val elementValue = createXMLValidationNode(xml,nodes)
              if (!elementValue.isDefined) {
                if (options.size > 1) validateNodeValue(options.drop(1))
                else errors += attribute + " " + path + " XML element not found"
              } else {
                if (elementValue doesNotMatch objValue(attribute, value, xPathNodesAndQuestion.get._2)) {
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
