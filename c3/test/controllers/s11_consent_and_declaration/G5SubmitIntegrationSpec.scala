package controllers.s11_consent_and_declaration

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.S11_consent_and_declaration.G5SubmitPage

class G5SubmitIntegrationSpec extends Specification with Tags {
  "Response" should {
    "page contains JS enabled check" in new WithBrowser with PageObjects {
      val page = G5SubmitPage(context)
      page goToThePage()
      page jsCheckEnabled() must beTrue
    }
  } section("integration",models.domain.CarersAllowance.id)
}