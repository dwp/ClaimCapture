package controllers.submission

import play.api.Logger
import models.view.CachedClaim
import services.UnavailableTransactionIdException
import scala.concurrent.{ExecutionContext, Future}
import monitoring.BotChecking
import play.api.mvc.Controller
import jmx.inspectors.SubmissionNotifier
import ExecutionContext.Implicits.global
import services.submission.ClaimSubmissionService

class SubmissionController extends Controller with SubmissionNotifier {

  this : ClaimSubmissionService with BotChecking with CachedClaim =>

  def submit = submitting {
    implicit claim => implicit request => implicit lang =>

      if (isHoneyPotBot(claim)) {
        // Only log honeypot for now.
        // May send to an error page in the future
        Logger.warn(s"Honeypot ! User-Agent : ${request.headers.get("User-Agent").orNull}")
      }

      if (isSpeedBot(claim)) {
        // Only log speed check for now.
        // May send to an error page in the future
        Logger.warn(s"Speed check ! User-Agent : ${request.headers.get("User-Agent").orNull}")
      }

      try {
        fireNotification(claim) {
         submission(claim, request)
        }
      } catch {
        case e: UnavailableTransactionIdException =>
          Logger.error(s"UnavailableTransactionIdException ! ${e.getMessage}")
          Future(Redirect(errorPage))
        case e: java.lang.Exception =>
          Logger.error(s"InternalServerError ! ${e.getMessage}")
          Logger.error(s"InternalServerError ! ${e.getStackTraceString}")
          Future(Redirect(errorPage))
      }
  }

}