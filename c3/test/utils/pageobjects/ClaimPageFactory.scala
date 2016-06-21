package utils.pageobjects

import utils.pageobjects.s_breaks.{GBreaksInCarePage, GBreakPage}
import utils.pageobjects.save_for_later.{GSaveForLaterResumePage, GSaveForLaterSavePage}
import utils.pageobjects.third_party.GThirdPartyPage
import utils.pageobjects.your_income._
import scala.language.dynamics
import utils.pageobjects.s_eligibility._
import utils.pageobjects.s_disclaimer._
import utils.pageobjects.s_about_you._
import utils.pageobjects.s_your_partner._
import utils.pageobjects.s_care_you_provide._
import utils.pageobjects.s_education._
import utils.pageobjects.s_employment._
import utils.pageobjects.s_self_employment._
import utils.pageobjects.s_pay_details._
import utils.pageobjects.s_information._
import utils.pageobjects.s_consent_and_declaration._
import utils.pageobjects.IterationManager._
import utils.pageobjects.preview.PreviewPage
import utils.pageobjects.s_claim_date.GClaimDatePage
import utils.pageobjects.common._

/**
 * Factory used by Page to create from an html page the right page object.
 * If there is no Page Object mapping to the title then it creates an instance of UnknownPage.
  *
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
      case GBenefitsPage.url => GBenefitsPage(ctx)
      case GEligibilityPage.url => GEligibilityPage(ctx)
      case G5CarersResponsePage.url => G5CarersResponsePage(ctx)
      case GApprovePage.url => GApprovePage(ctx)
      case GThirdPartyPage.url => GThirdPartyPage(ctx)
      //S1.5
      case GClaimDatePage.url => GClaimDatePage(ctx)
      // S2
      case GYourDetailsPage.url => GYourDetailsPage(ctx)
      case GMaritalStatusPage.url => GMaritalStatusPage(ctx)
      case GContactDetailsPage.url => GContactDetailsPage(ctx)
      case GNationalityAndResidencyPage.url => GNationalityAndResidencyPage(ctx)
      //Save
      case GSaveForLaterSavePage.url => GSaveForLaterSavePage(ctx)
      //Resume
      case GSaveForLaterResumePage.url => GSaveForLaterResumePage(ctx)
    }
    m.orElse[String, Page] {
      case GPaymentsFromAbroadPage.url => GPaymentsFromAbroadPage(ctx)
      // S3
      case GYourPartnerPersonalDetailsPage.url => GYourPartnerPersonalDetailsPage(ctx)
      // S4
      case GTheirPersonalDetailsPage.url => GTheirPersonalDetailsPage(ctx)
      case GMoreAboutTheCarePage.url => GMoreAboutTheCarePage(ctx)
    }.orElse[String, Page] {
      IterableNode(Breaks, ctx)(iteration => {
        case GBreaksInCarePage.url => GBreaksInCarePage(ctx, iteration)
        case GBreakPage.url => GBreakPage(ctx, iteration)
      })
    }.orElse[String, Page] {
      //S6
      case GYourCourseDetailsPage.url => GYourCourseDetailsPage(ctx)
      // S7 - guard question(s)
      case GYourIncomePage.url => GYourIncomePage(ctx)
    }.orElse[String, Page] {
      IterableNode(Employment, ctx)(iteration => {
        // S7
        case GBeenEmployedPage.url => GBeenEmployedPage(ctx, iteration)
        case GJobDetailsPage.url => GJobDetailsPage(ctx, iteration)
        case GLastWagePage.url => GLastWagePage(ctx, iteration)
        case GPensionAndExpensesPage.url => GPensionAndExpensesPage(ctx, iteration)
      })
    }.orElse[String, Page] {
      // s7 - Used both by self-employment and employment
      case GEmploymentAdditionalInfoPage.url => GEmploymentAdditionalInfoPage(ctx)
      // S8
      case GStatutorySickPayPage.url => GStatutorySickPayPage(ctx)
      case GStatutoryMaternityPaternityAdoptionPayPage.url => GStatutoryMaternityPaternityAdoptionPayPage(ctx)
      case GFosteringAllowancePage.url => GFosteringAllowancePage(ctx)
      case GDirectPaymentPage.url => GDirectPaymentPage(ctx)
      case GOtherPaymentsPage.url => GOtherPaymentsPage(ctx)
      // S9
      case GHowWePayYouPage.url => GHowWePayYouPage(ctx)
      // S9
      case GSelfEmploymentDatesPage.url => GSelfEmploymentDatesPage(ctx)
      case GSelfEmploymentPensionsAndExpensesPage.url => GSelfEmploymentPensionsAndExpensesPage(ctx)
      case PreviewPage.url => PreviewPage(ctx)
      // S10
      case GAdditionalInfoPage.url => GAdditionalInfoPage(ctx)
      case GDisclaimerPage.url => GDisclaimerPage(ctx)
      case GDeclarationPage.url =>
        if (ctx.browser.pageSource() contains "DWPBody") XmlPage(ctx)
        else GDeclarationPage(ctx)
      case ClaimHelpPage.url => ClaimHelpPage(ctx)
      // Catch pages not covered by framework
      case _ =>
        if (previousUrl.isEmpty) buildPageFromUrImpl(url.replaceFirst("/[^/]*$", ""), ctx, url)
        else new UnknownPage(previousUrl, ctx)
    }(url.replaceFirst("\\?.*$", "").replaceFirst("\\#.*$", ""))
  }
}

object IterableNode {
  def apply[A, B](section: String, ctx: PageObjectsContext)(func: Int => PartialFunction[A, B]): PartialFunction[A, B] = {
    func(ctx.iterationManager(section))
  }
}
