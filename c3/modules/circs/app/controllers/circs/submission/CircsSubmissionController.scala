package controllers.circs.submission

import play.api.mvc._
import play.api.Logger
import com.google.inject._
import models.view.CachedCircs
import services.UnavailableTransactionIdException
import controllers.submission.Submitter

@Singleton
class CircsSubmissionController @Inject()(submitter: Submitter) extends Controller with CachedCircs {

  def submit = executeOnForm { implicit circs => implicit request =>
    try {
      Async {
        submitter.submit(circs, request)
      }
    }
    catch {
      case e: UnavailableTransactionIdException => {
        Logger.error(s"UnavailableTransactionIdException ! ${e.getMessage}")
        Redirect(s"/error?key=${CachedCircs.key}")
      }
      case e: java.lang.Exception => {
        Logger.error(s"InternalServerError ! ${e.getMessage}")
        Logger.error(s"InternalServerError ! ${e.getStackTraceString}")
        Redirect(s"/error?key=${CachedCircs.key}")
      }
    }
  }
}