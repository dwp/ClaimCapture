package utils.pageobjects.s7_employment

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects.{IterationManager, ClaimPage, Page, PageContext}

final class G14JobCompletionPage(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) extends ClaimPage(browser, G14JobCompletionPage.url.replace(":jobID", iteration.toString), G14JobCompletionPage.title, previousPage, iteration) {
  override def getNewIterationNumber: Int = IterationManager.increase("Employment")
}

object G14JobCompletionPage {
  val title = "Job Completion - Employment History".toLowerCase

  val url  = "/employment/job-completion/:jobID"

  def apply(browser: TestBrowser, previousPage: Option[Page] = None, iteration: Int) = new G14JobCompletionPage(browser,previousPage,iteration)
}

trait G14JobCompletionPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G14JobCompletionPage (browser,iteration = 1)
}