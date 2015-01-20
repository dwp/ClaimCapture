package monitoring

import com.kenshoo.play.metrics.MetricsRegistry

object Histograms {
  def recordCacheSize(size: Int) {
    MetricsRegistry.defaultRegistry.histogram("c3-cache-size").update(size)
  }
  def recordClaimSubmissionTime(time: Long) {
    MetricsRegistry.defaultRegistry.histogram("c3-claim-submission-time").update(time)
  }
  def recordChangeOfCircsSubmissionTime(time: Long) {
    MetricsRegistry.defaultRegistry.histogram("c3-coc-submission-time").update(time)
  }
}

object Counters {
  def incrementSubmissionErrorStatus(status:String) {
    MetricsRegistry.defaultRegistry.counter(s"submission-error-status-$status").inc()
  }
  def incrementClaimSubmissionCount() {
    // claims and coc
    MetricsRegistry.defaultRegistry.counter("submission-successful-count").inc()
  }
}
