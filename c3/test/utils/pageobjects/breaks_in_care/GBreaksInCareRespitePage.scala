package utils.pageobjects.breaks_in_care

import utils.WithBrowser
import utils.pageobjects.IterationManager._
import utils.pageobjects.{IterationManager, PageContext, ClaimPage, PageObjectsContext}

class GBreaksInCareRespitePage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, GBreaksInCareRespitePage.url) {
  declareRadioList("#whoWasInRespite", "AboutTheCareYouProvideBreakWhoWasInRespite_" + iteration)

  declareDate("#whenWereYouAdmitted", "AboutTheCareYouProvideBreakWhenWereYouAdmitted_" + iteration)
  declareYesNo("#yourRespiteStayEnded_answer","AboutTheCareYouProvideYourStayEnded_"+iteration)
  declareDate("#yourRespiteStayEnded_date", "AboutTheCareYouProvideYourStayEndedDate_" + iteration)

  declareDate("#whenWasDpAdmitted", "AboutTheCareYouProvideBreakWhenWasDpAdmitted_" + iteration)
  declareYesNo("#dpRespiteStayEnded_answer","AboutTheCareYouProvideDpStayEnded_"+iteration)
  declareDate("#dpRespiteStayEnded_date", "AboutTheCareYouProvideDpStayEndedDate_" + iteration)

  declareYesNo("#yourMedicalProfessional", "AboutTheCareYouProvideYourMedicalProfessional_" + iteration)
  declareYesNo("#dpMedicalProfessional", "AboutTheCareYouProvideDpMedicalProfessional_" + iteration)

  protected override def getNewIterationNumber = {
    import IterationManager._
    ctx.iterationManager.increment(Breaks)
  }
}

object GBreaksInCareRespitePage {
  val url  = "/breaks/respite-care-home"

  def apply(ctx:PageObjectsContext, iteration:Int=1) = new GBreaksInCareRespitePage(ctx,iteration)
}

trait GBreaksInCareRespitePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GBreaksInCareRespitePage (PageObjectsContext(browser), iteration = 1)
}

