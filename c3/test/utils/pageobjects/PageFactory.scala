package utils.pageobjects

import play.api.test.TestBrowser
import utils.pageobjects.s1_carers_allowance.{Over16Page, ApprovePage, BenefitsPage, HoursPage}
import scala.io.Source
import scala.language.dynamics
import scala.util.matching.Regex
import scala.collection.mutable.Map


/**
 * Factory used by Page to create from an html page the right page object.
 * If there is no Page Object mapping to the title then it creates an instance of UnknownPage.
 * @author Jorge Migueis
 *         Date: 10/07/2013
 */
object PageFactory {

  // used to parse claim file's lines. Expect 3 information colums, followed by attribute and then value of attribute.
  private val Extractor = """^ *([^,"]+|"[^"]+") *, *([^,"]+|"[^"]+") *, *([^,"]+|"[^"]+") *, *([^,"]+|"[^"]+") *,? *([^,"]+|"[^"]+")?.*""".r

  def buildPageFromTitle(browser: TestBrowser, title: String) = {
    title match {
      case BenefitsPage.title => BenefitsPage buildPageWith browser
      case HoursPage.title => HoursPage buildPageWith browser
      case ApprovePage.title => ApprovePage buildPageWith browser
      case Over16Page.title => Over16Page buildPageWith browser
      case _ => new UnknownPage(browser, title)
    }
  }

  def buildClaimFromFile(fileName: String) = {
    val claim = new ClaimScenario
    buildFromFile(fileName, claim.updateDynamic)
    claim
  }


  def buildXmlMappingFromFile(fileName: String) = {

    val map = Map[String,String]()
    def converter (name: String)(value: String):Unit = {
         map += (name -> value)
    }
    buildFromFile(fileName, converter)
    map
  }

  private def buildFromFile(fileName: String, extractor: (String) => (String) => Unit ) = {
    Source fromURL (getClass getResource fileName) getLines() foreach {
      line =>
        try {
          val Extractor(column1, column2, column3, column_name,column_value) = line
          if (null != column_name && null != column_value)
            extractor(column_name.replaceAll("\"", "").trim)(column_value.replaceAll("\"", "").trim)
        }
        catch {
          case e: Exception => throw new PageObjectException(message="Error while parsing file " + fileName + ". ", exception=e)
        }
    }
  }

}
