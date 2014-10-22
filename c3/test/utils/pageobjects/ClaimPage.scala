package utils.pageobjects


/**
 * TODO write description
 * @author Jorge Migueis
 *         Date: 21/08/2013
 */
abstract class ClaimPage(ctx:PageObjectsContext, url: String, pageTitle: String,iteration:Int = 1)
  extends Page(ClaimPageFactory,ctx, url,pageTitle,iteration) {

}
