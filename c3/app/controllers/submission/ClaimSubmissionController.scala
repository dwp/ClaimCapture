package controllers.submission

import play.api.Logger
import com.google.inject._
import models.view.CachedClaim
import services.UnavailableTransactionIdException
import scala.concurrent.{ExecutionContext, Future}
import monitoring.ClaimBotChecking
import play.api.mvc.Controller
import jmx.inspectors.SubmissionNotifier
import ExecutionContext.Implicits.global

@Singleton
class ClaimSubmissionController @Inject()(submitter: Submitter) extends Controller with SubmissionNotifier with ClaimBotChecking with CachedClaim {

  def submit = submitting {
    implicit claim => implicit request =>

      if (isHoneyPotBot(claim)) {
        // Only log honeypot for now.
        // May send to an error page in the future
        Logger.warn(s"Honeypot ! Headers : ${request.headers}")
      }

      if (isSpeedBot(claim)) {
        // Only log speed check for now.
        // May send to an error page in the future
        Logger.warn(s"Speed check ! Headers : ${request.headers}")
      }

      try {
        fireNotification(claim) {
          submitter.submit(claim, request)
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