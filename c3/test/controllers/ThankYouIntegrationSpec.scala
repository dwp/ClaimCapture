package controllers

import controllers.mappings.Mappings
import models.domain.{AboutOtherMoney, Claim}
import models.view.CachedClaim
import models.yesNo.{YesNoWithEmployerAndMoney, YesNo}
import org.specs2.mutable._
import play.api.mvc.Flash
import play.api.test.FakeRequest
import utils.{WithJsBrowser, WithBrowser}
import play.api.i18n._
import play.api.Play.current

class ThankYouIntegrationSpec extends Specification {
  def escapeMessage(id:String,param:String="") = {
    val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
    messagesApi(id,param)
  }

  section("integration")
  "Thank You" should {
    "present 'Thank You' page" in new WithBrowser with BrowserMatchers {
      browser.goTo("/thankyou/apply-carers")
      urlMustEqual("/thankyou/apply-carers")
    }

    "show employment and self-employment messages" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.employment(browser)

      browser.goTo("/thankyou/apply-carers")
      urlMustEqual("/thankyou/apply-carers")

      browser.find("#employment").getText mustEqual "any payslips you have had since your claim date"
      browser.find("#selfEmployment").getText mustEqual "your most recent finalised accounts you have for your business"
      browser.pageSource() must contain(escapeMessage("evidence.include.documents"))
    }

    "show employment messages" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.justEmployment(browser)

      browser.goTo("/thankyou/apply-carers")
      urlMustEqual("/thankyou/apply-carers")

      browser.find("#employment").getText.nonEmpty must beTrue
      browser.find("#selfEmployment").size() shouldEqual 0
      browser.pageSource() must contain(escapeMessage("evidence.include.documents"))
    }

    "show self-employment messages" in new WithBrowser with BrowserMatchers {
      Formulate.claimDate(browser)
      Formulate.justSelfEmployment(browser)

      browser.goTo("/thankyou/apply-carers")
      urlMustEqual("/thankyou/apply-carers")

      browser.find("#employment").size() shouldEqual 0
      browser.find("#selfEmployment").getText.nonEmpty must beTrue
      browser.pageSource() must contain(escapeMessage("evidence.include.documents"))
    }

    "don't show employment messages" in new WithBrowser with BrowserMatchers {
      browser.goTo("/thankyou/apply-carers")
      urlMustEqual("/thankyou/apply-carers")

      browser.find("#employment").size() shouldEqual 0
      browser.find("#selfEmployment").size() shouldEqual 0
      browser.pageSource() must not contain(escapeMessage("evidence.include.documents"))

    }

    "request statutory pay evidence if they have statutory pay" in new WithJsBrowser {
      implicit val lang = Lang("en")
      val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
      implicit val messages = Messages(lang, messagesApi)
      val otherPayments = AboutOtherMoney(YesNo(""), None, None, None,
        YesNoWithEmployerAndMoney(Mappings.yes, None, None, None, None, None),
        YesNoWithEmployerAndMoney(Mappings.yes, None, None, None, None, None))

      implicit val claim = Claim(CachedClaim.key).update(otherPayments)
      implicit val request = FakeRequest()
      implicit val flash = Flash()

      val thankYouPage = views.html.common.thankYouClaim(lang).body

      thankYouPage must contain(messagesApi("evidence.otherMoney.statutorySickPay"))
      thankYouPage must contain(messagesApi("evidence.otherMoney.otherStatutoryPay"))
      thankYouPage must contain(messagesApi("evidence.include.documents"))
    }

    "not request statutory pay evidence if they don't have statutory pay" in new WithJsBrowser {
      implicit val lang = Lang("en")
      val messagesApi: MessagesApi = current.injector.instanceOf[MMessages]
      implicit val messages = Messages(lang, messagesApi)
      val otherPayments = AboutOtherMoney(YesNo(""), None, None, None,
        YesNoWithEmployerAndMoney(Mappings.no, None, None, None, None, None),
        YesNoWithEmployerAndMoney(Mappings.no, None, None, None, None, None))

      implicit val claim = Claim(CachedClaim.key).update(otherPayments)
      implicit val request = FakeRequest()
      implicit val flash = Flash()

      val thankYouPage = views.html.common.thankYouClaim(lang).body

      thankYouPage must not contain messagesApi("evidence.otherMoney.statutorySickPay")
      thankYouPage must not contain messagesApi("evidence.otherMoney.otherStatutoryPay")
      browser.pageSource() must not contain(messagesApi("evidence.include.documents"))
    }
  }
  section("integration")
}
