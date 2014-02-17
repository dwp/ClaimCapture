package controllers.circs.submission

import com.google.inject._
import controllers.submission.Submitter
import models.view.CachedChangeOfCircs
import play.api.Logger
import play.api.mvc.Controller
import services.UnavailableTransactionIdException
import scala.concurrent.{ExecutionContext, Future}
import jmx.inspectors.SubmissionNotifier
import monitoring.ChangeBotChecking
import ExecutionContext.Implicits.global

@Singleton
class ChangeOfCircsSubmissionController @Inject()(submitter: Submitter) extends Controller with SubmissionNotifier with ChangeBotChecking with CachedChangeOfCircs {

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