package utils.pageobjects.s_breaks

import utils.WithBrowser
import utils.pageobjects._

final class GBreakPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, GBreakPage.url, iteration) {
  declareDate("#start", "AboutTheCareYouProvideBreakStartDate_" + iteration)
  declareYesNo("#hasBreakEnded_answer","AboutTheCareYouProvideHasBreakEnded_"+iteration)
  declareDate("#hasBreakEnded_date", "AboutTheCareYouProvideBreakEndDate_" + iteration)
  declareInput("#startTime", "AboutTheCareYouProvideBreakStartTime_" + iteration)
  declareInput("#endTime", "AboutTheCareYouProvideBreakEndTime_" + iteration)
  declareRadioList("#whereYou_answer", "AboutTheCareYouProvideWhereWereYouDuringTheBreak_" + iteration)
  declareInput("#whereYou_text", "AboutTheCareYouProvideWhereWereYouDuringTheBreakOther_" + iteration)
  declareRadioList("#wherePerson_answer", "AboutTheCareYouProvideWhereWasThePersonYouCareForDuringtheBreak_" + iteration)
  declareInput("#wherePerson_text", "AboutTheCareYouProvideWhereWasThePersonYouCareForDuringtheBreakOther_" + iteration)
  declareYesNo("#medicalDuringBreak", "AboutTheCareYouProvideDidYouOrthePersonYouCareForGetAnyMedicalTreatment_" + iteration)

  /**
   * Called by submitPage of Page. A new G10 will be built with an incremented iteration number.
   * @return Incremented iteration number.
   */
  protected override def getNewIterationNumber = {
    import IterationManager._
    ctx.iterationManager.increment(Breaks)
  }
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object GBreakPage {
  val url  = "/breaks/break"

  def apply(ctx:PageObjectsContext, iteration:Int=1) = new GBreakPage(ctx,iteration)
}

/** The context for Specs tests */
trait GBreakPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GBreakPage (PageObjectsContext(browser), iteration = 1)
}
