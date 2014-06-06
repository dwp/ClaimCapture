package utils.pageobjects

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
import utils.pageobjects.IterationManager._
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage


/**
 * Factory used by Page to create from an html page the right page object.
 * If there is no Page Object mapping to the title then it creates an instance of UnknownPage.
 * @author Jorge Migueis
 *         Date: 10/07/2013
 */
object ClaimPageFactory extends PageFactory {
  def buildPageFromTitle(title: String,ctx:PageObjectsContext) = {

    // Generic solution using mapping does not work because the objects should register themselves
    // and there is no way to get that registration triggered automatically when test are loaded.
    if (null == title ) XmlPage (ctx)
    else {
      val m:PartialFunction[String,Page] = {
        // S1
        case G1BenefitsPage.title => G1BenefitsPage (ctx)
        case G2HoursPage.title => G2HoursPage (ctx)
        case G3Over16Page.title => G3Over16Page (ctx)
        case G4LivesInGBPage.title => G4LivesInGBPage (ctx)
        case G5CarersResponsePage.title => G5CarersResponsePage (ctx)
        case G6ApprovePage.title => G6ApprovePage (ctx)
        //S1.5
        case G1ClaimDatePage.title => G1ClaimDatePage (ctx)
        // S2
        case G1YourDetailsPage.title => G1YourDetailsPage (ctx)
        case G2ContactDetailsPage.title => G2ContactDetailsPage (ctx)
        case G4NationalityAndResidencyPage.title => G4NationalityAndResidencyPage (ctx)
      }
      m.orElse[String,Page]{
        IterableNode(Abroad,ctx)(iteration =>{
          case G5AbroadForMoreThan52WeeksPage.title => G5AbroadForMoreThan52WeeksPage (ctx, iteration)
          case G6TripPage.title => G6TripPage (ctx, iteration)
        })
      }.orElse[String,Page]{
        case G7OtherEEAStateOrSwitzerlandPage.title => G7OtherEEAStateOrSwitzerlandPage (ctx)
        case G8MoreAboutYouPage.title => G8MoreAboutYouPage (ctx)
        // S3
        case G1YourPartnerPersonalDetailsPage.title => G1YourPartnerPersonalDetailsPage (ctx)
        // S4
        case G1TheirPersonalDetailsPage.title => G1TheirPersonalDetailsPage (ctx)
        case G2TheirContactDetailsPage.title => G2TheirContactDetailsPage (ctx)
        case G7MoreAboutTheCarePage.title => G7MoreAboutTheCarePage (ctx)
      }.orElse[String,Page]{
        IterableNode(Breaks,ctx)(iteration =>{
          case G10BreaksInCarePage.title => G10BreaksInCarePage (ctx, iteration)
          case G11BreakPage.title => G11BreakPage (ctx, iteration)
        })
      }.orElse[String,Page]{
        //S6
        case G1YourCourseDetailsPage.title => G1YourCourseDetailsPage (ctx)
        // S7 - guard question(s)
        case G1EmploymentPage.title => G1EmploymentPage (ctx)
      }.orElse[String,Page]{
        IterableNode(Employment,ctx)(iteration =>{
          // S7
          case G2BeenEmployedPage.title => G2BeenEmployedPage (ctx,iteration)
          case G3JobDetailsPage.title => G3JobDetailsPage (ctx,iteration)
          case G4EmployerContactDetailsPage.title => G4EmployerContactDetailsPage (ctx,iteration)
          case G5LastWagePage.title => G5LastWagePage (ctx,iteration)
          case G6AdditionalWageDetailsPage.title => G6AdditionalWageDetailsPage (ctx,iteration)
          case G7PensionSchemesPage.title => G7PensionSchemesPage (ctx,iteration)
          case G8AboutExpensesPage.title => G8AboutExpensesPage (ctx,iteration)
          case G9NecessaryExpensesPage.title => G9NecessaryExpensesPage (ctx,iteration)
          case G10ChildcareExpensesPage.title => G10ChildcareExpensesPage (ctx,iteration)
          case G12PersonYouCareForExpensesPage.title => G12PersonYouCareForExpensesPage (ctx,iteration)
        })
      }.orElse[String,Page]{
        // S8
        case G1AboutOtherMoneyPage.title => G1AboutOtherMoneyPage (ctx)

        // S9
        case G1HowWePayYouPage.title => G1HowWePayYouPage (ctx)
        case G2BankBuildingSocietyDetailsPage.title => G2BankBuildingSocietyDetailsPage (ctx)
        case G3PayDetailsCompletedPage.title => G3PayDetailsCompletedPage (ctx)
        // S9
        case G1AboutSelfEmploymentPage.title => G1AboutSelfEmploymentPage (ctx)
        case G2SelfEmploymentYourAccountsPage.title => G2SelfEmploymentYourAccountsPage (ctx)
        case G4SelfEmploymentPensionsAndExpensesPage.title => G4SelfEmploymentPensionsAndExpensesPage (ctx)
        case G5ChildcareExpensesWhileAtWorkPage.title => G5ChildcareExpensesWhileAtWorkPage (ctx)
        case G7ExpensesWhileAtWorkPage.title => G7ExpensesWhileAtWorkPage (ctx)
        case PreviewPage.title => PreviewPage(ctx)
        // S10
        case G1AdditionalInfoPage.title => G1AdditionalInfoPage (ctx)
        case G2ConsentPage.title => G2ConsentPage (ctx)
        case G3DisclaimerPage.title => G3DisclaimerPage (ctx)
        case G4DeclarationPage.title => G4DeclarationPage (ctx)
        case G5SubmitPage.title => G5SubmitPage (ctx)
        // Catch pages not covered by framework
        case _ => new UnknownPage(title, ctx)
      }(title.toLowerCase)
    }
  }
}

object IterableNode {
  def apply[A,B](section:String,ctx:PageObjectsContext)(func:Int => PartialFunction[A,B]):PartialFunction[A,B] = {
    func(ctx.iterationManager(section))
  }
}
