package utils.pageobjects.s_care_you_provide

import utils.WithBrowser
import utils.pageobjects._

/**
 * Page object for s_care_you_provide g1_their_personal_details.
 * @author Saqib Kayani
 *         Date: 25/07/2013
 */
final class GTheirPersonalDetailsPage (ctx:PageObjectsContext) extends ClaimPage(ctx, GTheirPersonalDetailsPage.url) {
  declareInput("#relationship","AboutTheCareYouProvideWhatTheirRelationshipToYou")
  declareRadioList("#title", "AboutTheCareYouProvideTitlePersonCareFor")
  declareInput("#titleOther", "AboutTheCareYouProvideTitleOtherPersonCareFor")
  declareInput("#firstName","AboutTheCareYouProvideFirstNamePersonCareFor")
  declareInput("#middleName", "AboutTheCareYouProvideMiddleNamePersonCareFor")
  declareInput("#surname", "AboutTheCareYouProvideSurnamePersonCareFor")
  declareNino("#nationalInsuranceNumber", "AboutTheCareYouProvideNINOPersonCareFor")
  declareDate("#dateOfBirth", "AboutTheCareYouProvideDateofBirthPersonYouCareFor")
  declareYesNo("#theirAddress_answer", "AboutTheCareYouProvideDoTheyLiveAtTheSameAddressAsYou")
  declareAddress("#theirAddress_address", "AboutTheCareYouProvideAddressPersonCareFor")
  declareInput("#theirAddress_postCode", "AboutTheCareYouProvidePostcodePersonCareFor")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GTheirPersonalDetailsPage {
  val url  = "/care-you-provide/their-personal-details"

  def apply(ctx:PageObjectsContext) = new GTheirPersonalDetailsPage(ctx)
}

/** The context for Specs tests */
trait GTheirPersonalDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GTheirPersonalDetailsPage (PageObjectsContext(browser))
}
