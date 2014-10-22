package utils.pageobjects


abstract class CircumstancesPage(ctx:PageObjectsContext, url: String, pageTitle: String, iteration: Int = 1)
  extends Page(CircumstancesPageFactory,ctx, url,pageTitle,iteration) {

}
