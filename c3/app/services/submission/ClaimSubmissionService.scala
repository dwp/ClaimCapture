package services.submission

import app.ConfigProperties._
import app.ReportChange._
import app.XMLValues.NotAsked
import controllers.submission.{StatusRoutingController, claimType}
import models.domain.{CircumstancesDeclaration, Claim, ReportChanges}
import models.view.ClaimHandling.ClaimResult
import models.view.{ClaimHandling, CachedChangeOfCircs, CachedClaim}
import play.api.Logger
import play.api.i18n.Lang
import play.api.mvc.Results.Redirect
import play.api.mvc.{Result, AnyContent, Request}
import play.mvc.Http
import services.ClaimTransactionComponent
import services.async.AsyncActors

trait ClaimSubmissionService {

  this: ClaimTransactionComponent with ClaimHandling =>

  private val JS_ENABLED = 1
  private val JS_DISABLED = 0
  def submission(claim: Claim, request: Request[AnyContent], jsEnabled: Boolean = true): ClaimResult = {

    val transId = getTransactionIdAndRegisterGenerated(copyInstance(claim), if (jsEnabled) JS_ENABLED else JS_DISABLED)

    if (!jsEnabled) {
      Logger.info(s"No JS - Submit ${claim.key} ${claim.uuid}, User-Agent : ${request.headers.get("User-Agent").orNull}, TxnId : [$transId]")
    }

    val updatedClaim = copyInstance(claim withTransactionId transId)

    // actor.receive, which calls async service submission
    AsyncActors.asyncManagerActor ! updatedClaim

    updatedClaim -> Redirect(StatusRoutingController.redirectSubmitting(updatedClaim))
  }

  def getTransactionIdAndRegisterGenerated(claim: Claim, jsEnabled: Int): String = {
    val transId = claimTransaction.generateId
    claimTransaction.registerId(transId, AsyncClaimSubmissionService.GENERATED, claimType(claim), jsEnabled, getProperty("origin.tag", "GB"))
    Logger.info(s"TransactionId [$transId] generated for ${claim.key} ${claim.uuid}.")
    transId
  }

  def respondWithSuccess(claim: Claim, txnId: String, request: Request[AnyContent]): Result = {
    Logger.info(s"Successful submission : ${claim.key}  ${claim.uuid} transactionId [$txnId]")
    claim.key match {
      case CachedClaim.key =>
        Redirect(controllers.routes.ClaimEnding.thankyou())
      case CachedChangeOfCircs.key =>
        Redirect(controllers.routes.CircsEnding.thankyou())
    }
  }
}

object ClaimSubmissionService {
  val SUBMITTED = "0000"
  val ACKNOWLEDGED = "0001"
  val SUCCESS = "0002"
  val UNKNOWN_ERROR = "9001"
  val BAD_REQUEST_ERROR = "9002"
  val REQUEST_TIMEOUT_ERROR = "9003"
  val SERVER_ERROR = "9004"
  val COMMUNICATION_ERROR = "9005"
  val SERVICE_UNAVAILABLE = "9006"
  val BAD_GATEWAY = "9007"
  val INTERNAL_ERROR = "9008"
  val txnStatusConst = Map(
    "UNKNOWN_ERROR" -> UNKNOWN_ERROR,
    "BAD_REQUEST_ERROR" -> BAD_REQUEST_ERROR,
    "REQUEST_TIMEOUT_ERROR" -> REQUEST_TIMEOUT_ERROR,
    "SERVER_ERROR" -> SERVER_ERROR,
    "SERVICE_UNAVAILABLE" -> SERVICE_UNAVAILABLE,
    "BAD_GATEWAY" -> BAD_GATEWAY
  )

  def recordMi(claim: Claim, id: String, recordMI: (String, Boolean, Option[Int], Option[Lang]) => Unit): Unit = {
    val changesMap = Map(
      StoppedCaring.name -> Some(0),
      AddressChange.name -> Some(1),
      SelfEmployment.name -> Some(2),
      PaymentChange.name -> Some(3),
      AdditionalInfo.name -> Some(4),
      BreakFromCaring.name -> Some(5),
      BreakFromCaringYou.name -> Some(7),
      EmploymentChange.name -> Some(6),
      NotAsked -> None
    )
    val declaration = claim.questionGroup[CircumstancesDeclaration].getOrElse(CircumstancesDeclaration())
    val thirdParty = declaration.circsSomeOneElse.isDefined
    val circsChange = changesMap(claim.questionGroup[ReportChanges].getOrElse(ReportChanges()).reportChanges)
    recordMI(id, thirdParty, circsChange, claim.lang)
  }

  def httpStatusCodes(status: Int): String = status match {
    case Http.Status.BAD_REQUEST => "BAD_REQUEST_ERROR"
    case Http.Status.REQUEST_TIMEOUT => "REQUEST_TIMEOUT_ERROR"
    case Http.Status.INTERNAL_SERVER_ERROR => "SERVER_ERROR"
    case Http.Status.BAD_GATEWAY => "BAD_GATEWAY"
    case Http.Status.SERVICE_UNAVAILABLE => "SERVICE_UNAVAILABLE"
    case _ => "UNKNOWN_ERROR"
  }

}
