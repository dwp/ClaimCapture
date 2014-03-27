package controllers.submission

import play.api.mvc.Controller
import models.view.CachedClaim
import services.ClaimTransactionComponent
import services.submission.AsyncClaimSubmissionService
import services.async.AsyncActors
import app.ConfigProperties._
import models.domain.Claim


object AsyncSubmissionController extends Controller with CachedClaim with ClaimTransactionComponent{

  val claimTransaction = new ClaimTransaction

  def submit = claiming{ implicit claim => implicit request => implicit lang =>


    val transId = getTransactionIdAndRegisterGenerated(claim)

    val updatedClaim = copyInstance(claim withTransactionId transId)

    AsyncActors.asyncManagerActor ! updatedClaim

    updatedClaim -> Redirect(controllers.submission.routes.StatusRoutingController.present)
  }

  def getTransactionIdAndRegisterGenerated(claim:Claim) = {
      val transId = claimTransaction.generateId
      claimTransaction.registerId(transId,AsyncClaimSubmissionService.GENERATED,claimType(claim))
      transId

  }

}
