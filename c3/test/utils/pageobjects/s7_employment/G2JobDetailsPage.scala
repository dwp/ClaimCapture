package utils.pageobjects.s7_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G2JobDetailsPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, G2JobDetailsPage.url.replace(":jobID", iteration.toString), G2JobDetailsPage.title, iteration) {
  declareInput("#employerName", "EmploymentEmployerName_" + iteration)
  declareDate("#jobStartDate", "EmploymentWhenDidYouStartYourJob_" + iteration)
  declareYesNo("#finishedThisJob", "EmploymentHaveYouFinishedThisJob_" + iteration)
  declareDate("#lastWorkDate", "EmploymentWhenDidYouLastWork_" + iteration)
  declareDate("#p45LeavingDate","EmploymentLeavingDateP45_"+iteration)
  declareInput("#hoursPerWeek", "EmploymentHowManyHoursAWeekYouNormallyWork_" + iteration)
  declareInput("#payrollEmployeeNumber", "EmploymentPayrollOrEmployeeNumber_" + iteration)
}

object G2JobDetailsPage {
  val title = "Your job - Employment History".toLowerCase

  val url  = "/employment/job-details/:jobID"

  def apply(ctx:PageObjectsContext, iteration:Int) = new G2JobDetailsPage(ctx, iteration)
}

trait G2JobDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2JobDetailsPage (PageObjectsContext(browser), iteration = 1)
}