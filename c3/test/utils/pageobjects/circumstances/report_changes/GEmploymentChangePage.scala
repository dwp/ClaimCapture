package utils.pageobjects.circumstances.report_changes

import controllers.mappings.Mappings
import utils.pageobjects.{TestData, PageContext, CircumstancesPage, PageObjectsContext}
import utils.WithBrowser

final class GEmploymentChangePage(ctx:PageObjectsContext) extends CircumstancesPage(ctx, GEmploymentChangePage.url) {
  declareYesNo("#stillCaring_answer", "CircumstancesEmploymentChangeStillCaring")
  declareDate("#stillCaring_date", "CircumstancesEmploymentChangeFinishedStillCaringDate")
  declareYesNo("#hasWorkStartedYet_answer", "CircumstancesEmploymentChangeHasWorkStartedYet")
  declareDate("#hasWorkStartedYet_dateWhenStarted", "CircumstancesEmploymentChangeDateWhenStarted")
  declareDate("#hasWorkStartedYet_dateWhenWillItStart", "CircumstancesEmploymentChangeDateWhenWillItStart")
  declareYesNo("#hasWorkFinishedYet_answer", "CircumstancesEmploymentChangeHasWorkFinishedYet")
  declareDate("#hasWorkFinishedYet_dateWhenFinished", "CircumstancesEmploymentChangeDateWhenFinished")
  declareRadioList("#typeOfWork_answer", "CircumstancesEmploymentChangeTypeOfWork")
  declareInput("#typeOfWork_selfEmployedTypeOfWork", "CircumstancesEmploymentChangeSelfEmployedTypeOfWork")
  declareYesNo("#paidMoneyYet_answer", "CircumstancesEmploymentChangeSelfEmployedPaidMoneyYet")
  declareDate("#paidMoneyYet_date", "CircumstancesEmploymentChangeSelfEmployedPaidMoneyDate")
  declareYesNoDontKnow("#typeOfWork_selfEmployedTotalIncome", "CircumstancesEmploymentChangeSelfEmployedTotalIncome")
  declareInput("#typeOfWork_selfEmployedMoreAboutChanges", "CircumstancesEmploymentChangeSelfEmployedMoreAboutChanges")
  declareInput("#typeOfWork_employerName", "CircumstancesEmploymentChangeEmployerName")
  declareAddress("#typeOfWork_employerNameAndAddress", "CircumstancesEmploymentChangeEmployerNameAndAddress")
  declareInput("#typeOfWork_employerPostcode", "CircumstancesEmploymentChangeEmployerPostcode")
  declareInput("#typeOfWork_employerContactNumber", "CircumstancesEmploymentChangeEmployerContactNumber")
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GEmploymentChangePage {
  val url  = "/circumstances/report-changes/employment-change"

  def apply(ctx:PageObjectsContext) = new GEmploymentChangePage(ctx)

  def fillPresentJobDetails(context: PageObjectsContext, f: => TestData => Unit) = {
    val claimData = new TestData
    claimData.CircumstancesEmploymentChangeStillCaring = Mappings.yes
    claimData.CircumstancesEmploymentChangeHasWorkStartedYet = Mappings.yes
    claimData.CircumstancesEmploymentChangeDateWhenStarted = "01/01/2013"
    claimData.CircumstancesEmploymentChangeHasWorkFinishedYet = Mappings.no
    claimData.CircumstancesEmploymentChangeTypeOfWork = "employed"
    claimData.CircumstancesEmploymentChangeEmployerName = "Asda"
    claimData.CircumstancesEmploymentChangeEmployerNameAndAddress = "Fulwood&Preston"
    claimData.CircumstancesEmploymentChangeHowOftenFrequency = "Weekly"

    f(claimData)
    val page = new GEmploymentChangePage(context) goToThePage()
    page.fillPageWith(claimData).submitPage()
  }

  def fillPastJobDetails(context: PageObjectsContext, f: => TestData => Unit) = {
    val claimData = new TestData
    claimData.CircumstancesEmploymentChangeStillCaring = Mappings.yes
    claimData.CircumstancesEmploymentChangeHasWorkStartedYet = Mappings.yes
    claimData.CircumstancesEmploymentChangeDateWhenStarted = "01/01/2013"
    claimData.CircumstancesEmploymentChangeHasWorkFinishedYet = Mappings.yes
    claimData.CircumstancesEmploymentChangeDateWhenFinished = "28/02/2013"
    claimData.CircumstancesEmploymentChangeTypeOfWork = "employed"
    claimData.CircumstancesEmploymentChangeEmployerName = "Asda"
    claimData.CircumstancesEmploymentChangeEmployerNameAndAddress = "Fulwood&Preston"

    f(claimData)
    val page = new GEmploymentChangePage(context) goToThePage()
    page.fillPageWith(claimData).submitPage()
  }

  def fillFutureJobDetails(context: PageObjectsContext, f: => TestData => Unit) = {
    val claimData = new TestData
    claimData.CircumstancesEmploymentChangeStillCaring = Mappings.yes
    claimData.CircumstancesEmploymentChangeHasWorkStartedYet = Mappings.no
    claimData.CircumstancesEmploymentChangeDateWhenWillItStart = "01/01/2030"
    claimData.CircumstancesEmploymentChangeTypeOfWork = "employed"
    claimData.CircumstancesEmploymentChangeEmployerName = "Asda"
    claimData.CircumstancesEmploymentChangeEmployerNameAndAddress = "Fulwood&Preston"

    f(claimData)
    val page = new GEmploymentChangePage(context) goToThePage()
    page.fillPageWith(claimData).submitPage()
  }
}

/** The context for Specs tests */
trait GEmploymentChangePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GEmploymentChangePage(PageObjectsContext(browser))
}
