package controllers.submission

import models.view.CachedClaim
import services.ClaimTransactionComponent
import monitoring.ClaimBotChecking
import services.submission.{WebServiceClientComponent, ClaimSubmissionService}

class ClaimSubmissionController extends SubmissionController
    with ClaimSubmissionService
    with ClaimTransactionComponent
    with WebServiceClientComponent
    with ClaimBotChecking
    with CachedClaim {
  val webServiceClient = new WebServiceClient
  val claimTransaction = new ClaimTransaction
}
