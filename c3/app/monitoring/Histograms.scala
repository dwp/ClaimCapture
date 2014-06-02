package monitoring

import com.kenshoo.play.metrics.MetricsRegistry

object Histograms {
  def recordCacheSize(size: Int) {
    MetricsRegistry.default.histogram("c3-cache-size").update(size)
  }
  def recordClaimSubmissionTime(time: Long) {
    MetricsRegistry.default.histogram("c3-claim-submission-time").update(time)
  }
  def recordChangeOfCircsSubmissionTime(time: Long) {
    MetricsRegistry.default.histogram("c3-coc-submission-time").update(time)
  }
}

object Counters {
  def incrementSubmissionErrorStatus(status:String) {
    MetricsRegistry.default.counter(s"submission-error-status-$status").inc()
  }
  def incrementClaimSubmissionCount() {
    // claims and coc
    MetricsRegistry.default.counter("c3-claim-submission-count").inc()
  }
}
