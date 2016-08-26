package utils.pageobjects.breaks_in_care

import app.BreaksInCareGatherOptions
import controllers.mappings.Mappings
import utils.WithBrowser
import utils.pageobjects._

class GBreaksInCareHospitalPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, GBreaksInCareHospitalPage.url+"/"+iteration, iteration) {
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

  def fillDetails(context: PageObjectsContext, f: => TestData => Unit) = {
    val claimData = defaultHospitalDetails()
    f(claimData)
    val page = new GBreaksInCareHospitalPage(context, 1) goToThePage()
    page.fillPageWith(claimData)
    page.submitPage()
  }

  private def defaultHospitalDetails() = {
    val claim = new TestData
    claim.AboutTheCareYouProvideBreakWhoWasInHospital_1 = BreaksInCareGatherOptions.You
    claim.AboutTheCareYouProvideBreakWhenWereYouAdmitted_1 = "01/01/2016"
    claim.AboutTheCareYouProvideYourStayEnded_1 = Mappings.no
    claim
  }
}

trait GBreaksInCareHospitalPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GBreaksInCareHospitalPage (PageObjectsContext(browser), iteration = 1)
}

