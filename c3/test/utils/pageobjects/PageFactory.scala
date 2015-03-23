package utils.pageobjects

/**
 * Base class of all the PageFactories, used by utils.pageobjects.Page to determine thanks to a page title which Page Object to build.
 */
trait PageFactory {
  def buildPageFromUrl(url: String,ctx:PageObjectsContext):Page
}
