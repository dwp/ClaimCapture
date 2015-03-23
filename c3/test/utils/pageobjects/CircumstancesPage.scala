package utils.pageobjects


abstract class CircumstancesPage(ctx:PageObjectsContext, url: String, iteration: Int = 1)
  extends Page(CircumstancesPageFactory,ctx, url,iteration) {

}
