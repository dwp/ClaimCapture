package utils.pageobjects.s7_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{PageContext, Page}

final class G7PensionSchemesPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends Page(browser, G7PensionSchemesPage.url.replace(":jobID", iteration.toString), G7PensionSchemesPage.title, previousPage, iteration) {
  declareYesNo("#payOccupationalPensionScheme", "EmploymentDoYouPayTowardsanOccupationalPensionScheme_" + iteration)
  declareInput("#howMuchPension", "EmploymentHowMuchYouPayforOccupationalPension_" + iteration)
  declareSelect("#howOftenPension", "EmploymentHowOftenOccupationalPension_" + iteration)
  declareYesNo("#payPersonalPensionScheme", "EmploymentDoYouPayTowardsAPersonalPension_" + iteration)
  declareInput("#howMuchPersonal", "EmploymentHowMuchYouPayforPersonalPension_" + iteration)
  declareSelect("#howOftenPersonal", "EmploymentHowOftenPersonalPension_" + iteration)
}

object G7PensionSchemesPage {
  val title = "Pension schemes - Employment"

  val url  = "/employment/pension-schemes/:jobID"

  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G7PensionSchemesPage(browser,previousPage,iteration)
}

trait G7PensionSchemesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G7PensionSchemesPage buildPageWith(browser,iteration = 1)
}