package utils.pageobjects.s_eligibility

import utils.WithBrowser
import utils.pageobjects._

final class G5CarersResponsePage(ctx:PageObjectsContext) extends ClaimPage(ctx, G5CarersResponsePage.url) {
  override def fillPageWith(theClaim: TestData): Page = this

  def isApproved =  ctx.browser.find(".prompt").size != 0 && ctx.browser.find(".prompt.error]").size == 0

  def isNotApproved =  ctx.browser.find(".prompt.error]").size != 0
}

object G5CarersResponsePage {
  val url = "/allowance/carers-response"

  def apply(ctx:PageObjectsContext) = new G5CarersResponsePage(ctx)
}

trait G5CarersResponsePageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = G5CarersResponsePage (PageObjectsContext(browser))
}