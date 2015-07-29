package utils.pageobjects.s4_care_you_provide

import utils.WithBrowser
import utils.pageobjects._

final class G11BreakPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, G11BreakPage.url, iteration) {
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
object G11BreakPage {
  val url  = "/care-you-provide/breaks"

  def apply(ctx:PageObjectsContext, iteration:Int=1) = new G11BreakPage(ctx,iteration)
}

/** The context for Specs tests */
trait G11BreakPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G11BreakPage (PageObjectsContext(browser), iteration = 1)
}