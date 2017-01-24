package utils.pageobjects.circumstances.breaks_in_care

import app.BreaksInCareGatherOptions
import controllers.mappings.Mappings
import utils.WithBrowser
import utils.pageobjects._

class GCircsBreaksInCareRespitePage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, GCircsBreaksInCareRespitePage.url+"/"+iteration, iteration) {
  declareRadioList("#whoWasInRespite", "AboutTheCareYouProvideBreakWhoWasInRespite_" + iteration)

  declareDate("#whenWereYouAdmitted", "AboutTheCareYouProvideBreakWhenWereYouAdmitted_" + iteration)
  declareYesNo("#yourRespiteStayEnded_answer", "AboutTheCareYouProvideYourStayEnded_" + iteration)
  declareDate("#yourRespiteStayEnded_date", "AboutTheCareYouProvideYourStayEndedDate_" + iteration)

  declareDate("#whenWasDpAdmitted", "AboutTheCareYouProvideBreakWhenWasDpAdmitted_" + iteration)
  declareYesNo("#dpRespiteStayEnded_answer", "AboutTheCareYouProvideDpStayEnded_" + iteration)
  declareDate("#dpRespiteStayEnded_date", "AboutTheCareYouProvideDpStayEndedDate_" + iteration)

  declareYesNo("#yourMedicalProfessional", "AboutTheCareYouProvideYourMedicalProfessional_" + iteration)
  declareYesNo("#dpMedicalProfessional", "AboutTheCareYouProvideDpMedicalProfessional_" + iteration)

  declareRadioList("#expectToCareAgain_answer","AboutTheCareYouProvideBreakExpectToCareAgain_"+iteration)
  declareRadioList("#expectToCareAgain_answer2","AboutTheCareYouProvideBreakExpectToCareAgain2_"+iteration)

  protected override def getNewIterationNumber = {
    import IterationManager._
    ctx.iterationManager.increment(CircsBreaks)
  }
}

object GCircsBreaksInCareRespitePage {
  val url = "/circumstances/breaks/respite-care-home"

  def apply(ctx: PageObjectsContext, iteration: Int = 1) = new GCircsBreaksInCareRespitePage(ctx, iteration)

  def fillDetails(context: PageObjectsContext, f: => TestData => Unit) = {
    val claimData = defaultRespiteDetails1()
    f(claimData)
    val page = new GCircsBreaksInCareRespitePage(context, 1) goToThePage()
    page.fillPageWith(claimData)
    page.submitPage()
  }

  def fillDetails2(context: PageObjectsContext, f: => TestData => Unit) = {
    val claimData = defaultRespiteDetails2()
    f(claimData)
    val page = new GCircsBreaksInCareRespitePage(context, 2) goToThePage()
    page.fillPageWith(claimData)
    page.submitPage()
  }

  def fillDetails3(context: PageObjectsContext, f: => TestData => Unit) = {
    val claimData = defaultRespiteDetails3()
    f(claimData)
    val page = new GCircsBreaksInCareRespitePage(context, 3) goToThePage()
    page.fillPageWith(claimData)
    page.submitPage()
  }

  private def defaultRespiteDetails1() = {
    val claim = new TestData
    claim.AboutTheCareYouProvideBreakWhoWasInRespite_1 = BreaksInCareGatherOptions.You
    claim.AboutTheCareYouProvideBreakWhenWereYouAdmitted_1 = "01/10/2015"
    claim.AboutTheCareYouProvideYourStayEnded_1 = Mappings.no
    claim.AboutTheCareYouProvideBreakExpectToCareAgain_1 = Mappings.dontknow
    claim.AboutTheCareYouProvideYourMedicalProfessional_1 = Mappings.no
    claim
  }

  private def defaultRespiteDetails2() = {
    val claim = new TestData
    claim.AboutTheCareYouProvideBreakWhoWasInRespite_2 = BreaksInCareGatherOptions.You
    claim.AboutTheCareYouProvideBreakWhenWereYouAdmitted_2 = "01/10/2015"
    claim.AboutTheCareYouProvideYourStayEnded_2 = Mappings.no
    claim.AboutTheCareYouProvideBreakExpectToCareAgain_2 = Mappings.dontknow
    claim.AboutTheCareYouProvideYourMedicalProfessional_2 = Mappings.no
    claim
  }

  private def defaultRespiteDetails3() = {
    val claim = new TestData
    claim.AboutTheCareYouProvideBreakWhoWasInRespite_3 = BreaksInCareGatherOptions.You
    claim.AboutTheCareYouProvideBreakWhenWereYouAdmitted_3 = "01/10/2015"
    claim.AboutTheCareYouProvideYourStayEnded_3 = Mappings.no
    claim.AboutTheCareYouProvideBreakExpectToCareAgain_3 = Mappings.dontknow
    claim.AboutTheCareYouProvideYourMedicalProfessional_3 = Mappings.no
    claim
  }
}

trait GCircsBreaksInCareRespitePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GCircsBreaksInCareRespitePage(PageObjectsContext(browser), iteration = 1)
}

