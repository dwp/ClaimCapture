package utils.pageobjects.common

import play.api.test.WithBrowser
import utils.pageobjects._


object ErrorPage {
  val url = "/error"

  def apply(ctx: PageObjectsContext) = new ErrorPage(ctx)
}

final class ErrorPage(ctx: PageObjectsContext) extends ClaimPage(ctx, ErrorPage.url){

  /**
   * Throws a PageObjectException.
   * @param theClaim   Data to use to fill page
   */
  override def fillPageWith(theClaim: TestData): Page =
    throw new PageObjectException(s"Reached Error page [$url], the previous page was [${ctx.previousPage.getOrElse(this).url}]. This page cannot be filled. Check test.")

  /**
   * Throws a PageObjectException.
   * @param theClaim   Claim to populate.
   */
  override def populateClaim(theClaim: TestData): Page =
    throw new PageObjectException(s"Reached Error page [$url], the previous page was [${ctx.previousPage.getOrElse(this).url}]. This page cannot populate a claim. Check test.")
}


trait ErrorPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = ErrorPage (PageObjectsContext(browser))
}
