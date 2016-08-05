package utils.pageobjects.breaks_in_care

import utils.WithBrowser
import utils.pageobjects.IterationManager._
import utils.pageobjects.{IterationManager, PageContext, ClaimPage, PageObjectsContext}

class GBreaksInCareHospitalPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, GBreaksInCareHospitalPage.url) {
  declareRadioList("#whoWasInHospital", "AboutTheCareYouProvideBreakWhoWasInHospital_" + iteration)

  declareDate("#whenWereYouAdmitted", "AboutTheCareYouProvideBreakWhenWereYouAdmitted_" + iteration)
  declareYesNo("#yourStayEnded_answer","AboutTheCareYouProvideYourStayEnded_"+iteration)
  declareDate("#yourStayEnded_date", "AboutTheCareYouProvideYourStayEndedDate_" + iteration)

  declareDate("#whenWasDpAdmitted", "AboutTheCareYouProvideBreakWhenWasDpAdmitted_" + iteration)
  declareYesNo("#dpStayEnded_answer","AboutTheCareYouProvideDpStayEnded_"+iteration)
  declareDate("#dpStayEnded_date", "AboutTheCareYouProvideDpStayEndedDate_" + iteration)

  declareYesNo("#breaksInCareStillCaring", "AboutTheCareYouProvideBreaksInCareStillCaring_" + iteration)

  protected override def getNewIterationNumber = {
    import IterationManager._
    ctx.iterationManager.increment(Breaks)
  }
}

object GBreaksInCareHospitalPage {
  val url  = "/breaks/hospital"

  def apply(ctx:PageObjectsContext, iteration:Int=1) = new GBreaksInCareHospitalPage(ctx,iteration)
}

trait GBreaksInCareHospitalPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GBreaksInCareHospitalPage (PageObjectsContext(browser), iteration = 1)
}

