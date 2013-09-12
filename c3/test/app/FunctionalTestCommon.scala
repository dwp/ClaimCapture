package app

import org.specs2.mutable.{Tags, Specification}
import utils.pageobjects.{XmlPage, TestData}
import utils.pageobjects.xml_validation.XMLBusinessValidation

/**
 * Provides XML validation capacity to End to End tests.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
abstract class FunctionalTestCommon extends Specification with Tags {
  isolated

  def validateAndPrintErrors(page: XmlPage, claim: TestData, validator: XMLBusinessValidation): Boolean = {
    val issues = page.validateXmlWith(claim, validator)
    val errors = issues._1
    val warnings = issues._2

    if (errors.nonEmpty) {
      println("Number of errors: " + errors.size)
      println("[Error] " + errors.mkString("\n\n[Error] "))
      println(page.source())
    }

    if (warnings.nonEmpty) {
      println("Number of warnings: " + warnings.size)
      println("[Warning] " + warnings.mkString("\n[Warning] "))
    }
    //    println(page.source())
    errors.isEmpty
  }

}