package utils.pageobjects

import play.api.test.TestBrowser
import utils.pageobjects.s1_carers_allowance.{ApprovePage, BenefitsPage, HoursPage}
import scala.io.Source
import scala.language.dynamics


/**
 * Factory used by Page to create from an html page the right page object.
 * If there is no Page Object mapping to the title then it creates an instance of UnknownPage.
 * @author Jorge Migueis
 *         Date: 10/07/2013
 */
object PageFactory {
  def buildPageFromTitle(browser: TestBrowser, title: String) = {
    title match {
      case BenefitsPage.title => BenefitsPage buildPage (browser)
      case HoursPage.title => HoursPage buildPage (browser)
      case ApprovePage.title => ApprovePage buildPage (browser)
      case _ => new UnknownPage(browser, title)
    }
  }

  def buildClaimFromFile(fileName: String) = {
    val claim = new ClaimScenario
    Source.fromURL(getClass getResource (fileName)).getLines() foreach {
      line =>
        claim.updateDynamic(line)("= oops")
    }
  }
}
