package utils.pageobjects.s_employment

import utils.WithBrowser
import utils.pageobjects._

final class GJobDetailsPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, s"${GJobDetailsPage.url}/${iteration.toString}", iteration) {
  declareInput("#employerName", "EmploymentEmployerName_" + iteration)
  declareInput("#phoneNumber","EmploymentEmployerPhoneNumber_" + iteration)
  declareAddress("#address", "EmploymentEmployerAddress_" + iteration)
  declareInput("#postcode","EmploymentEmployerPostcode_" + iteration)
  declareYesNo("#startJobBeforeClaimDate", "EmploymentDidYouStartThisJobBeforeClaimDate_" + iteration)
  declareDate("#jobStartDate", "EmploymentWhenDidYouStartYourJob_" + iteration)
  declareYesNo("#finishedThisJob", "EmploymentHaveYouFinishedThisJob_" + iteration)
  declareDate("#lastWorkDate", "EmploymentWhenDidYouLastWork_" + iteration)
  declareDate("#p45LeavingDate","EmploymentLeavingDateP45_"+iteration)
  declareInput("#hoursPerWeek", "EmploymentHowManyHoursAWeekYouNormallyWork_" + iteration)
}

object GJobDetailsPage {
  val url  = "/employment/job-details"

  def apply(ctx:PageObjectsContext, iteration:Int=1) = new GJobDetailsPage(ctx, iteration)
}

trait GJobDetailsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GJobDetailsPage (PageObjectsContext(browser), iteration = 1)
}
