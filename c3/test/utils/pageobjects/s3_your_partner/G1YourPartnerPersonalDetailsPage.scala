package utils.pageobjects.s3_your_partner

import utils.WithBrowser
import utils.pageobjects._

/**
 * PageObject for page s3_your_partner g1_yourPartnerPersonalDetails.
 * @author Jorge Migueis
 *         Date: 19/07/2013
 */
final class G1YourPartnerPersonalDetailsPage (ctx:PageObjectsContext) extends ClaimPage(ctx, G1YourPartnerPersonalDetailsPage.url) {
  declareYesNo("#hadPartnerSinceClaimDate", "AboutYourPartnerHadPartnerSinceClaimDate")
  declareSelect("#title", "AboutYourPartnerTitle")
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
object G1YourPartnerPersonalDetailsPage {
  val url  = "/your-partner/personal-details"

  def apply(ctx:PageObjectsContext) = new G1YourPartnerPersonalDetailsPage(ctx)
}

/** The context for Specs tests */
trait G1YourPartnerPersonalDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G1YourPartnerPersonalDetailsPage (PageObjectsContext(browser))
}