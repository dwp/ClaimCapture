package controllers.submission

import play.api.mvc.Controller
import models.view.CachedClaim
import services.ClaimTransactionComponent
import services.submission.AsyncClaimSubmissionService


object StatusRoutingController extends Controller with CachedClaim with ClaimTransactionComponent {

  val claimTransaction = new ClaimTransaction

  def present = claiming{ implicit claim => implicit request => implicit lang =>
    Ok(views.html.common.asyncSubmitting())
  }

  def submit = claiming { implicit claim => implicit request => implicit lang =>

    val transactionStatus = claimTransaction.getTransactionStatusById(claim.transactionId.getOrElse(""))

    //TODO: Check for other cases where we submit to thankyou page
    transactionStatus match {
      case Some(ts) if ts.status == AsyncClaimSubmissionService.SUCCESS => Redirect(controllers.routes.ClaimEnding.thankyou)
      case Some(ts) if ts.status == AsyncClaimSubmissionService.SERVICE_UNAVAILABLE => Redirect(controllers.submission.routes.StatusRoutingController.errorRetry)
      case Some(ts) if ts.status == AsyncClaimSubmissionService.GENERATED => Redirect(controllers.submission.routes.StatusRoutingController.present)
      case None => Redirect(controllers.routes.ClaimEnding.timeout)
      case _ => Redirect(controllers.submission.routes.StatusRoutingController.error)
    }

  }

  def error = claiming { implicit claim => implicit request => implicit lang =>
    Ok(views.html.common.error(startPage))
  }

  def errorRetry = claiming { implicit claim => implicit request => implicit lang =>

    Ok(views.html.common.error_retry(controllers.submission.routes.AsyncSubmissionController.submit().absoluteURL(true)))
  }
}
