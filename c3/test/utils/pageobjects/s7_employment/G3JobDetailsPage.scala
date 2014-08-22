package utils.pageobjects.s7_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G3JobDetailsPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, G3JobDetailsPage.url.replace(":jobID", iteration.toString), G3JobDetailsPage.title, iteration) {
  declareInput("#employerName", "EmploymentEmployerName_" + iteration)
  declareInput("#phoneNumber","EmploymentEmployerPhoneNumber_" + iteration)
  declareAddress("#address", "EmploymentEmployerAddress_" + iteration)
  declareInput("#postcode","EmploymentEmployerPostcode_" + iteration)
  declareDate("#jobStartDate", "EmploymentWhenDidYouStartYourJob_" + iteration)
  declareYesNo("#finishedThisJob", "EmploymentHaveYouFinishedThisJob_" + iteration)
  declareDate("#lastWorkDate", "EmploymentWhenDidYouLastWork_" + iteration)
  declareDate("#p45LeavingDate","EmploymentLeavingDateP45_"+iteration)
  declareInput("#hoursPerWeek", "EmploymentHowManyHoursAWeekYouNormallyWork_" + iteration)
}

object G3JobDetailsPage {
  val title = "Employer Details - Employment History".toLowerCase

  val url  = "/employment/job-details/:jobID"

  def apply(ctx:PageObjectsContext, iteration:Int=1) = new G3JobDetailsPage(ctx, iteration)
}

trait G3JobDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G3JobDetailsPage (PageObjectsContext(browser), iteration = 1)
}