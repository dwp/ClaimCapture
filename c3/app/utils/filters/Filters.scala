package utils.filters

import javax.inject.Inject

import com.kenshoo.play.metrics.MetricsFilter
import play.api.http.HttpFilters
import utils.csrf.DwpCSRFFilter

class Filters @Inject() (metricsFilter: MetricsFilter) extends HttpFilters {
  val dwpCSRFFilter = new DwpCSRFFilter(createIfFound = CSRFCreation.createIfFound, createIfNotFound = CSRFCreation.createIfNotFound)
  val userAgentCheckFilter = new UserAgentCheckFilter()
  val filters = Seq(metricsFilter, userAgentCheckFilter, dwpCSRFFilter)
}
