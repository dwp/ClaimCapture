package monitoring

import com.kenshoo.play.metrics.MetricsRegistry

object Histograms {
  def recordCacheSize(size: Int) {
    MetricsRegistry.default.histogram("play-cache-size").update(size)
  }
}
