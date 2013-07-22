package utils.pageobjects

import play.api.test.TestBrowser
import scala.language.dynamics
import scala.collection.mutable
import utils.pageobjects.s1_carers_allowance._
import utils.pageobjects.s2_about_you._
import utils.pageobjects.s8_other_money._
import utils.pageobjects.s6_pay_details.HowWePayYouPage
import utils.pageobjects.s3_your_partner.{YourPartnerContactDetailsPage, YourPartnerPersonalDetailsPage}


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
      case MoreAboutYouPage.title => MoreAboutYouPage buildPageWith(browser, previousPage)
      case EmploymentPage.title => EmploymentPage buildPageWith(browser, previousPage)
      case PropertyAndRentPage.title => PropertyAndRentPage buildPageWith(browser, previousPage)
      case CompletedPage.title => CompletedPage buildPageWith(browser, previousPage)
      // S3
      case YourPartnerPersonalDetailsPage.title => YourPartnerPersonalDetailsPage buildPageWith(browser, previousPage)
      case YourPartnerContactDetailsPage.title => YourPartnerContactDetailsPage buildPageWith(browser, previousPage)
      // S6
      case HowWePayYouPage.title => HowWePayYouPage buildPageWith(browser, previousPage)
      // S8 TODO SKW these must be filled in so tests using  "must beAnInstanceOf" work correctly!!!
      case G1AboutOtherMoneyPage.title => G1AboutOtherMoneyPage buildPageWith(browser, previousPage)
      case G3PersonWhoGetsThisMoneyPage.title => G3PersonWhoGetsThisMoneyPage buildPageWith(browser, previousPage)
      case G4PersonContactDetailsPage.title => G4PersonContactDetailsPage buildPageWith(browser, previousPage)
      case G5StatutorySickPayPage.title => G5StatutorySickPayPage buildPageWith(browser, previousPage)
      case G6OtherStatutoryPayPage.title => G6OtherStatutoryPayPage buildPageWith(browser, previousPage)
      // Catch pages not covered by framework
      case _ => new UnknownPage(browser, title, previousPage)
    }
  }
}
