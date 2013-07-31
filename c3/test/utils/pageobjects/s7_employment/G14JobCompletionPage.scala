package utils.pageobjects.s7_employment

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G14JobCompletionPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends Page(browser, G14JobCompletionPage.url, G14JobCompletionPage.title, previousPage) {
  override val url = super.getUrl.replace(":jobID",iteration.toString)
                                          def fillPageWith(theClaim: ClaimScenario) {
    fillYesNo("#beenEmployed", theClaim.EmploymentBeenEmployed)
  }

  override def updateIterationNumber() = iteration + 1
}

object G14JobCompletionPage {
  val title = "Your employment history - Employment"
  val url  = "/employment/beenEmployed"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G14JobCompletionPage(browser,previousPage,iteration)
}

trait G14JobCompletionPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G14JobCompletionPage buildPageWith(browser,iteration = 1)
}
