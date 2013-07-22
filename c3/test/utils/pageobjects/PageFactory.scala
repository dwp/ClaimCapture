package utils.pageobjects

import play.api.test.TestBrowser
import scala.language.dynamics
import scala.collection.mutable
import utils.pageobjects.s1_carers_allowance._
import utils.pageobjects.s2_about_you._
import utils.pageobjects.s8_other_money._
import utils.pageobjects.s6_pay_details.G1HowWePayYouPage
import utils.pageobjects.s3_your_partner.{G2YourPartnerContactDetailsPage, G1YourPartnerPersonalDetailsPage}


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

  def buildPageFromTitle(browser: TestBrowser, title: String, previousPage: Option[Page], iteration: Int) = {
    // Generic solution using mapping does not work because the objects should register themselves
    // and there is no way to get that registration triggered automatically when test are loaded.
    title match {
      // S1
      case G1BenefitsPage.title => G1BenefitsPage buildPageWith(browser, previousPage)
      case G2HoursPage.title => G2HoursPage buildPageWith(browser, previousPage)
      case G3Over16Page.title => G3Over16Page buildPageWith(browser, previousPage)
      case G4LivingInGBPage.title => G4LivingInGBPage buildPageWith(browser, previousPage)
      case G5ApprovePage.title => G5ApprovePage buildPageWith(browser, previousPage)
      // S2
      case G1YourDetailsPage.title => G1YourDetailsPage buildPageWith(browser, previousPage)
      case G2ContactDetailsPage.title => G2ContactDetailsPage buildPageWith(browser, previousPage)
      case G3TimeOutsideUKPage.title => G3TimeOutsideUKPage buildPageWith(browser, previousPage)
      case G4ClaimDatePage.title => G4ClaimDatePage buildPageWith(browser, previousPage)
      case G5MoreAboutYouPage.title => G5MoreAboutYouPage buildPageWith(browser, previousPage)
      case G6EmploymentPage.title => G6EmploymentPage buildPageWith(browser, previousPage)
      case G7PropertyAndRentPage.title => G7PropertyAndRentPage buildPageWith(browser, previousPage)
      case CompletedPage.title => CompletedPage buildPageWith(browser, previousPage)
      // S3
      case G1YourPartnerPersonalDetailsPage.title => G1YourPartnerPersonalDetailsPage buildPageWith(browser,previousPage)
      case G2YourPartnerContactDetailsPage.title => G2YourPartnerContactDetailsPage buildPageWith(browser, previousPage)
      // S6
      case G1HowWePayYouPage.title => G1HowWePayYouPage buildPageWith(browser, previousPage)
      // S8 TODO SKW these must be filled in so tests using  "must beAnInstanceOf" work correctly!!!
      case G1AboutOtherMoneyPage.title => G1AboutOtherMoneyPage buildPageWith(browser, previousPage)
      case G3PersonWhoGetsThisMoneyPage.title => G3PersonWhoGetsThisMoneyPage buildPageWith(browser, previousPage)
      case G4PersonContactDetailsPage.title => G4PersonContactDetailsPage buildPageWith(browser, previousPage)
      case G5StatutorySickPayPage.title => G5StatutorySickPayPage buildPageWith(browser, previousPage)
      // Catch pages not covered by framework
      case _ => new UnknownPage(browser, title, previousPage)
    }
  }
}
