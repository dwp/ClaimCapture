package utils.pageobjects

import play.api.test.TestBrowser
import scala.language.dynamics
import scala.collection.mutable
import utils.pageobjects.s1_carers_allowance._
import utils.pageobjects.s2_about_you._
import utils.pageobjects.s8_other_money._
import utils.pageobjects.s6_pay_details.G1HowWePayYouPage
import utils.pageobjects.s3_your_partner._
import utils.pageobjects.s3_your_partner._
import utils.pageobjects.s9_self_employment._
import utils.pageobjects.s4_care_you_provide._

/**
 * Factory used by Page to create from an html page the right page object.
 * If there is no Page Object mapping to the title then it creates an instance of UnknownPage.
 * @author Jorge Migueis
 *         Date: 10/07/2013
 */
object PageFactory {

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
      case G8CompletedPage.title => G8CompletedPage buildPageWith(browser, previousPage)
      // S3
      case G1YourPartnerPersonalDetailsPage.title => G1YourPartnerPersonalDetailsPage buildPageWith(browser,previousPage)
      case G2YourPartnerContactDetailsPage.title => G2YourPartnerContactDetailsPage buildPageWith(browser, previousPage)
      case G3MoreAboutYourPartnerPage.title => G3MoreAboutYourPartnerPage buildPageWith(browser, previousPage)
      case G4PersonYouCareForPage.title => G4PersonYouCareForPage buildPageWith(browser,previousPage)
      case G5CompletedPage.title => G5CompletedPage buildPageWith(browser, previousPage)
      // s4
      case G1TheirPersonalDetailsPage.title => G1TheirPersonalDetailsPage buildPageWith(browser, previousPage)
      case G2TheirContactDetailsPage.title => G2TheirContactDetailsPage buildPageWith(browser, previousPage)
      case G3MoreAboutThePersonPage.title => G3MoreAboutThePersonPage buildPageWith(browser, previousPage)
      case G4PreviousCarerPersonalDetailsPage.title => G4PreviousCarerPersonalDetailsPage buildPageWith(browser, previousPage)
      case G5PreviousCarerContactDetailsPage.title => G5PreviousCarerContactDetailsPage buildPageWith(browser, previousPage)
      case G6RepresentativesForThePersonPage.title => G6RepresentativesForThePersonPage buildPageWith(browser, previousPage)
      // S6
      case G1HowWePayYouPage.title => G1HowWePayYouPage buildPageWith(browser, previousPage)
      // S8 TODO SKW these must be filled in so tests using  "must beAnInstanceOf" work correctly!!!
      case G1AboutOtherMoneyPage.title => G1AboutOtherMoneyPage buildPageWith(browser, previousPage)
      case G2MoneyPaidToSomeoneElseForYouPage.title => G2MoneyPaidToSomeoneElseForYouPage buildPageWith(browser, previousPage)
      case G3PersonWhoGetsThisMoneyPage.title => G3PersonWhoGetsThisMoneyPage buildPageWith(browser, previousPage)
      case G4PersonContactDetailsPage.title => G4PersonContactDetailsPage buildPageWith(browser, previousPage)
      case G5StatutorySickPayPage.title => G5StatutorySickPayPage buildPageWith(browser, previousPage)
      case G6OtherStatutoryPayPage.title => G6OtherStatutoryPayPage buildPageWith(browser, previousPage)
      case G7CompletedPage.title => G7CompletedPage buildPageWith(browser, previousPage)
      // S9
      case G1AboutSelfEmploymentPage.title => G1AboutSelfEmploymentPage buildPageWith(browser, previousPage)
      case G6ChildcareProvidersContactDetailsPage.title => G6ChildcareProvidersContactDetailsPage buildPageWith(browser, previousPage)
      case G7ExpensesWhileAtWorkPage.title => G7ExpensesWhileAtWorkPage buildPageWith(browser, previousPage)
      case G8CareProvidersContactDetailsPage.title => G8CareProvidersContactDetailsPage buildPageWith(browser, previousPage)
      case s9_self_employment.G9CompletedPage.title => s9_self_employment.G9CompletedPage buildPageWith(browser, previousPage)
      // Catch pages not covered by framework
      case _ => new UnknownPage(browser, title, previousPage)
    }
  }
}
