package controllers.submission

import play.api.mvc.{Result, Controller}
import models.view.{CachedChangeOfCircs, CachedClaim}
import services.ClaimTransactionComponent
import services.submission.AsyncClaimSubmissionService
import services.async.AsyncActors
import app.ConfigProperties._
import models.view.CachedClaim._
import models.domain.Claim
import play.api.Play
import scala.util.{Try, Success, Failure}

object AsyncSubmissionController{
  import Play.current

  def asyncCondition = Try(!Play.isTest && !getProperty("submit.prints.xml",false) && getProperty("async.submission",false)) match {
    case Success(s) => s
    case Failure(_) => false
  }
}

class AsyncSubmissionController extends Controller with ClaimTransactionComponent with ClaimSubmittable{

  this: CachedClaim =>

  val claimTransaction = new ClaimTransaction

  def submitAction(claim:Claim):Either[Result, ClaimResult] = {

    val transId = getTransactionIdAndRegisterGenerated(copyInstance(claim))

    val updatedClaim = copyInstance(claim withTransactionId transId)

    AsyncActors.asyncManagerActor ! updatedClaim

    updatedClaim -> Redirect(StatusRoutingController.redirectSubmitting(updatedClaim))
  }

  def getTransactionIdAndRegisterGenerated(claim:Claim) = {
      val transId = claimTransaction.generateId
      claimTransaction.registerId(transId,AsyncClaimSubmissionService.GENERATED,claimType(claim))
      transId
  }
}

class AsyncClaimSubmissionController extends AsyncSubmissionController with CachedClaim
class AsyncCofCSubmissionController extends AsyncSubmissionController with CachedChangeOfCircs
