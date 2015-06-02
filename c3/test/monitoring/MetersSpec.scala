package monitoring

import org.specs2.mutable.Specification
import utils.WithBrowser
import utils.pageobjects.PageObjects
import utils.pageobjects.s0_carers_allowance.G1BenefitsPage
import com.kenshoo.play.metrics.MetricsRegistry
import utils.WithJsBrowser

class MetersSpec extends Specification {

  "Meters metrics" should {
    "must measure com.kenshoo.play.metrics.MetricsFilter.200" in new WithJsBrowser  with PageObjects {
      val page = G1BenefitsPage(context)
      page goToThePage()
      val count = MetricsRegistry.defaultRegistry.meter("com.kenshoo.play.metrics.MetricsFilter.200").getCount
      count mustEqual 3
    }
  } section "unit"
}
