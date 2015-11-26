package monitoring

import com.codahale.metrics.SharedMetricRegistries
import play.api.Play._

object Histograms {
  def recordCacheSize(size: Int) {
    SharedMetricRegistries.getOrCreate(current.configuration.getString("metrics.name").getOrElse("default")).histogram("c3-cache-size").update(size)
  }
  def recordClaimSubmissionTime(time: Long) {
    SharedMetricRegistries.getOrCreate(current.configuration.getString("metrics.name").getOrElse("default")).histogram("c3-claim-submission-time").update(time)
  }
  def recordChangeOfCircsSubmissionTime(time: Long) {
    SharedMetricRegistries.getOrCreate(current.configuration.getString("metrics.name").getOrElse("default")).histogram("c3-coc-submission-time").update(time)
  }
}

object Counters {
  def incrementSubmissionErrorStatus(status:String) {
    SharedMetricRegistries.getOrCreate(current.configuration.getString("metrics.name").getOrElse("default")).counter(s"submission-error-status-$status").inc()
  }
  def incrementClaimSubmissionCount() {
    // claims and coc
    SharedMetricRegistries.getOrCreate(current.configuration.getString("metrics.name").getOrElse("default")).counter("submission-successful-count").inc()
  }
}
