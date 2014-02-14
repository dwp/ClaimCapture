package utils.pageobjects.circumstances.s2_report_changes

import utils.pageobjects.{PageContext, CircumstancesPage, PageObjectsContext}
import play.api.test.WithBrowser

/**
 * Created by neddakaltcheva on 2/14/14.
 */
final class G6AddressChangePage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, G6AddressChangePage.url, G6AddressChangePage.title) {
  declareYesNo("#stillCaring", "CircumstancesSelfEmploymentStillCaring")
  declareDate("#stillCaring_date", "CircumstancesSelfEmploymentFinishedStillCaringDate")
  declareDate("#whenThisSelfEmploymentStarted", "CircumstancesSelfEmploymentWhenThisStarted")
  declareInput("#typeOfBusiness", "CircumstancesSelfEmploymentTypeOfBusiness")
  declareYesNoDontKnow("#totalOverWeeklyIncomeThreshold", "CircumstancesSelfEmploymentTotalOverWeeklyIncomeThreshold")
  declareInput("#moreAboutChanges", "CircumstancesSelfEmploymentCaringMoreAboutChanges")
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