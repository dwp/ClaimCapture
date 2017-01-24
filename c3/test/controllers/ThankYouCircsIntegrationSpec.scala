package controllers

import app.ReportChange
import controllers.mappings.Mappings
import models.DayMonthYear
import models.domain._
import models.view.CachedChangeOfCircs
import models.yesNo.{YesNoDontKnowWithDates, YesNoWithDate}
import utils.{WithApplication, WithBrowser}
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._

class ThankYouCircsIntegrationSpec extends Specification {
  section("integration")
  "Change Thank You" should {
    val thankyouPageBreakText = "You must tell us as soon as you start providing care again, or if a decision is made that you have permanently stopped providing care, as your entitlement may be affected."
    def g2FakeRequest(claimKey: String) = {
      FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey).withFormUrlEncodedBody()
    }

    "present 'Thank You' page" in new WithBrowser with BrowserMatchers {
      browser.goTo("/thankyou/change-carers")
      urlMustEqual("/thankyou/change-carers")
    }

    "should NOT display breaks in care message when Other break and Caring has restarted" in new WithApplication with MockForm {
      val dontknowBreak = CircsBreak(iterationID = "1", typeOfCare = Breaks.another,
        caringEnded = Some(DayMonthYear(1, 2, 2003)), caringStarted = Some(YesNoWithDate(Mappings.yes, None)))
      val breaksInCare = CircsBreaksInCare().update(dontknowBreak)
      val claim = Claim(CachedChangeOfCircs.key).update(breaksInCare)
      cache.set("default" + CachedChangeOfCircs.key, claim.update(ReportChangeReason(false, ReportChange.BreakFromCaring.name)))

      val result = CircsEnding.thankyou(g2FakeRequest(CachedChangeOfCircs.key))
      val bodyText: String = contentAsString(result)
      bodyText must not contain (thankyouPageBreakText)
    }

    "should NOT display breaks in care message when Other break Not Restarted Caring but Yes will restart" in new WithApplication with MockForm {
      val dontknowBreak = CircsBreak(iterationID = "1", typeOfCare = Breaks.another,
        caringEnded = Some(DayMonthYear(1, 2, 2003)), caringStarted = Some(YesNoWithDate(Mappings.no, None)),
        expectToCareAgain = Some(YesNoDontKnowWithDates(Some(Mappings.yes), None, None)))
      val breaksInCare = CircsBreaksInCare().update(dontknowBreak)
      val claim = Claim(CachedChangeOfCircs.key).update(breaksInCare)
      cache.set("default" + CachedChangeOfCircs.key, claim.update(ReportChangeReason(false, ReportChange.BreakFromCaring.name)))

      val result = CircsEnding.thankyou(g2FakeRequest(CachedChangeOfCircs.key))
      val bodyText: String = contentAsString(result)
      bodyText must not contain (thankyouPageBreakText)
    }

    "should NOT display breaks in care message when Other break Not Restarted Caring and No wont restart" in new WithApplication with MockForm {
      val dontknowBreak = CircsBreak(iterationID = "1", typeOfCare = Breaks.another,
        caringEnded = Some(DayMonthYear(1, 2, 2003)), caringStarted = Some(YesNoWithDate(Mappings.no, None)),
        expectToCareAgain = Some(YesNoDontKnowWithDates(Some(Mappings.no), None, None)))
      val breaksInCare = CircsBreaksInCare().update(dontknowBreak)
      val claim = Claim(CachedChangeOfCircs.key).update(breaksInCare)
      cache.set("default" + CachedChangeOfCircs.key, claim.update(ReportChangeReason(false, ReportChange.BreakFromCaring.name)))

      val result = CircsEnding.thankyou(g2FakeRequest(CachedChangeOfCircs.key))
      val bodyText: String = contentAsString(result)
      bodyText must not contain(thankyouPageBreakText)
    }

    "should display breaks in care message when Other break Not Restarted Caring and Dont Know if will" in new WithApplication with MockForm {
      val dontknowBreak = CircsBreak(iterationID = "1", typeOfCare = Breaks.another,
        caringEnded = Some(DayMonthYear(1, 2, 2003)), caringStarted = Some(YesNoWithDate(Mappings.no, None)),
        expectToCareAgain = Some(YesNoDontKnowWithDates(Some(Mappings.dontknow), None, None)))
      val breaksInCare = CircsBreaksInCare().update(dontknowBreak)
      val claim = Claim(CachedChangeOfCircs.key).update(breaksInCare)
      cache.set("default" + CachedChangeOfCircs.key, claim.update(ReportChangeReason(false, ReportChange.BreakFromCaring.name)))

      val result = CircsEnding.thankyou(g2FakeRequest(CachedChangeOfCircs.key))
      val bodyText: String = contentAsString(result)
      bodyText must contain(thankyouPageBreakText)
    }
  }
  section("integration")
}
