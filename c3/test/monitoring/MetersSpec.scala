package monitoring

import com.codahale.metrics.SharedMetricRegistries
import org.specs2.mutable._
import utils.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.s_eligibility.GBenefitsPage
import utils.WithJsBrowser

class MetersSpec extends Specification {

  "Meters metrics" should {
    "must measure com.kenshoo.play.metrics.MetricsFilter.200" in new WithJsBrowser  with PageObjects {
      val page = GBenefitsPage(context)
      page goToThePage()
      val count = SharedMetricRegistries.getOrCreate("c3.metrics").meter("com.kenshoo.play.metrics.MetricsFilter.200").getCount
      count mustEqual 3
    }
  }
  section("unit")
}
