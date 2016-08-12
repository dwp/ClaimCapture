package monitoring

import gov.dwp.carers.CADSHealthCheck
import gov.dwp.carers.CADSHealthCheck.Result
import models.view.ClaimHandling
import services.ClaimTransactionComponent

class ClaimTransactionCheck extends CADSHealthCheck(ClaimHandling.C3NAME, ClaimHandling.C3VERSION_VALUE, "-db-check") with ClaimTransactionComponent {
  val claimTransaction = new ClaimTransaction

  override protected def check: CADSHealthCheck.Result = {
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
