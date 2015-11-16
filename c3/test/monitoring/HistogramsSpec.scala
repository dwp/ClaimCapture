package monitoring

import com.codahale.metrics.SharedMetricRegistries
import org.specs2.mutable._
import utils.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.s_eligibility.GBenefitsPage

class HistogramsSpec extends Specification {
  "Histogram metrics" should {
    "must measure play-cache-size" in new WithBrowser with PageObjects {
      val page = GBenefitsPage(context)
      page goToThePage()
      val count = SharedMetricRegistries.getOrCreate("c3.metrics").histogram("c3-cache-size").getCount
      count mustEqual 1
    }
  }
section("unit")
}
