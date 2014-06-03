package monitoring

import com.kenshoo.play.metrics.{MetricsRegistry, MetricsFilter}
import com.codahale.metrics.MetricRegistry

abstract class MonitorFilter extends MetricsFilter

object MonitorFilter extends MonitorFilter {
  override def registry: MetricRegistry =  MetricsRegistry.default
}
