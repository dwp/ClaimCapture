package app

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.s1_carers_allowance.G1BenefitsPageContext
import utils.pageobjects.{XmlPage, ClaimScenario, Page}

/**
 * End-to-End functional tests using input files created by Steve Moody.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
abstract class FunctionalTestCommon extends Specification with Tags {
  isolated

  def validateAndPrintErrors(page: XmlPage, claim: ClaimScenario) = {
    val issues = page.validateXmlWith(claim)
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

    errors.isEmpty
  }

}