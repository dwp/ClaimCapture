package utils.pageobjects.common

import play.api.test.{WithBrowser, TestBrowser}
import utils.pageobjects._


final class ErrorPage(ctx: PageObjectsContext) extends ClaimPage(ctx, ErrorPage.url, ErrorPage.title){

  /**
   * Throws a PageObjectException.
   * @param theClaim   Data to use to fill page
   */
  override def fillPageWith(theClaim: TestData): Page =
    throw new PageObjectException(s"Reached Error page [$pageTitle], the previous page was [${ctx.previousPage.getOrElse(this).pageTitle}]. This page cannot be filled. Check test.")

  /**
   * Throws a PageObjectException.
   * @param theClaim   Claim to populate.
   */
  override def populateClaim(theClaim: TestData): Page =
    throw new PageObjectException(s"Reached Error page [$pageTitle], the previous page was [${ctx.previousPage.getOrElse(this).pageTitle}]. This page cannot populate a claim. Check test.")
}


object ErrorPage {
  val title = "Sorry, there has been a problem.".toLowerCase

  val url = "/error"

  def apply(ctx: PageObjectsContext) = new ErrorPage(ctx)
}

trait ErrorPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = ErrorPage (PageObjectsContext(browser))
}
