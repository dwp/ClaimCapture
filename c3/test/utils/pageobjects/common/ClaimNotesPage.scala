package utils.pageobjects.common

import play.api.test.WithBrowser
import utils.pageobjects._

object ClaimNotesPage {
  val url = "/internal-claim-notes"

  def apply(ctx: PageObjectsContext) = new ClaimNotesPage(ctx)
}

final class ClaimNotesPage(ctx: PageObjectsContext) extends ClaimPage(ctx, ClaimNotesPage.url){

  /**
   * Throws a PageObjectException.
   * @param theClaim   Data to use to fill page
   */
  override def fillPageWith(theClaim: TestData): Page =
    throw new PageObjectException(s"Reached calimNote page [$url], the previous page was [${ctx.previousPage.getOrElse(this).url}]. This page cannot be filled. Check test.")

  /**
   * Throws a PageObjectException.
   * @param theClaim   Claim to populate.
   */
  override def populateClaim(theClaim: TestData): Page =
    throw new PageObjectException(s"Reached ClaimNotes page [$url], the previous page was [${ctx.previousPage.getOrElse(this).url}]. This page cannot populate a claim. Check test.")
}


trait ClaimNotesPageContext extends PageContext {
  this: WithBrowser[_] =>

  val page = ClaimNotesPage (PageObjectsContext(browser))
}

