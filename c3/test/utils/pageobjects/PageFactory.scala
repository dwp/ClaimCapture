package utils.pageobjects

import play.api.test.TestBrowser
import scala.language.dynamics
import scala.collection.mutable
import utils.pageobjects.s1_carers_allowance._
import utils.pageobjects.s2_about_you._
import utils.pageobjects.s3_your_partner._
import utils.pageobjects.s9_self_employment._
import utils.pageobjects.s4_care_you_provide._
import utils.pageobjects.s5_time_spent_abroad._
import utils.pageobjects.s6_pay_details._
import utils.pageobjects.s7_employment._
import utils.pageobjects.s8_other_money._

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
      case TestPage.title => TestPage buildPageWith(browser, previousPage)
      // S1
      case G1BenefitsPage.title => G1BenefitsPage buildPageWith(browser, previousPage)
      case G2HoursPage.title => G2HoursPage buildPageWith(browser, previousPage)
      case G3Over16Page.title => G3Over16Page buildPageWith(browser, previousPage)
      case G4LivingInGBPage.title => G4LivingInGBPage buildPageWith(browser, previousPage)
      case G5CarersResponsePage.title => G5CarersResponsePage buildPageWith(browser, previousPage)
      case G6ApprovePage.title => G6ApprovePage buildPageWith(browser, previousPage)
      // S2
      case G1YourDetailsPage.title => G1YourDetailsPage buildPageWith(browser, previousPage)
      case G2ContactDetailsPage.title => G2ContactDetailsPage buildPageWith(browser, previousPage)
      case G3TimeOutsideUKPage.title => G3TimeOutsideUKPage buildPageWith(browser, previousPage)
      case G4ClaimDatePage.title => G4ClaimDatePage buildPageWith(browser, previousPage)
      case G5MoreAboutYouPage.title => G5MoreAboutYouPage buildPageWith(browser, previousPage)
      case G6EmploymentPage.title => G6EmploymentPage buildPageWith(browser, previousPage)
      case G7PropertyAndRentPage.title => G7PropertyAndRentPage buildPageWith(browser, previousPage)
      case G8AboutYouCompletedPage.title => G8AboutYouCompletedPage buildPageWith(browser, previousPage)
      // S3
      case G1YourPartnerPersonalDetailsPage.title => G1YourPartnerPersonalDetailsPage buildPageWith(browser,previousPage)
      case G2YourPartnerContactDetailsPage.title => G2YourPartnerContactDetailsPage buildPageWith(browser, previousPage)
      case G3MoreAboutYourPartnerPage.title => G3MoreAboutYourPartnerPage buildPageWith(browser, previousPage)
      case G4PersonYouCareForPage.title => G4PersonYouCareForPage buildPageWith(browser,previousPage)
      case G5YourPartnerCompletedPage.title => G5YourPartnerCompletedPage buildPageWith(browser, previousPage)
      // S4
      case G1TheirPersonalDetailsPage.title => G1TheirPersonalDetailsPage buildPageWith(browser, previousPage)
      case G2TheirContactDetailsPage.title => G2TheirContactDetailsPage buildPageWith(browser, previousPage)
      case G3MoreAboutThePersonPage.title => G3MoreAboutThePersonPage buildPageWith(browser, previousPage)
      case G4PreviousCarerPersonalDetailsPage.title => G4PreviousCarerPersonalDetailsPage buildPageWith(browser, previousPage)
      case G5PreviousCarerContactDetailsPage.title => G5PreviousCarerContactDetailsPage buildPageWith(browser, previousPage)
      case G6RepresentativesForThePersonPage.title => G6RepresentativesForThePersonPage buildPageWith(browser, previousPage)
      case G7MoreAboutTheCarePage.title => G7MoreAboutTheCarePage buildPageWith(browser, previousPage)
      case G8OneWhoPaysPersonalDetailsPage.title => G8OneWhoPaysPersonalDetailsPage buildPageWith(browser, previousPage)
      case G9ContactDetailsOfPayingPersonPage.title => G9ContactDetailsOfPayingPersonPage buildPageWith(browser, previousPage)
      case G10BreaksInCarePage.title => G10BreaksInCarePage buildPageWith(browser, previousPage, iteration)
      case G11BreakPage.title => G11BreakPage buildPageWith(browser, previousPage, iteration)
      case G12CareYouProvideCompletedPage.title => G12CareYouProvideCompletedPage buildPageWith(browser, previousPage)
      // S5
      case G1NormalResidenceAndCurrentLocationPage.title => G1NormalResidenceAndCurrentLocationPage buildPageWith(browser, previousPage)
      case G2AbroadForMoreThan4WeeksPage.title => G2AbroadForMoreThan4WeeksPage buildPageWith(browser, previousPage)
      case G3AbroadForMoreThan52WeeksPage.title => G3AbroadForMoreThan52WeeksPage buildPageWith(browser, previousPage)
      case G4TripPage.title => G4TripPage buildPageWith(browser, previousPage, iteration)
      case G5TimeAbroadCompletedPage.title => G5TimeAbroadCompletedPage buildPageWith(browser, previousPage)
      // S6
      case G1HowWePayYouPage.title => G1HowWePayYouPage buildPageWith(browser, previousPage)
      case G2AddressOfSchoolCollegeOrUniversityPage.title => G2AddressOfSchoolCollegeOrUniversityPage buildPageWith(browser, previousPage)
      case G3PayDetailsCompletedPage.title => G3PayDetailsCompletedPage buildPageWith(browser,previousPage)
      // S7
      case G1BeenEmployedPage.title => G1BeenEmployedPage buildPageWith(browser,previousPage)
      case G2JobDetailsPage.title => G2JobDetailsPage buildPageWith(browser,previousPage,iteration)
      case G3EmployerContactDetailsPage.title => G3EmployerContactDetailsPage buildPageWith(browser,previousPage,iteration)
      case G4LastWagePage.title => G4LastWagePage buildPageWith(browser,previousPage,iteration)
      case G5AdditionalWageDetailsPage.title => G5AdditionalWageDetailsPage buildPageWith(browser,previousPage,iteration)
      case G6MoneyOwedByEmployerPage.title => G6MoneyOwedByEmployerPage buildPageWith(browser,previousPage,iteration)
      case G7PensionSchemesPage.title => G7PensionSchemesPage buildPageWith(browser,previousPage,iteration)
      case G8AboutExpensesPage.title => G8AboutExpensesPage buildPageWith(browser,previousPage,iteration)
      case G9NecessaryExpensesPage.title => G9NecessaryExpensesPage buildPageWith(browser,previousPage,iteration)
      case G10ChildcareExpensesPage.title => G10ChildcareExpensesPage buildPageWith(browser,previousPage,iteration)
      case G11ChildcareProviderPage.title => G11ChildcareProviderPage buildPageWith(browser,previousPage,iteration)
      case G12PersonYouCareForExpensesPage.title => G12PersonYouCareForExpensesPage buildPageWith(browser,previousPage,iteration)
      case G13CareProviderPage.title => G13CareProviderPage buildPageWith(browser,previousPage,iteration)
      case G14JobCompletionPage.title => G14JobCompletionPage buildPageWith(browser,previousPage,iteration)
      case G15CompletedPage.title => G15CompletedPage buildPageWith(browser,previousPage)
      // S8
      case G1AboutOtherMoneyPage.title => G1AboutOtherMoneyPage buildPageWith(browser, previousPage)
      case G2MoneyPaidToSomeoneElseForYouPage.title => G2MoneyPaidToSomeoneElseForYouPage buildPageWith(browser, previousPage)
      case G3PersonWhoGetsThisMoneyPage.title => G3PersonWhoGetsThisMoneyPage buildPageWith(browser, previousPage)
      case G4PersonContactDetailsPage.title => G4PersonContactDetailsPage buildPageWith(browser, previousPage)
      case G5StatutorySickPayPage.title => G5StatutorySickPayPage buildPageWith(browser, previousPage)
      case G6OtherStatutoryPayPage.title => G6OtherStatutoryPayPage buildPageWith(browser, previousPage)
      case G7OtherEEAStateOrSwitzerlandPage.title => G7OtherEEAStateOrSwitzerlandPage buildPageWith(browser,previousPage)
      case G8OtherMoneyCompletedPage.title => G8OtherMoneyCompletedPage buildPageWith(browser, previousPage)
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
