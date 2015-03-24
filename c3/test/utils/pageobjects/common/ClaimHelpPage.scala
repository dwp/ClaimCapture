package utils.pageobjects.common

import utils.pageobjects._


object ClaimHelpPage {
  val url = "/claim-help"

  def apply(ctx: PageObjectsContext) = new ClaimHelpPage(ctx)
}

final class ClaimHelpPage(ctx: PageObjectsContext) extends ClaimPage(ctx, ClaimHelpPage.url){

  /**
   * Throws a PageObjectException.
   * @param theClaim   Data to use to fill page
   */
  override def fillPageWith(theClaim: TestData): Page =
    throw new PageObjectException(s"Reached Claim Help page [$url], the previous page was [${ctx.previousPage.getOrElse(this).url}]. This page cannot be filled. Check test.")

  /**
   * Throws a PageObjectException.
   * @param theClaim   Claim to populate.
   */
  override def populateClaim(theClaim: TestData): Page =
    throw new PageObjectException(s"Reached Claim Help page [$url], the previous page was [${ctx.previousPage.getOrElse(this).url}]. This page cannot populate a claim. Check test.")
}