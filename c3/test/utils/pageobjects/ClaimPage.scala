package utils.pageobjects

abstract class ClaimPage(ctx:PageObjectsContext, url: String, iteration:Int = 1)
  extends Page(ClaimPageFactory,ctx, url, iteration) {

}
