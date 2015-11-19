package controllers.preview

import org.specs2.mutable._
import utils.WithJsBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.preview.PreviewPage

/**
 * Created by valtechuk on 16/10/2015.
 */
class PreviewSpec extends Specification {

  "Preview page" should {
    "be presented with back button removed" in new WithJsBrowser with PageObjects{
      val page =  PreviewPage(context)
      page goToThePage()
      val source = page.source
      print( source )

      source must not contain("backButton")
    }
  }
}
