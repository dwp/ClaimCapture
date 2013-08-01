package utils.pageobjects.s7_employment

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G2JobDetailsPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) extends Page(browser, G2JobDetailsPage.url, G2JobDetailsPage.title, previousPage) {
  override val url = super.getUrl.replace(":jobID",iteration.toString)

  def fillPageWith(theClaim: ClaimScenario) {
    fillInput("#employerName",theClaim.selectDynamic("EmploymentEmployerName_"+iteration))
    fillDate("#jobStartDate", theClaim.selectDynamic("EmploymentWhenDidYouStartYourJob_"+iteration))
    fillRadioList("#finishedThisJob",theClaim.selectDynamic("EmploymentHaveYouFinishedThisJob_"+iteration))
    fillDate("#lastWorkDate",theClaim.selectDynamic("EmploymentWhenDidYouLastWork_"+iteration))
    fillInput("#hoursPerWeek",theClaim.selectDynamic("EmploymentHowManyHoursAWeekYouNormallyWork_"+iteration))
    fillInput("#jobTitle",theClaim.selectDynamic("EmploymentJobTitle_"+iteration))
    fillInput("#payrollEmployeeNumber",theClaim.selectDynamic("EmploymentPayrollOrEmployeeNumber_"+iteration))
  }

}

object G2JobDetailsPage {
  val title = "Job Details - Employment"
  val url  = "/employment/jobDetails/:jobID"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) = new G2JobDetailsPage(browser,previousPage,iteration)
}

trait G2JobDetailsPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G2JobDetailsPage buildPageWith (browser, iteration = 1)
}
