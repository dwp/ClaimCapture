package utils.pageobjects.breaks_in_care

import utils.WithBrowser
import utils.pageobjects.{IterationManager, PageContext, ClaimPage, PageObjectsContext}

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
}

trait GBreaksInCareOtherPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GBreaksInCareOtherPage (PageObjectsContext(browser), iteration = 1)
}

