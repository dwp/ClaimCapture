package utils.pageobjects


/**
 * Base class of all the PageFactories, used by utils.pageobjects.Page to determine thanks to a page title which Page Object to build.
 * @author Jorge Migueis
 *         Date: 21/08/2013
 */
trait PageFactory {
  def buildPageFromTitle(title: String,ctx:PageObjectsContext):Page

}
