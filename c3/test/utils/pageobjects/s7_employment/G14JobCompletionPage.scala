package utils.pageobjects.s7_employment

import play.api.test.TestBrowser
import utils.pageobjects.{ClaimScenario, PageContext, Page}

final class G14JobCompletionPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends Page(browser, G14JobCompletionPage.url.replace(":jobID",iteration.toString), G14JobCompletionPage.title, previousPage,iteration) {

  override def updateIterationNumber: Int = iteration + 1
}

object G14JobCompletionPage {
  val title = "Job Completion - Employment"
  val url  = "/employment/jobCompletion/:jobID"
  def buildPageWith(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G14JobCompletionPage(browser,previousPage,iteration)
}

trait G14JobCompletionPageContext extends PageContext {
  this: {val browser:TestBrowser}  =>
  val page = G14JobCompletionPage buildPageWith(browser,iteration = 1)
}
