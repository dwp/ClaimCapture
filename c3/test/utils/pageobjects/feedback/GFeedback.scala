package utils.pageobjects.feedback

import utils.WithBrowser
import utils.pageobjects._

final class GFeedbackPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GFeedbackPage.url) {
  declareRadioList("#satisfiedAnswer", "FeedbackSatisfied")
}

object GFeedbackPage {
  val url = "/feedback/feedback"

  def apply(ctx:PageObjectsContext) = new GFeedbackPage(ctx)
}

/** The context for Specs tests */
trait GFeedbackPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GFeedbackPage (PageObjectsContext(browser))
}
