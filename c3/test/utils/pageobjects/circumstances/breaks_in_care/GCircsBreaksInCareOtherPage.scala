package utils.pageobjects.circumstances.breaks_in_care

import controllers.mappings.Mappings
import utils.WithBrowser
import utils.pageobjects._

class GCircsBreaksInCareOtherPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, GCircsBreaksInCareOtherPage.url+"/"+iteration, iteration) {
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
    ctx.iterationManager.increment(CircsBreaks)
  }
}

object GCircsBreaksInCareOtherPage {
  val url  = "/circumstances/breaks/other-breaks"

  def apply(ctx:PageObjectsContext, iteration:Int=1) = new GCircsBreaksInCareOtherPage(ctx,iteration)

  def fillDetails(context: PageObjectsContext, f: => TestData => Unit) = {
    val claimData = defaultOtherDetails()
    f(claimData)
    val page = new GCircsBreaksInCareOtherPage(context, 1) goToThePage()
    page.fillPageWith(claimData)
    page.submitPage()
  }

  def fillDetails2(context: PageObjectsContext, f: => TestData => Unit) = {
    val claimData = defaultOtherDetails2()
    f(claimData)
    val page = new GCircsBreaksInCareOtherPage(context, 2) goToThePage()
    page.fillPageWith(claimData)
    page.submitPage()
  }

  def fillDetails3(context: PageObjectsContext, f: => TestData => Unit) = {
    val claimData = defaultOtherDetails3()
    f(claimData)
    val page = new GCircsBreaksInCareOtherPage(context, 3) goToThePage()
    page.fillPageWith(claimData)
    page.submitPage()
  }

  private def defaultOtherDetails() = {
    val claim = new TestData
    claim.AboutTheCareYouProvideBreakEndDate_1 = "01/10/2015"
    claim.AboutTheCareYouProvideBreakStartAnswer_1 = Mappings.no
    claim
  }

  private def defaultOtherDetails2() = {
    val claim = new TestData
    claim.AboutTheCareYouProvideBreakEndDate_2 = "01/10/2015"
    claim.AboutTheCareYouProvideBreakStartAnswer_2 = Mappings.no
    claim
  }

  private def defaultOtherDetails3() = {
    val claim = new TestData
    claim.AboutTheCareYouProvideBreakEndDate_3 = "01/10/2015"
    claim.AboutTheCareYouProvideBreakStartAnswer_3 = Mappings.no
    claim
  }
}

trait GBreaksInCareOtherPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GCircsBreaksInCareOtherPage (PageObjectsContext(browser), iteration = 1)
}

