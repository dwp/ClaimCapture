package monitoring

import com.kenshoo.play.metrics.MetricsRegistry

object Histograms {
   def cacheSize = MetricsRegistry.default.histogram("play-cache-size")
}
