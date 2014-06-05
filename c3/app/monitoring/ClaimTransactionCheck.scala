package monitoring

import com.codahale.metrics.health.HealthCheck
import com.codahale.metrics.health.HealthCheck.Result
import services.ClaimTransactionComponent

class ClaimTransactionCheck extends HealthCheck with ClaimTransactionComponent {
  val claimTransaction = new ClaimTransaction

  override protected def check: HealthCheck.Result = {
    try {
      claimTransaction.health()
      Result.healthy
    }
    catch {
      case e: Exception =>
        Result.unhealthy(e)
    }
  }
}
