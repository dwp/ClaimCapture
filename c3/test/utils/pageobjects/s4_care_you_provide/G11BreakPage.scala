package utils.pageobjects.s4_care_you_provide

import play.api.test.TestBrowser
import utils.pageobjects.{PageContext, ClaimScenario, Page}

/**
 * Created with IntelliJ IDEA.
 * User: jmi
 * Date: 30/07/2013
 * Time: 10:32
 * To change this template use File | Settings | File Templates.
 */
final class G11BreakPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends Page(browser, G11BreakPage.url, G11BreakPage.title, previousPage, iteration) {
  /**
   * Sub-class reads theClaim and interacts with browser to populate page.
   * @param theClaim   Data to use to fill page
   */
  def fillPageWith(theClaim: ClaimScenario) {
    fillDate("#start", theClaim.selectDynamic("AboutTheCareYouProvideBreakStartDate_" + iteration))
    fillDate("#end", theClaim.selectDynamic("AboutTheCareYouProvideBreakEndDate_" + iteration))
    fillTime("#start", theClaim.selectDynamic("AboutTheCareYouProvideBreakStartTime_" + iteration))
    fillTime("#end", theClaim.selectDynamic("AboutTheCareYouProvideBreakEndTime_" + iteration))
    fillWhereabouts("#whereYou_location", theClaim.selectDynamic("AboutTheCareYouProvideWhereWereYouDuringTheBreak_" + iteration))
    fillWhereabouts("#wherePerson_location", theClaim.selectDynamic("AboutTheCareYouProvideWhereWasThePersonYouCareForDuringtheBreak_" + iteration))
    fillYesNo("#medicalDuringBreak", theClaim.selectDynamic("AboutTheCareYouProvideDidYouOrthePersonYouCareForGetAnyMedicalTreatment_" + iteration))
  }

  /**
   * Called by submitPage of Page. A new G10 will be built with an incremented iteration number.
   * @return Incremented iteration number.
   */
  protected override def updateIterationNumber = iteration+1
}

/**
 * Companion object that integrates factory method.
 * It is used by PageFactory object defined in PageFactory.scala
 */
object G11BreakPage {
  val title = "Break - Care You Provide"
  val url  = "/careYouProvide/break"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration:Int) = new G11BreakPage(browser,previousPage,iteration)
}

/** The context for Specs tests */
trait G11BreakPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G11BreakPage buildPageWith (browser = browser, iteration = 1)
}