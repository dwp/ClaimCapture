package monitoring

import com.kenshoo.play.metrics.MetricsRegistry

object Histograms {
  def recordCacheSize(size: Int) {
    MetricsRegistry.default.histogram("c3-cache-size").update(size)
  }
}
