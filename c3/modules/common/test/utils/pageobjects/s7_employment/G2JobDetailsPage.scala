package utils.pageobjects.s7_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G2JobDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends ClaimPage(browser, G2JobDetailsPage.url.replace(":jobID", iteration.toString), G2JobDetailsPage.title, previousPage, iteration) {
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

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) = new G2JobDetailsPage(browser, previousPage, iteration)
}

trait G2JobDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G2JobDetailsPage buildPageWith (browser, iteration = 1)
}