package utils.pageobjects

import play.api.test.TestBrowser
import scala.io.Source
import scala.language.dynamics
import scala.collection.mutable
import scala.util.matching.Regex
import utils.pageobjects.s1_carers_allowance._
import utils.pageobjects.s2_about_you.{TimeOutsideUKPage, ClaimDatePage, ContactDetailsPage, YourDetailsPage}


/**
 * Factory used by Page to create from an html page the right page object.
 * If there is no Page Object mapping to the title then it creates an instance of UnknownPage.
 * @author Jorge Migueis
 *         Date: 10/07/2013
 */
object PageFactory {

  def buildXmlMappingFromFile(fileName: String) = {
    val map = mutable.Map[String, Array[String]]()
    def converter(name: String)(value: String): Unit = map += (name -> value.split(">"))
    FactoryFromFile.buildFromFile(fileName, converter)
    map
  }

  def buildPageFromTitle(browser: TestBrowser, title: String, previousPage: Option[Page]) = {
    // Generic solution using mapping does not work because the objects should register themselves
    // and there is no way to get that registration triggered automatically when test are loaded.
    title match {
      // S1
      case BenefitsPage.title => BenefitsPage buildPageWith(browser, previousPage)
      case HoursPage.title => HoursPage buildPageWith(browser, previousPage)
      case Over16Page.title => Over16Page buildPageWith(browser, previousPage)
      case LivingInGBPage.title => LivingInGBPage buildPageWith(browser, previousPage)
      case ApprovePage.title => ApprovePage buildPageWith(browser, previousPage)
      // S2
      case YourDetailsPage.title => YourDetailsPage buildPageWith(browser, previousPage)
      case ContactDetailsPage.title => ContactDetailsPage buildPageWith(browser, previousPage)
      case TimeOutsideUKPage.title => TimeOutsideUKPage buildPageWith(browser, previousPage)
      case ClaimDatePage.title => ClaimDatePage buildPageWith(browser, previousPage)
      case _ => new UnknownPage(browser, title, previousPage)
    }
  }
}
