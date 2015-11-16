package app

import org.joda.time.format.DateTimeFormat
import org.specs2.mutable.{Tags, Specification}
import org.specs2.specification.{Example, Fragment}
import utils.pageobjects.preview.{PreviewTestableData, PreviewPage, PreviewBusinessValidation}
import utils.pageobjects.{Page, XmlPage, TestData}
import utils.pageobjects.xml_validation.{XMLClaimBusinessValidation, XMLBusinessValidation}

/**
 * Provides XML validation capacity to End to End tests.
 * @author Jorge Migueis
 *         Date: 02/08/2013
 */
abstract class FunctionalTestCommon extends Specification with Tags {
  isolated


  def validateAndPrintErrors(page: XmlPage, claim: TestData, validator: XMLBusinessValidation): Boolean = validateAndPrintErrors(Left(page),claim,validator)
  def validateAndPrintErrors(page: PreviewPage, claim: TestData, validator: XMLBusinessValidation): Boolean = validateAndPrintErrors(Right(page),claim,validator)

  def validateAndPrintErrors(page: Either[XmlPage,PreviewPage], claim: TestData, validator: XMLBusinessValidation): Boolean = {
    val issues = page match{
      case Left(p) => p.validateXmlWith(claim, validator)
      case Right(p) => p.validateXmlWith(claim,validator)
    }

    val p = page match{
      case Left(pg) => pg
      case Right(pg) => pg
    }


    val errors = issues._1
    val warnings = issues._2

    if (errors.nonEmpty) {
      println("Number of errors: " + errors.size)
      println("[Error] " + errors.mkString("\n\n[Error] "))
      println(p.source)
    }

    if (warnings.nonEmpty) {
      println("Number of warnings: " + warnings.size)
      println("[Warning] " + warnings.mkString("\n[Warning] "))
    }
    //    println(page.source)
    errors.isEmpty
  }

  def ninoConversion(id:String) = id -> {(s:String) => s}
  def dateConversion(id:String) = id -> {(s:String) => DateTimeFormat.forPattern("dd MMMM, yyyy").print(DateTimeFormat.forPattern("dd/MM/yyyy").parseDateTime(s)).toLowerCase }
  def addressConversion(id:String) = id -> {(s:String) =>s.replaceAll("&",", ")}

  def test(page:Page,claim:TestData,testableData:PreviewTestableData) = {

    page goToThePage()
    val previewPage = page runClaimWith(claim, PreviewPage.url,trace=true)

    previewPage match {
      case p: PreviewPage =>
        val validator = new PreviewBusinessValidation(testableData)
        validateAndPrintErrors(p,claim,validator) should beTrue

      case p: Page => println(p.source)
    }

    val lastPage = previewPage runClaimWith(claim, trace=true)

    lastPage match {
      case p: XmlPage => {
        val validator: XMLBusinessValidation = new XMLClaimBusinessValidation
        validateAndPrintErrors(p, claim, validator) should beTrue
      }
      case p: Page => println(p.source)
    }

  }

}