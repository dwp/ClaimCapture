package utils.pageobjects.circumstances.s2_report_changes

import utils.pageobjects.{PageContext, CircumstancesPage, PageObjectsContext}
import play.api.test.WithBrowser

/**
 * Created by neddakaltcheva on 2/14/14.
 */
final class G6AddressChangePage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G6AddressChangePage.url, G6AddressChangePage.title) {
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
object G6AddressChangePage {
  val title = "Change of address - change in circumstances".toLowerCase

  val url  = "/circumstances/report-changes/address-change"

  def apply(ctx:PageObjectsContext) = new G6AddressChangePage(ctx)
}

/** The context for Specs tests */
trait G6AddressChangePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G6AddressChangePage(PageObjectsContext(browser))
}