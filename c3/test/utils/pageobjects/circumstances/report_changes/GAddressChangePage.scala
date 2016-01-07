package utils.pageobjects.circumstances.report_changes

import utils.pageobjects.{PageContext, CircumstancesPage, PageObjectsContext}
import utils.WithBrowser

/**
 * Created by neddakaltcheva on 2/14/14.
 */
final class GAddressChangePage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, GAddressChangePage.url) {
  declareAddress("#previousAddress", "CircumstancesAddressChangePreviousAddress")
  declareInput("#previousPostcode", "CircumstancesAddressChangePreviousPostcode")
  declareYesNo("#stillCaring_answer", "CircumstancesAddressChangeStillCaring")
  declareDate("#stillCaring_date", "CircumstancesAddressChangeFinishedStillCaringDate")
  declareAddress("#newAddress", "CircumstancesAddressChangeNewAddress")
  declareInput("#newPostcode", "CircumstancesAddressChangeNewPostcode")
  declareYesNo("#caredForChangedAddress_answer", "CircumstancesAddressChangeCaredForChangedAddress")
  declareYesNo("#sameAddress_answer", "CircumstancesAddressChangeSameAddress")
  declareAddress("#sameAddress_theirNewAddress", "CircumstancesAddressChangeSameAddressTheirAddress")
  declareInput("#sameAddress_theirNewPostcode", "CircumstancesAddressChangeSameAddressTheirPostcode")
  declareInput("#moreAboutChanges", "CircumstancesAddressChangeMoreAboutChanges")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GAddressChangePage {
  val url  = "/circumstances/report-changes/address-change"

  def apply(ctx:PageObjectsContext) = new GAddressChangePage(ctx)
}

/** The context for Specs tests */
trait GAddressChangePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GAddressChangePage(PageObjectsContext(browser))
}
