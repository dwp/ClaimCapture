package utils.pageobjects.breaks_in_care

import controllers.mappings.Mappings
import utils.WithBrowser
import utils.pageobjects._

class GBreaksInCareOtherPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, GBreaksInCareOtherPage.url+"/"+iteration, iteration) {
  declareDate("#caringEnded_date", "AboutTheCareYouProvideBreakEndDate_" + iteration)
  declareInput("#caringEnded_time", "AboutTheCareYouProvideBreakEndTime_" + iteration)
  declareYesNo("#caringStarted_answer","AboutTheCareYouProvideBreakStartAnswer_"+iteration)
  declareDate("#caringStarted_date", "AboutTheCareYouProvideBreakStartDate_" + iteration)
  declareInput("#caringStarted_time", "AboutTheCareYouProvideBreakStartTime_" + iteration)

  declareRadioList("#whereWasDp_answer","AboutTheCareYouProvideBreakWhereWasDp_"+iteration)
  declareInput("#whereWasDp_text", "AboutTheCareYouProvideBreakWhereWasDpText_" + iteration)

  declareRadioList("#whereWereYou_answer","AboutTheCareYouProvideBreakWhereWereYou_"+iteration)
  declareInput("#whereWereYou_text", "AboutTheCareYouProvideBreakWhereWereYouText_" + iteration)

  protected override def getNewIterationNumber = {
    import IterationManager._
    ctx.iterationManager.increment(Breaks)
  }
}

object GBreaksInCareOtherPage {
  val url  = "/breaks/other-breaks"

  def apply(ctx:PageObjectsContext, iteration:Int=1) = new GBreaksInCareOtherPage(ctx,iteration)

  def fillDetails(context: PageObjectsContext, f: => TestData => Unit) = {
    val claimData = defaultOtherDetails()
    f(claimData)
    val page = new GBreaksInCareOtherPage(context, 1) goToThePage()
    page.fillPageWith(claimData)
    page.submitPage()
  }

  private def defaultOtherDetails() = {
    val claim = new TestData
    claim.AboutTheCareYouProvideBreakEndDate_1 = "01/10/2015"
    claim.AboutTheCareYouProvideBreakStartAnswer_1 = Mappings.no
    claim
  }}

trait GBreaksInCareOtherPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GBreaksInCareOtherPage (PageObjectsContext(browser), iteration = 1)
}

