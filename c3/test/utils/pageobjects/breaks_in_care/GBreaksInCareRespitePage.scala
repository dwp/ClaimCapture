package utils.pageobjects.breaks_in_care

import app.BreaksInCareGatherOptions
import controllers.mappings.Mappings
import utils.WithBrowser
import utils.pageobjects.IterationManager._
import utils.pageobjects._

class GBreaksInCareRespitePage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, GBreaksInCareRespitePage.url+"/"+iteration, iteration) {
  declareRadioList("#whoWasInRespite", "AboutTheCareYouProvideBreakWhoWasInRespite_" + iteration)

  declareDate("#whenWereYouAdmitted", "AboutTheCareYouProvideBreakWhenWereYouAdmitted_" + iteration)
  declareYesNo("#yourRespiteStayEnded_answer", "AboutTheCareYouProvideYourStayEnded_" + iteration)
  declareDate("#yourRespiteStayEnded_date", "AboutTheCareYouProvideYourStayEndedDate_" + iteration)

  declareDate("#whenWasDpAdmitted", "AboutTheCareYouProvideBreakWhenWasDpAdmitted_" + iteration)
  declareYesNo("#dpRespiteStayEnded_answer", "AboutTheCareYouProvideDpStayEnded_" + iteration)
  declareDate("#dpRespiteStayEnded_date", "AboutTheCareYouProvideDpStayEndedDate_" + iteration)

  declareYesNo("#yourMedicalProfessional", "AboutTheCareYouProvideYourMedicalProfessional_" + iteration)
  declareYesNo("#dpMedicalProfessional", "AboutTheCareYouProvideDpMedicalProfessional_" + iteration)

  protected override def getNewIterationNumber = {
    import IterationManager._
    ctx.iterationManager.increment(Breaks)
  }
}

object GBreaksInCareRespitePage {
  val url = "/breaks/respite-care-home"

  def apply(ctx: PageObjectsContext, iteration: Int = 1) = new GBreaksInCareRespitePage(ctx, iteration)

  def fillDetails(context: PageObjectsContext, f: => TestData => Unit) = {
    val claimData = defaultRespiteDetails()
    f(claimData)
    val page = new GBreaksInCareRespitePage(context, 1) goToThePage()
    page.fillPageWith(claimData)
    page.submitPage()
    println("Respite page:"+page.source)
  }

  private def defaultRespiteDetails() = {
    val claim = new TestData
    claim.AboutTheCareYouProvideBreakWhoWasInRespite_1 = BreaksInCareGatherOptions.You
    claim.AboutTheCareYouProvideBreakWhenWereYouAdmitted_1 = "01/10/2015"
    claim.AboutTheCareYouProvideYourStayEnded_1 = Mappings.no
    claim.AboutTheCareYouProvideYourMedicalProfessional_1 = Mappings.no
    claim
  }
}

trait GBreaksInCareRespitePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GBreaksInCareRespitePage(PageObjectsContext(browser), iteration = 1)
}

