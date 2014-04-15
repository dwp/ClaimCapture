package controllers.circs.submission

import controllers.submission.SubmissionController
import models.view.CachedChangeOfCircs
import services.ClaimTransactionComponent
import monitoring.ChangeBotChecking
import services.submission.{WebServiceClientComponent, ClaimSubmissionService}

class ChangeOfCircsSubmissionController extends SubmissionController
    with ClaimSubmissionService
    with ClaimTransactionComponent
    with WebServiceClientComponent
    with ChangeBotChecking
    with CachedChangeOfCircs {
  val webServiceClient = new WebServiceClient
  val claimTransaction = new ClaimTransaction
}