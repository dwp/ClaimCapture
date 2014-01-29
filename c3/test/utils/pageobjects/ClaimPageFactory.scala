package utils.pageobjects

import play.api.test.TestBrowser
import scala.language.dynamics
import utils.pageobjects.s1_carers_allowance._
import utils.pageobjects.s2_about_you._
import utils.pageobjects.s3_your_partner._
import utils.pageobjects.s4_care_you_provide._
import utils.pageobjects.s6_education._
import utils.pageobjects.s7_employment._
import utils.pageobjects.s8_self_employment._
import utils.pageobjects.s9_other_money._
import utils.pageobjects.s10_pay_details._
import utils.pageobjects.S11_consent_and_declaration._


/**
 * Factory used by Page to create from an html page the right page object.
 * If there is no Page Object mapping to the title then it creates an instance of UnknownPage.
 * @author Jorge Migueis
 *         Date: 10/07/2013
 */
object ClaimPageFactory extends PageFactory {
  def buildPageFromTitle(browser: TestBrowser, title: String, previousPage: Option[Page], iteration: Int) = {
    import IterationManager._

    // Generic solution using mapping does not work because the objects should register themselves
    // and there is no way to get that registration triggered automatically when test are loaded.
    if (null == title ) XmlPage (browser, previousPage)
    else {
      val m:PartialFunction[String,Page] = {
        // S1
        case G1BenefitsPage.title => G1BenefitsPage (browser, previousPage)
        case G2HoursPage.title => G2HoursPage (browser, previousPage)
        case G3Over16Page.title => G3Over16Page (browser, previousPage)
        case G4LivesInGBPage.title => G4LivesInGBPage (browser, previousPage)
        case G5CarersResponsePage.title => G5CarersResponsePage (browser, previousPage)
        case G6ApprovePage.title => G6ApprovePage (browser, previousPage)
        // S2
        case G1YourDetailsPage.title => G1YourDetailsPage (browser, previousPage)
        case G2ContactDetailsPage.title => G2ContactDetailsPage (browser, previousPage)
        case G3ClaimDatePage.title => G3ClaimDatePage (browser, previousPage)
        case G4NationalityAndResidencyPage.title => G4NationalityAndResidencyPage (browser, previousPage)
      }
      m.orElse[String,Page]{
        IterableNode(Abroad)(iteration =>{
          case G5AbroadForMoreThan52WeeksPage.title => G5AbroadForMoreThan52WeeksPage (browser, previousPage, iteration)
          case G6TripPage.title => G6TripPage (browser, previousPage, iteration)
        })
      }.orElse[String,Page]{
        case G7OtherEEAStateOrSwitzerlandPage.title => G7OtherEEAStateOrSwitzerlandPage (browser,previousPage)
        case G8MoreAboutYouPage.title => G8MoreAboutYouPage (browser, previousPage)
        // S3
        case G1YourPartnerPersonalDetailsPage.title => G1YourPartnerPersonalDetailsPage (browser,previousPage)
        // S4
        case G1TheirPersonalDetailsPage.title => G1TheirPersonalDetailsPage (browser, previousPage)
        case G2TheirContactDetailsPage.title => G2TheirContactDetailsPage (browser, previousPage)
        case G3RelationshipAndOtherClaimsPage.title => G3RelationshipAndOtherClaimsPage (browser, previousPage)
        case G7MoreAboutTheCarePage.title => G7MoreAboutTheCarePage (browser, previousPage)
      }.orElse[String,Page]{
        IterableNode(Breaks)(iteration =>{
          case G10BreaksInCarePage.title => G10BreaksInCarePage (browser, previousPage, iteration)
          case G11BreakPage.title => G11BreakPage (browser, previousPage, iteration)
        })
      }.orElse[String,Page]{
        //S6
        case G1YourCourseDetailsPage.title => G1YourCourseDetailsPage (browser,previousPage)
      }.orElse[String,Page]{
        IterableNode(Employment)(iteration =>{
          // S7
          case G0EmploymentPage.title => G0EmploymentPage (browser, previousPage)
          case G2BeenEmployedPage.title => G2BeenEmployedPage (browser,previousPage,iteration)
          case G3JobDetailsPage.title => G3JobDetailsPage (browser,previousPage,iteration)
          case G4EmployerContactDetailsPage.title => G4EmployerContactDetailsPage (browser,previousPage,iteration)
          case G5LastWagePage.title => G5LastWagePage (browser,previousPage,iteration)
          case G6AdditionalWageDetailsPage.title => G6AdditionalWageDetailsPage (browser,previousPage,iteration)
          case G7PensionSchemesPage.title => G7PensionSchemesPage (browser,previousPage,iteration)
          case G8AboutExpensesPage.title => G8AboutExpensesPage (browser,previousPage,iteration)
          case G9NecessaryExpensesPage.title => G9NecessaryExpensesPage (browser,previousPage,iteration)
          case G10ChildcareExpensesPage.title => G10ChildcareExpensesPage (browser,previousPage,iteration)
          case G12PersonYouCareForExpensesPage.title => G12PersonYouCareForExpensesPage (browser,previousPage,iteration)
          case G14JobCompletionPage.title => G14JobCompletionPage (browser,previousPage,iteration)
        })
      }.orElse[String,Page]{
        case G15CompletedPage.title => G15CompletedPage (browser,previousPage)
        // S8
        case G1AboutOtherMoneyPage.title => G1AboutOtherMoneyPage (browser, previousPage)
        case G5StatutorySickPayPage.title => G5StatutorySickPayPage (browser, previousPage)
        case G6OtherStatutoryPayPage.title => G6OtherStatutoryPayPage (browser, previousPage)
        // S9
        case G1HowWePayYouPage.title => G1HowWePayYouPage (browser, previousPage)
        case G2BankBuildingSocietyDetailsPage.title => G2BankBuildingSocietyDetailsPage (browser, previousPage)
        case G3PayDetailsCompletedPage.title => G3PayDetailsCompletedPage (browser,previousPage)
        // S9
        case G1AboutSelfEmploymentPage.title => G1AboutSelfEmploymentPage (browser, previousPage)
        case G2SelfEmploymentYourAccountsPage.title => G2SelfEmploymentYourAccountsPage (browser, previousPage)
        case G4SelfEmploymentPensionsAndExpensesPage.title => G4SelfEmploymentPensionsAndExpensesPage (browser, previousPage)
        case G5ChildcareExpensesWhileAtWorkPage.title => G5ChildcareExpensesWhileAtWorkPage (browser, previousPage)
        case G7ExpensesWhileAtWorkPage.title => G7ExpensesWhileAtWorkPage (browser, previousPage)
        case s8_self_employment.G9CompletedPage.title => s8_self_employment.G9CompletedPage (browser, previousPage)
        // S10
        case G1AdditionalInfoPage.title => G1AdditionalInfoPage (browser,previousPage)
        case G2ConsentPage.title => G2ConsentPage (browser,previousPage)
        case G3DisclaimerPage.title => G3DisclaimerPage (browser,previousPage)
        case G4DeclarationPage.title => G4DeclarationPage (browser,previousPage)
        case G5SubmitPage.title => G5SubmitPage (browser,previousPage)
        // Catch pages not covered by framework
        case _ => new UnknownPage(browser, title, previousPage)
      }(title.toLowerCase)
    }
  }
}

object IterationManager{
  val Abroad = "Abroad"
  val Breaks = "Breaks"
  val Employment = "Employment"
  var iterationBlocks = Map[String,Int]()

  def apply(section:String):Int = {
    if (iterationBlocks.exists(_._1 == section)){
      iterationBlocks(section)
    }else{
      1
    }
  }

  def init() = {
    iterationBlocks = Map("Abroad" -> 1,"Breaks" -> 1,"Employment" -> 1)
  }

  def increment(section:String):Int = {
    if (iterationBlocks.exists(_._1 == section)){
      iterationBlocks = iterationBlocks.filterNot(_._1 == section) ++ Map(section -> (iterationBlocks(section)+1))
      apply(section)
    }else{
      1
    }
  }
}

object IterableNode {
  def apply[A,B](section:String)(func:Int => PartialFunction[A,B]):PartialFunction[A,B] = {
    func(IterationManager(section))
  }
}
