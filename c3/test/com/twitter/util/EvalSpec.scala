package com.twitter.util

import org.specs2.mutable.Specification
import play.api.test.WithApplication
import play.api.templates.Html

class EvalSpec extends Specification {
  "Eval" should {
    "add 3 to 4 giving 7" in {
      val eval = new Eval()
      eval[Int]("3 + 4") shouldEqual 7
    }
  }

  "Eval in Play" should {
    "blah" in new WithApplication {
      val contentHtml: Html = Html("<div>Hi</div>")
      val footerHtml: Html = Html("<footer></footer>")

      val resultHtml: Html = views.html.common.main_test("Boo")(contentHtml)(footerHtml)

      val myCall = views.html.common.main_test.apply _
      val myResult = myCall("Ye")(contentHtml)(footerHtml)
    }

    "blah 2" in new WithApplication {
      val eval = new Eval()

      val contentHtml: Html = Html("<div>Hi</div>")
      val footerHtml: Html = Html("<footer></footer>")

      val x = eval[(String) => (Html) => (Html) => (Html)]("views.html.common.main_test.apply _")
      val y = x("Goodie")(contentHtml)(footerHtml)
    }

    "blah 3" in new WithApplication {
      import play.api.Play.current

      val template = current.configuration.getString("main.template").getOrElse("views.html.common.main_test")
      println(template)

      val eval = new Eval()

      val contentHtml: Html = Html("<div>Hi</div>")
      val footerHtml: Html = Html("<footer></footer>")

      val x = eval[(String) => (Html) => (Html) => (Html)](s"$template.apply _")
      val y = x("Goodie again")(contentHtml)(footerHtml)
      println(y)
    }
  }
}