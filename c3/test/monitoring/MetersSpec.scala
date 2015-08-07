package monitoring

import org.specs2.mutable.Specification
import utils.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.s_eligibility.GBenefitsPage
import com.kenshoo.play.metrics.MetricsRegistry
import utils.WithJsBrowser

class MetersSpec extends Specification {

  "Meters metrics" should {
    "must measure com.kenshoo.play.metrics.MetricsFilter.200" in new WithJsBrowser  with PageObjects {
      val page = GBenefitsPage(context)
      page goToThePage()
      val count = MetricsRegistry.defaultRegistry.meter("com.kenshoo.play.metrics.MetricsFilter.200").getCount
      count mustEqual 3
    }
  } section "unit"
}
