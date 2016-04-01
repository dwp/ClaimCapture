package utils.pageobjects.your_income

import utils.WithBrowser
import utils.pageobjects.{PageContext, ClaimPage, PageObjectsContext}

/**
  * Created by peterwhitehead on 29/03/2016.
  */
class GOtherPaymentsPage(ctx:PageObjectsContext) extends ClaimPage(ctx, GOtherPaymentsPage.url) {
  declareInput("#otherPaymentsInfo", "OtherPaymentsInfo")
}

object GOtherPaymentsPage {
  val url  = "/your-income/other-payments"

  def apply(ctx:PageObjectsContext) = new GOtherPaymentsPage(ctx)
}

trait GOtherPaymentsPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = GOtherPaymentsPage (PageObjectsContext(browser))
}

