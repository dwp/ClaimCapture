package controllers.submission

import play.api.mvc._
import models.view._
import play.api.Logger
import services.UnavailableTransactionIdException
import com.google.inject._

@Singleton
class SubmissionController@Inject()(submitter: Submitter) extends Controller with CachedClaim {

  def submit = claiming {
    implicit claim => implicit request =>
      try {
        Async {
          submitter.submit(claim)
        }
      }
      catch {
        case e: UnavailableTransactionIdException => {
          Logger.error(s"UnavailableTransactionIdException ! ${e.getMessage}")
          Redirect("/error")
        }
        case e: java.lang.Exception => {
          Logger.error(s"InternalServerError ! ${e.getMessage}")
          Logger.error(s"InternalServerError ! ${e.getStackTraceString}")
          Redirect("/error")
        }
      }
  }
}