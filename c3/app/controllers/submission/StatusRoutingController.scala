package controllers.submission

import play.api.mvc.{Call, Controller}
import models.view.{CachedChangeOfCircs, CachedClaim}
import services.ClaimTransactionComponent
import services.submission.{ClaimSubmissionService, AsyncClaimSubmissionService}
import play.api.Logger
import models.domain.Claim
import AsyncClaimSubmissionService._
import ClaimSubmissionService._

object StatusRoutingController {
  import play.api.mvc.Results._

  def redirectThankYou(implicit claim: Claim):Call = {
    if (claimType(claim) == FULL_CLAIM) controllers.routes.ClaimEnding.thankyou
    else controllers.routes.CircsEnding.thankyou
  }

  def redirectSubmitting(implicit claim: Claim):Call = {
    if (claimType(claim) == FULL_CLAIM) controllers.submission.routes.ClaimStatusRoutingController.present
    else controllers.submission.routes.CofCStatusRoutingController.present
  }

  def redirectErrorRetry(implicit claim: Claim):Call = {
    if (claimType(claim) == FULL_CLAIM) controllers.submission.routes.ClaimStatusRoutingController.errorRetry
    else controllers.submission.routes.CofCStatusRoutingController.errorRetry
  }

  def redirectError(implicit claim: Claim):Call = {
    if (claimType(claim) == FULL_CLAIM) controllers.submission.routes.ClaimStatusRoutingController.error
    else controllers.submission.routes.CofCStatusRoutingController.error
  }

  def redirectTimeout(implicit claim: Claim):Call = {
    if (claimType(claim) == FULL_CLAIM) controllers.routes.ClaimEnding.timeout
    else controllers.routes.CircsEnding.timeout
  }
}

class StatusRoutingController extends Controller with CachedClaim with ClaimTransactionComponent {

  val claimTransaction = new ClaimTransaction

  def present = claiming{ implicit claim =>  implicit request =>  lang =>
    Logger.debug(s"Showing async submitting ${claim.key} ${claim.uuid}")
    Ok(views.html.common.asyncSubmitting(lang))
  }

  def submit = claiming { implicit claim =>  implicit request =>  lang =>
    import StatusRoutingController._

    val transactionStatus = claimTransaction.getTransactionStatusById(claim.transactionId.getOrElse(""))

    Logger.info(s"Checking transaction status: ${transactionStatus} for ${claim.key} ${claim.uuid}")
    transactionStatus match {

      case Some(ts) if ts.status == SUCCESS   || ts.status == ACKNOWLEDGED          => Redirect(redirectThankYou)
      case Some(ts) if ts.status == GENERATED || ts.status == SUBMITTED             => Redirect(redirectSubmitting)
      case Some(ts) if ts.status == ClaimSubmissionService.SERVICE_UNAVAILABLE      => Redirect(redirectErrorRetry)
      case None                                                                     => Redirect(redirectTimeout)
      case _                                                                        => Redirect(redirectError)
    }

  }


  def error = ending { implicit claim =>  implicit request =>  lang =>
    Ok(views.html.common.error(startPage))
  }

  def errorRetry = claiming { implicit claim =>  implicit request =>  lang =>

    if (claimType(claim) == FULL_CLAIM){
      Ok(views.html.common.error_retry(controllers.s12_consent_and_declaration.routes.G3Declaration.present.url))
    }else{
      Ok(views.html.common.error_retry(controllers.circs.s3_consent_and_declaration.routes.G1Declaration.present.url))
    }
  }
}

object ClaimStatusRoutingController extends StatusRoutingController with CachedClaim
object CofCStatusRoutingController extends StatusRoutingController with CachedChangeOfCircs
