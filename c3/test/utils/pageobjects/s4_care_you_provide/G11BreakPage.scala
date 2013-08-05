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
 
    declareDate("#start", "AboutTheCareYouProvideBreakStartDate_" + iteration)
    declareDate("#end", "AboutTheCareYouProvideBreakEndDate_" + iteration)
    declareTime("#start", "AboutTheCareYouProvideBreakStartTime_" + iteration)
    declareTime("#end", "AboutTheCareYouProvideBreakEndTime_" + iteration)
    declareWhereabouts("#whereYou_location", "AboutTheCareYouProvideWhereWereYouDuringTheBreak_" + iteration)
    declareWhereabouts("#wherePerson_location", "AboutTheCareYouProvideWhereWasThePersonYouCareForDuringtheBreak_" + iteration)
    declareYesNo("#medicalDuringBreak", "AboutTheCareYouProvideDidYouOrthePersonYouCareForGetAnyMedicalTreatment_" + iteration)

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