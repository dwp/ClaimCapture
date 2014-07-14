package services.submission

import app.ReportChange._
import app.XMLValues._
import play.api.mvc.{AnyContent, Request}
import play.api.Logger
import controllers.submission._
import models.view.{CachedChangeOfCircs, CachedClaim}
import play.api.mvc.Results._
import models.domain.{CircumstancesDeclaration, ReportChanges, Claim}
import services.ClaimTransactionComponent
import play.api.i18n.Lang
import models.view.CachedClaim._
import scala.Some
import play.api.mvc.SimpleResult
import services.async.AsyncActors

trait ClaimSubmissionService {

  this: ClaimTransactionComponent with CachedClaim =>

  def submission(claim: Claim, request: Request[AnyContent], jsEnabled:Boolean = true) : ClaimResult = {

    val transId = getTransactionIdAndRegisterGenerated(copyInstance(claim), if (jsEnabled) 1 else 0)

    if (!jsEnabled) {
      Logger.info(s"No JS - Submit ${claim.key}, User-Agent : ${request.headers.get("User-Agent").orNull}, TxnId : $transId")
    }

    val updatedClaim = copyInstance(claim withTransactionId transId)

    // actor.receive, which calls async service submission
    AsyncActors.asyncManagerActor ! updatedClaim

    updatedClaim -> Redirect(StatusRoutingController.redirectSubmitting(updatedClaim))
  }

  def getTransactionIdAndRegisterGenerated(claim:Claim, jsEnabled:Int) = {
    val transId = claimTransaction.generateId
    claimTransaction.registerId(transId,AsyncClaimSubmissionService.GENERATED,claimType(claim), jsEnabled)
    transId
  }

  def respondWithSuccess(claim: Claim, txnId: String, request: Request[AnyContent]): SimpleResult = {
    Logger.info(s"Successful submission : ${claim.key} : transactionId [$txnId]")
    claim.key match {
      case CachedClaim.key =>
        Redirect(controllers.routes.ClaimEnding.thankyou())
      case CachedChangeOfCircs.key =>
        Redirect(controllers.routes.CircsEnding.thankyou())
    }
  }
}

object ClaimSubmissionService {
  def recordMi(claim: Claim, id: String, recordMI:(String,Boolean,Option[Int],Option[Lang]) => Unit) = {
    val changesMap = Map(StoppedCaring.name -> Some(0), AddressChange.name -> Some(1), SelfEmployment.name -> Some(2), PaymentChange.name -> Some(3), AdditionalInfo.name -> Some(4), BreakFromCaring.name -> Some(5), EmploymentChange.name -> Some(6), NotAsked -> None)
    val declaration = claim.questionGroup[CircumstancesDeclaration].getOrElse(CircumstancesDeclaration())
    val thirdParty = declaration.circsSomeOneElse.isDefined
    val circsChange = changesMap(claim.questionGroup[ReportChanges].getOrElse(ReportChanges()).reportChanges)
    recordMI(id,thirdParty,circsChange,claim.lang)

  }

  val SUBMITTED = "0000"
  val ACKNOWLEDGED = "0001"
  val SUCCESS = "0002"
  val UNKNOWN_ERROR = "9001"
  val BAD_REQUEST_ERROR = "9002"
  val REQUEST_TIMEOUT_ERROR = "9003"
  val SERVER_ERROR = "9004"
  val COMMUNICATION_ERROR = "9005"
  val SERVICE_UNAVAILABLE = "9006"

  val httpStatusCodes = Map(
  400->"BAD_REQUEST_ERROR",
  408->"REQUEST_TIMEOUT_ERROR",
  500->"SERVER_ERROR",
  503->"SERVICE_UNAVAILABLE")

  val txnStatusConst = Map(
    "BAD_REQUEST_ERROR"->"9002",
    "REQUEST_TIMEOUT_ERROR"->"9003",
    "SERVER_ERROR"->"9004",
    "SERVICE_UNAVAILABLE"->"9006")

}
