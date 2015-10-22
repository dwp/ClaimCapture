package controllers

import controllers.mappings.Mappings
import models.domain.{AboutOtherMoney, Claim}
import models.view.CachedClaim
import models.yesNo.{YesNoWithEmployerAndMoney, YesNo}
import org.specs2.mutable.{Tags, Specification}
import play.api.mvc.Flash
import play.api.test.FakeRequest
import utils.{WithJsBrowser, WithBrowser}
import play.api.i18n.{MMessages => Messages, Lang}

class ThankYouIntegrationSpec extends Specification with Tags {
  "Thank You" should {
    "present 'Thank You' page" in new WithBrowser with BrowserMatchers {
      browser.goTo("/thankyou/apply-carers")
      urlMustEqual("/thankyou/apply-carers")
    }

    "show employment and self employment messages" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.employment(browser)

      browser.goTo("/thankyou/apply-carers")
      urlMustEqual("/thankyou/apply-carers")

      browser.find("employment").getText mustEqual "Your Employment documents"
      browser.find("selfEmployment").getText mustEqual "Your Self-employed documents"
    }

    "show employment messages" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.justEmployment(browser)

      browser.goTo("/thankyou/apply-carers")
      urlMustEqual("/thankyou/apply-carers")

      browser.find("#employment").getText.nonEmpty must beTrue
      browser.find("#selfEmployment").size() shouldEqual 0
    }

    "show self employment messages" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.justSelfEmployment(browser)

      browser.goTo("/thankyou/apply-carers")
      urlMustEqual("/thankyou/apply-carers")

      browser.find("#employment").size() shouldEqual 0
      browser.find("#selfEmployment").getText.nonEmpty must beTrue
    }

    "don't show employment messages" in new WithBrowser with BrowserMatchers {
      browser.goTo("/thankyou/apply-carers")
      urlMustEqual("/thankyou/apply-carers")

      browser.find("#employment").size() shouldEqual 0
      browser.find("#selfEmployment").size() shouldEqual 0

    }

    "request statutory pay evidence if they have statutory pay" in new WithJsBrowser {

      val otherPayments = AboutOtherMoney(YesNo(""), None, None, None,
        YesNoWithEmployerAndMoney(Mappings.yes, None, None, None, None, None),
        YesNoWithEmployerAndMoney(Mappings.yes, None, None, None, None, None))

      val lang = Lang("en")
      implicit val claim = Claim(CachedClaim.key).update(otherPayments)
      implicit val request = FakeRequest()
      implicit val flash = Flash()

      val thankYouPage = views.html.common.thankYouClaim(lang).body

      thankYouPage must contain(Messages("evidence.otherMoney.statutorySickPay"))
      thankYouPage must contain(Messages("evidence.otherMoney.otherStatutoryPay"))
    }

    "not request statutory pay evidence if they don't have statutory pay" in new WithJsBrowser {
      val otherPayments = AboutOtherMoney(YesNo(""), None, None, None,
        YesNoWithEmployerAndMoney(Mappings.no, None, None, None, None, None),
        YesNoWithEmployerAndMoney(Mappings.no, None, None, None, None, None))

      val lang = Lang("en")
      implicit val claim = Claim(CachedClaim.key).update(otherPayments)
      implicit val request = FakeRequest()
      implicit val flash = Flash()

      val thankYouPage = views.html.common.thankYouClaim(lang).body

      thankYouPage must not contain Messages("evidence.otherMoney.statutorySickPay")
      thankYouPage must not contain Messages("evidence.otherMoney.otherStatutoryPay")
    }

  } section "integration"
}