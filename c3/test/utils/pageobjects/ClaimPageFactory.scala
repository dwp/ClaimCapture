package utils.pageobjects


import scala.language.dynamics
import utils.pageobjects.s0_carers_allowance._
import utils.pageobjects.s1_disclaimer._
import utils.pageobjects.s2_about_you._
import utils.pageobjects.s3_your_partner._
import utils.pageobjects.s4_care_you_provide._
import utils.pageobjects.s6_education._
import utils.pageobjects.s8_employment._
import utils.pageobjects.s7_self_employment._
import utils.pageobjects.s9_other_money._
import utils.pageobjects.s11_pay_details._
import utils.pageobjects.s10_information._
import utils.pageobjects.s12_consent_and_declaration._
import utils.pageobjects.IterationManager._
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s1_2_claim_date.G1ClaimDatePage
import utils.pageobjects.common._

/**
 * Factory used by Page to create from an html page the right page object.
 * If there is no Page Object mapping to the title then it creates an instance of UnknownPage.
 * @author Jorge Migueis
 *         Date: 10/07/2013
 */
object ClaimPageFactory extends PageFactory {


  def buildPageFromUrl(url: String, ctx: PageObjectsContext): Page = buildPageFromUrImpl(url, ctx)

  private def buildPageFromUrImpl(url: String, ctx: PageObjectsContext, previousUrl: String = ""): Page = {
    // Generic solution using mapping does not work because the objects should register themselves
    // and there is no way to get that registration triggered automatically when test are loaded.
    val m: PartialFunction[String, Page] = {
      // S1
      case G1BenefitsPage.url => G1BenefitsPage(ctx)
      case G2EligibilityPage.url => G2EligibilityPage(ctx)
      case G5CarersResponsePage.url => G5CarersResponsePage(ctx)
      case G6ApprovePage.url => G6ApprovePage(ctx)
      //S1.5
      case G1ClaimDatePage.url => G1ClaimDatePage(ctx)
      // S2
      case G1YourDetailsPage.url => G1YourDetailsPage(ctx)
      case G2MaritalStatusPage.url => G2MaritalStatusPage(ctx)
      case G3ContactDetailsPage.url => G3ContactDetailsPage(ctx)
      case G4NationalityAndResidencyPage.url => G4NationalityAndResidencyPage(ctx)
    }
    m.orElse[String, Page] {
      IterableNode(Abroad, ctx)(iteration => {
        case G5AbroadForMoreThan52WeeksPage.url => G5AbroadForMoreThan52WeeksPage(ctx, iteration)
      })
    }.orElse[String, Page] {
      case G7OtherEEAStateOrSwitzerlandPage.url => G7OtherEEAStateOrSwitzerlandPage(ctx)
      // S3
      case G1YourPartnerPersonalDetailsPage.url => G1YourPartnerPersonalDetailsPage(ctx)
      // S4
      case G1TheirPersonalDetailsPage.url => G1TheirPersonalDetailsPage(ctx)
      case G2TheirContactDetailsPage.url => G2TheirContactDetailsPage(ctx)
      case G7MoreAboutTheCarePage.url => G7MoreAboutTheCarePage(ctx)
    }.orElse[String, Page] {
      IterableNode(Breaks, ctx)(iteration => {
        case G10BreaksInCarePage.url => G10BreaksInCarePage(ctx, iteration)
        case G11BreakPage.url => G11BreakPage(ctx, iteration)
      })
    }.orElse[String, Page] {
      //S6
      case G1YourCourseDetailsPage.url => G1YourCourseDetailsPage(ctx)
      // S7 - guard question(s)
      case G1EmploymentPage.url => G1EmploymentPage(ctx)
    }.orElse[String, Page] {
      IterableNode(Employment, ctx)(iteration => {
        // S7
        case G2BeenEmployedPage.url => G2BeenEmployedPage(ctx, iteration)
        case G3JobDetailsPage.url => G3JobDetailsPage(ctx, iteration)
        case G5LastWagePage.url => G5LastWagePage(ctx, iteration)
        case G8PensionAndExpensesPage.url => G8PensionAndExpensesPage(ctx, iteration)
      })
    }.orElse[String, Page] {
      // s7 - Used both by self employment and employment
      case G9EmploymentAdditionalInfoPage.url => G9EmploymentAdditionalInfoPage(ctx)
      // S8
      case G1AboutOtherMoneyPage.url => G1AboutOtherMoneyPage(ctx)
      // S9
      case G1HowWePayYouPage.url => G1HowWePayYouPage(ctx)
      case G2BankBuildingSocietyDetailsPage.url => G2BankBuildingSocietyDetailsPage(ctx)
      // S9
      case G1AboutSelfEmploymentPage.url => G1AboutSelfEmploymentPage(ctx)
      case G2SelfEmploymentYourAccountsPage.url => G2SelfEmploymentYourAccountsPage(ctx)
      case G4SelfEmploymentPensionsAndExpensesPage.url => G4SelfEmploymentPensionsAndExpensesPage(ctx)
      case PreviewPage.url => PreviewPage(ctx)
      // S10
      case G1AdditionalInfoPage.url => G1AdditionalInfoPage(ctx)
      case G1DisclaimerPage.url => G1DisclaimerPage(ctx)
      case G3DeclarationPage.url =>
        if (ctx.browser.pageSource() contains "DWPBody") XmlPage(ctx)
        else G3DeclarationPage(ctx)
      case ClaimNotesPage.url => ClaimNotesPage(ctx)
      case ClaimHelpPage.url => ClaimHelpPage(ctx)
      // Catch pages not covered by framework
      case _ =>
        if (previousUrl.isEmpty) buildPageFromUrImpl(url.replaceFirst("/[^/]*$", ""), ctx, url)
        else new UnknownPage(previousUrl, ctx)
    }(url.replaceFirst("\\?.*$", ""))
  }
}

object IterableNode {
  def apply[A, B](section: String, ctx: PageObjectsContext)(func: Int => PartialFunction[A, B]): PartialFunction[A, B] = {
    func(ctx.iterationManager(section))
  }
}
