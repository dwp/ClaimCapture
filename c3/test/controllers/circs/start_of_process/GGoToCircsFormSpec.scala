package controllers.circs.start_of_process

import app.ReportChange
import models.domain._
import models.view.CachedChangeOfCircs
import org.specs2.mutable._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.WithApplication

class GGoToCircsFormSpec extends Specification {
  section("unit", models.domain.CircumstancesReportChanges.id)
  "GGoToCircsFunction " should {
    def g2FakeRequest(claimKey: String) = {
      FakeRequest().withSession(CachedChangeOfCircs.key -> claimKey).withFormUrlEncodedBody(
      )
    }

    "redirect to the next page after a valid additional info submission" in new WithApplication with MockForm {
      val claim = Claim(claimKey)
      cache.set("default" + claimKey, claim.update(ReportChangeReason(false, ReportChange.AdditionalInfo.name)))
      val result = GGoToCircsFunction.present(g2FakeRequest(claimKey))
      redirectLocation(result) must beSome("/circumstances/report-changes/other-change")
    }

    "redirect to the next page after a valid self-employment submission" in new WithApplication with MockForm {
      val claim = Claim(claimKey)
      cache.set("default" + claimKey, claim.update(ReportChangeReason(false, ReportChange.SelfEmployment.name)))
      val result = GGoToCircsFunction.present(g2FakeRequest(claimKey))
      redirectLocation(result) must beSome("/circumstances/report-changes/self-employment")
    }

    "redirect to the next page after a valid stopped caring submission" in new WithApplication with MockForm {
      val claim = Claim(claimKey)
      cache.set("default" + claimKey, claim.update(ReportChangeReason(false, ReportChange.StoppedCaring.name)))
      val result = GGoToCircsFunction.present(g2FakeRequest(claimKey))
      redirectLocation(result) must beSome("/circumstances/report-changes/stopped-caring")
    }

    "redirect to the next page after a valid address change submission" in new WithApplication with MockForm {
      val claim = Claim(claimKey)
      cache.set("default" + claimKey, claim.update(ReportChangeReason(false, ReportChange.AddressChange.name)))
      val result = GGoToCircsFunction.present(g2FakeRequest(claimKey))
      redirectLocation(result) must beSome("/circumstances/report-changes/address-change")
    }

    "redirect to the next page after a valid payment change submission" in new WithApplication with MockForm {
      val claim = Claim(claimKey)
      cache.set("default" + claimKey, claim.update(ReportChangeReason(false, ReportChange.PaymentChange.name)))
      val result = GGoToCircsFunction.present(g2FakeRequest(claimKey))
      redirectLocation(result) must beSome("/circumstances/report-changes/payment-change")
    }

    "redirect to the next page after a valid break from caring submission" in new WithApplication with MockForm {
      val claim = Claim(claimKey)
      cache.set("default" + claimKey, claim.update(ReportChangeReason(false, ReportChange.BreakFromCaring.name)))
      val result = GGoToCircsFunction.present(g2FakeRequest(claimKey))
      redirectLocation(result) must beSome("/circumstances/report-changes/breaks-in-care")
    }

    "redirect to the next page after a valid break from caring submission because of you" in new WithApplication with MockForm {
      val claim = Claim(claimKey)
      cache.set("default" + claimKey, claim.update(ReportChangeReason(false, ReportChange.BreakFromCaringYou.name)))
      val result = GGoToCircsFunction.present(g2FakeRequest(claimKey))
      redirectLocation(result) must beSome("/circumstances/report-changes/breaks-in-care")
    }
  }
  section("unit", models.domain.CircumstancesReportChanges.id)
}
