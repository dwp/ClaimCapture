package utils.pageobjects.s7_employment

import play.api.test.WithBrowser
import utils.pageobjects._

final class G14JobCompletionPage(ctx:PageObjectsContext, iteration: Int) extends ClaimPage(ctx, G14JobCompletionPage.url.replace(":jobID", iteration.toString), G14JobCompletionPage.title, iteration) {
  override def getNewIterationNumber: Int = {
    import IterationManager._
    println("INCREASING EMPLOYMENT ITERATION")
    ctx.iterationManager.increment(Employment)
  }
}

object G14JobCompletionPage {
  val title = "Job Completion - Employment History".toLowerCase

  val url  = "/employment/job-completion/:jobID"

  def apply(ctx:PageObjectsContext, iteration: Int= 1) = new G14JobCompletionPage(ctx,iteration)
}

trait G14JobCompletionPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G14JobCompletionPage (PageObjectsContext(browser),iteration = 1)
}