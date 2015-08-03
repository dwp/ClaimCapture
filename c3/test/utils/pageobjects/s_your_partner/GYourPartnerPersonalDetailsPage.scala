package utils.pageobjects.s_your_partner

import utils.WithBrowser
import utils.pageobjects._

/**
 * PageObject for page s_your_partner g_yourPartnerPersonalDetails.
 * @author Jorge Migueis
 *         Date: 19/07/2013
 */
final class GYourPartnerPersonalDetailsPage (ctx:PageObjectsContext) extends ClaimPage(ctx, GYourPartnerPersonalDetailsPage.url) {
  declareYesNo("#hadPartnerSinceClaimDate", "AboutYourPartnerHadPartnerSinceClaimDate")
  declareRadioList("#title", "AboutYourPartnerTitle")
  declareInput("#titleOther", "AboutYourPartnerTitleOther")
  declareInput("#firstName", "AboutYourPartnerFirstName")
  declareInput("#middleName", "AboutYourPartnerMiddleName")
  declareInput("#surname", "AboutYourPartnerSurname")
  declareInput("#otherNames", "AboutYourPartnerOtherNames")
  declareNino("#nationalInsuranceNumber", "AboutYourPartnerNINO")
  declareDate("#dateOfBirth", "AboutYourPartnerDateofBirth")
  declareInput("#nationality", "AboutYourPartnerNationality")
  //declareYesNo("#liveAtSameAddress", "AboutYourPartnerDoesYourPartnerLiveAtTheSameAddressAsYou")
  declareYesNo("#separated_fromPartner", "AboutYourPartnerHaveYouSeparatedfromYourPartner")
  declareYesNo("#isPartnerPersonYouCareFor", "AboutYourPartnerIsYourPartnerThePersonYouAreClaimingCarersAllowancefor")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GYourPartnerPersonalDetailsPage {
  val url  = "/your-partner/personal-details"

  def apply(ctx:PageObjectsContext) = new GYourPartnerPersonalDetailsPage(ctx)
}

/** The context for Specs tests */
trait GYourPartnerPersonalDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GYourPartnerPersonalDetailsPage (PageObjectsContext(browser))
}