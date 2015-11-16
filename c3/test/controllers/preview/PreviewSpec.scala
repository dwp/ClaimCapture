package controllers.preview

import org.specs2.mutable.{Tags, Specification}
import utils.WithJsBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.preview.PreviewPage

class PreviewSpec extends Specification with Tags{

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
