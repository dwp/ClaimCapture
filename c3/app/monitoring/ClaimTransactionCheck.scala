package monitoring

import com.codahale.metrics.health.HealthCheck
import com.codahale.metrics.health.HealthCheck.Result
import services.ClaimTransactionComponent

class ClaimTransactionCheck extends HealthCheck with ClaimTransactionComponent {
  val claimTransaction = new ClaimTransaction

  override protected def check: HealthCheck.Result = {
    claimTransaction.health() // any exceptions thrown here are converted to Result.unhealthy
    Result.healthy
  }
}
