package utils.pageobjects.s7_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{ClaimPage,Page, PageContext}

final class G3JobDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends ClaimPage(browser, G3JobDetailsPage.url.replace(":jobID", iteration.toString), G3JobDetailsPage.title, previousPage, iteration) {
  declareInput("#employerName", "EmploymentEmployerName_" + iteration)
  declareDate("#jobStartDate", "EmploymentWhenDidYouStartYourJob_" + iteration)
  declareYesNo("#finishedThisJob", "EmploymentHaveYouFinishedThisJob_" + iteration)
  declareDate("#lastWorkDate", "EmploymentWhenDidYouLastWork_" + iteration)
  declareDate("#p45LeavingDate","EmploymentLeavingDateP45_"+iteration)
  declareInput("#hoursPerWeek", "EmploymentHowManyHoursAWeekYouNormallyWork_" + iteration)
  declareInput("#payrollEmployeeNumber", "EmploymentPayrollOrEmployeeNumber_" + iteration)
}

object G3JobDetailsPage {
  val title = "Your job - Employment History".toLowerCase

  val url  = "/employment/job-details/:jobID"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) = new G3JobDetailsPage(browser, previousPage, iteration)
}

trait G3JobDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G3JobDetailsPage (browser, iteration = 1)
}